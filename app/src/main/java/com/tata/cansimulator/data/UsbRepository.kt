//package com.tata.cansimulator.data
//
//
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.hardware.usb.*
//import android.os.Build
//import android.util.Log
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.*
//import java.nio.charset.Charset
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlin.math.min
//import kotlin.math.pow
//
//@Singleton
//class UsbRepository @Inject constructor(
//    private val context: Context
//) {
//    private val TAG = "UsbRepository"
//
//    // Exposed connection state & data flows
//    private val _connectionState = MutableStateFlow(ConnectionState.Disconnected)
//    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
//
//    private val _dataFlow = MutableSharedFlow<String>(replay = 1)
//    val dataFlow: SharedFlow<String> = _dataFlow.asSharedFlow()
//
//    // coroutine scope for IO and long-living jobs (service lifetime)
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//
//    // USB objects
//    private var usbDevice: UsbDevice? = null
//    private var deviceConnection: UsbDeviceConnection? = null
//
//    // read job
//    private var readJob: Job? = null
//
//    // reconnect/backoff state
//    private var reconnectAttempts = 0
//    private val maxReconnectAttempts = 6
//
//    companion object {
//        const val ACTION_USB_PERMISSION = "com.example.usbserial.USB_PERMISSION"
//    }
//
//    // ========== PUBLIC API ==========
//
//    // Set the device discovered by BroadcastReceiver
//    fun setDevice(device: UsbDevice) {
//        usbDevice = device
//        log("device set: ${device.deviceName}")
//    }
//
//    // Request system permission dialog. The BroadcastReceiver should listen for ACTION_USB_PERMISSION and call onPermissionResult.
//    fun requestPermission() {
//        val manager = context.getSystemService(Context.USB_SERVICE) as UsbManager
//        val device = usbDevice ?: run {
//            log("requestPermission: usbDevice is null")
//            return
//        }
//        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
//        val pending = PendingIntent.getBroadcast(context, 0, Intent(ACTION_USB_PERMISSION), flags)
//        manager.requestPermission(device, pending)
//        _connectionState.value = ConnectionState.PermissionRequested
//    }
//
//    // Called after permission broadcast with result=true
//    fun openDeviceWithPermission() {
//        val manager = context.getSystemService(Context.USB_SERVICE) as UsbManager
//        val device = usbDevice ?: run {
//            _connectionState.value = ConnectionState.Error("No device")
//            return
//        }
//        if (!manager.hasPermission(device)) {
//            _connectionState.value = ConnectionState.PermissionRequired
//            return
//        }
//
//        try {
//            deviceConnection?.close()
//            deviceConnection = manager.openDevice(device)
//            if (deviceConnection == null) {
//                _connectionState.value = ConnectionState.Error("openDevice returned null")
//                return
//            }
//
//            // INSPECT device interfaces -> choose correct interface.
//            // By default try to claim interface 0, but later we provide inspector utility to help you pick.
//            val intf = pickUsbInterface(device)
//            if (intf == null) {
//                _connectionState.value = ConnectionState.Error("No appropriate interface")
//                return
//            }
//
//            val claimed = deviceConnection!!.claimInterface(intf, true)
//            if (!claimed) {
//                _connectionState.value = ConnectionState.Error("Cannot claim interface ${intf.id}")
//                return
//            }
//
//            // find IN endpoint (bulk) to read from
//            val inEndpoint = findInEndpoint(intf)
//            if (inEndpoint == null) {
//                _connectionState.value = ConnectionState.Error("No IN endpoint found")
//                return
//            }
//
//            log("Device opened, interface=${intf.id}, endpoint=${inEndpoint.endpointNumber}")
//            _connectionState.value = ConnectionState.Connected
//            reconnectAttempts = 0
//            startReadLoop(deviceConnection!!, inEndpoint)
//        } catch (e: Exception) {
//            log("openDevice error: ${e.message}")
//            _connectionState.value = ConnectionState.Error(e.message ?: "open error")
//            scheduleReconnectWithBackoff()
//        }
//    }
//
//    // Stop reading and close connection (e.g., device detached)
//    fun stopAndClose() {
//        log("stopAndClose called")
//        readJob?.cancel()
//        readJob = null
//        try { deviceConnection?.close() } catch (e: Exception) { /* ignore */ }
//        deviceConnection = null
//        usbDevice = null
//        _connectionState.value = ConnectionState.Disconnected
//    }
//
//    // Call when system reports device detached
//    fun onDeviceDetached() {
//        log("device detached")
//        stopAndClose()
//    }
//
//    // Debug helper: inspect device interfaces/endpoints and emit info to dataFlow (so UI shows it)
//    fun inspectDeviceAndEmitInfo(device: UsbDevice) {
//        val sb = StringBuilder()
//        sb.append("Device: ").append(device.deviceName).append("\n")
//        sb.append("VendorId=").append(device.vendorId).append(" ProductId=").append(device.productId).append("\n")
//        for (i in 0 until device.interfaceCount) {
//            val intf = device.getInterface(i)
//            sb.append("Interface[$i]: class=").append(intf.interfaceClass)
//                .append(" subclass=").append(intf.interfaceSubclass)
//                .append(" protocol=").append(intf.interfaceProtocol)
//                .append(" endpoints=").append(intf.endpointCount).append("\n")
//            for (e in 0 until intf.endpointCount) {
//                val ep = intf.getEndpoint(e)
//                sb.append("  EP[$e]: dir=").append(ep.direction).append(" type=").append(ep.type)
//                    .append(" num=").append(ep.endpointNumber)
//                    .append(" maxPacket=").append(ep.maxPacketSize).append("\n")
//            }
//        }
//        scope.launch { _dataFlow.emit(sb.toString()) }
//    }
//
//    // ========== INTERNALS ==========
//
//    private fun pickUsbInterface(device: UsbDevice): UsbInterface? {
//        // Default: try CDC/ACM-like class (USB_CLASS_COMM or USB_CLASS_CDC_DATA) or interface 0
//        for (i in 0 until device.interfaceCount) {
//            val intf = device.getInterface(i)
//            // Android USB classes: 2 = COMM, 10 = CDC_DATA, 0xff = vendor-specific
//            if (intf.interfaceClass == UsbConstants.USB_CLASS_COMM ||
//                intf.interfaceClass == UsbConstants.USB_CLASS_CDC_DATA ||
//                intf.interfaceClass == UsbConstants.USB_CLASS_VENDOR_SPEC) {
//                return intf
//            }
//        }
//        // fallback to interface 0
//        return if (device.interfaceCount > 0) device.getInterface(0) else null
//    }
//
//    private fun findInEndpoint(intf: UsbInterface): UsbEndpoint? {
//        for (i in 0 until intf.endpointCount) {
//            val ep = intf.getEndpoint(i)
//            if (ep.type == UsbConstants.USB_ENDPOINT_XFER_BULK && ep.direction == UsbConstants.USB_DIR_IN) {
//                return ep
//            }
//        }
//        return null
//    }
//
//    private fun startReadLoop(conn: UsbDeviceConnection, inEndpoint: UsbEndpoint) {
//        readJob?.cancel()
//        readJob = scope.launch {
//            val buf = ByteArray(maxOf(inEndpoint.maxPacketSize, 64))
//            while (isActive) {
//                try {
//                    // bulkTransfer returns number of bytes read or negative on error
//                    val len = conn.bulkTransfer(inEndpoint, buf, buf.size, 1000)
//                    if (len > 0) {
//                        val s = String(buf, 0, len, Charset.forName("UTF-8"))
//                        _dataFlow.emit(s)
//                    } else {
//                        // len <= 0 means no data within timeout; continue
//                        // optionally detect prolonged no-data as error
//                    }
//                } catch (ce: CancellationException) {
//                    throw ce
//                } catch (e: Exception) {
//                    log("read loop error: ${e.message}")
//                    _connectionState.value = ConnectionState.Error(e.message ?: "read error")
//                    break
//                }
//            }
//            // loop ended - schedule reconnect perhaps
//            scheduleReconnectWithBackoff()
//        }
//    }
//
//    // exponential backoff reconnect attempts
//    private fun scheduleReconnectWithBackoff() {
//        if (reconnectAttempts >= maxReconnectAttempts) {
//            log("max reconnect reached, giving up")
//            return
//        }
//        reconnectAttempts++
//        val delayMs = min(30_000L, (2.0.pow(reconnectAttempts.toDouble()) * 1000L).toLong())
//        log("scheduling reconnect in ${delayMs}ms (attempt $reconnectAttempts)")
//        scope.launch {
//            delay(delayMs)
//            tryReconnect()
//        }
//    }
//
//    private fun tryReconnect() {
//        log("tryReconnect: usbDevice=${usbDevice?.deviceName}")
//        val manager = context.getSystemService(Context.USB_SERVICE) as UsbManager
//        val device = usbDevice
//        if (device == null) {
//            log("tryReconnect: no device to reconnect")
//            return
//        }
//        if (!manager.hasPermission(device)) {
//            log("tryReconnect: no permission -> requestPermission")
//            requestPermission()
//            return
//        }
//        openDeviceWithPermission()
//    }
//
//    // simple logging helper
//    private fun log(msg: String) {
//        Log.d(TAG, msg)
//    }
//
//    // ========== Connection states ==========
//    sealed class ConnectionState {
//        object Connected : ConnectionState()
//        object Disconnected : ConnectionState()
//        object PermissionRequested : ConnectionState()
//        object PermissionRequired : ConnectionState()
//        data class Error(val reason: String) : ConnectionState()
//    }
//}
