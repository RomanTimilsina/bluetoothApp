////package com.example.newapp
////
////import android.bluetooth.BluetoothAdapter
////import android.bluetooth.BluetoothDevice
////import android.content.BroadcastReceiver
////import android.content.Context
////import android.content.Intent
////import android.content.IntentFilter
////import android.os.Bundle
////import android.widget.Toast
////import androidx.activity.ComponentActivity
////import androidx.activity.compose.setContent
////import androidx.compose.foundation.layout.*
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.unit.dp
////import androidx.compose.ui.unit.sp
////
////class MainActivity : ComponentActivity() {
////
////    // Create a BroadcastReceiver for ACTION_FOUND.
////
////    private lateinit var bluetoothAdapter: BluetoothAdapter
////
////    private val receiver = object : BroadcastReceiver() {
////
////        override fun onReceive(context: Context, intent: Intent) {
////            val action: String? = intent.action
////            when(action) {
////                BluetoothDevice.ACTION_FOUND -> {
////                    // Discovery has found a device. Get the BluetoothDevice
////                    // object and its info from the Intent.
////                    val device: BluetoothDevice? =
////                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
////                    val deviceName = device?.name ?: "Unknown"
////                    val deviceHardwareAddress = device?.address ?: "Unknown" // MAC address
////                }
////            }
////        }
////    }
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
////
////        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
////        registerReceiver(receiver, filter)
////
////        setContent {
////            MaterialTheme {
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = MaterialTheme.colorScheme.background
////                ) {
////                    // Centered column
////                    Column(
////                        modifier = Modifier.fillMaxSize(),
////                        verticalArrangement = Arrangement.Center,
////                        horizontalAlignment = Alignment.CenterHorizontally
////                    ) {
////                        // Simple clickable button
////                        Button(
////                            onClick = {
////                                val started = bluetoothAdapter.startDiscovery()
////                                if (started) {
////                                    Toast.makeText(
////                                        this@MainActivity,
////                                        "Scanning for devices...",
////                                        Toast.LENGTH_SHORT
////                                    ).show()
////                                }
////                            },
////                            modifier = Modifier.padding(16.dp)
////                        ) {
////                            Text(text = "Click Me", fontSize = 18.sp)
////                        }
////                    }
////                }
////            }
////        }
////    }
////
////    override fun onDestroy() {
////        super.onDestroy()
////
////        // Don't forget to unregister the ACTION_FOUND receiver.
////        unregisterReceiver(receiver)
////    }
////}
//
//
//package com.example.newapp
//
//import android.Manifest
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//class MainActivity : ComponentActivity() {
//
//    private lateinit var bluetoothAdapter: BluetoothAdapter
//    private val devicesList = mutableStateListOf<String>()
//
//    private val receiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            if (intent.action == BluetoothDevice.ACTION_FOUND) {
//                val device: BluetoothDevice? =
//                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//
//                // Only access device info if permissions are granted
//                if (device != null &&
//                    checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    val name = device.name ?: "Unknown"
//                    val address = device.address
//                    val info = "$name\n$address"
//                    if (!devicesList.contains(info)) {
//                        devicesList.add(info)
//                        Toast.makeText(context, "Found: $info", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show()
//            finish()
//            return
//        }
//
//        // Request runtime permissions
//        val requestPermissionsLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            val scanGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] == true
//            val connectGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
//            val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
//
//            if (!scanGranted || !connectGranted || !locationGranted) {
//                Toast.makeText(
//                    this,
//                    "Bluetooth and location permissions are required",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        // Launch permission request
//        val permissionList = mutableListOf(
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            permissionList.add(Manifest.permission.BLUETOOTH_SCAN)
//            permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
//        }
//        requestPermissionsLauncher.launch(permissionList.toTypedArray())
//
//        // Register receiver for discovered devices
//        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//        registerReceiver(receiver, filter)
//
//        setContent {
//            MaterialTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Button(
//                            onClick = {
//                                if (bluetoothAdapter.isEnabled) {
//                                    // Check for scan permission before starting
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
//                                        checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
//                                    ) {
//                                        Toast.makeText(
//                                            this@MainActivity,
//                                            "Bluetooth scan permission not granted",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        return@Button
//                                    }
//
//                                    devicesList.clear()
//                                    val started = bluetoothAdapter.startDiscovery()
//                                    if (started) {
//                                        Toast.makeText(
//                                            this@MainActivity,
//                                            "Scanning for devices...",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    } else {
//                                        Toast.makeText(
//                                            this@MainActivity,
//                                            "Could not start scanning",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                } else {
//                                    Toast.makeText(
//                                        this@MainActivity,
//                                        "Please enable Bluetooth",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            },
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text("Scan Nearby Bluetooth Devices", fontSize = 18.sp)
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Show discovered devices
//                        LazyColumn(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .weight(1f) // take remaining space
//                                .padding(16.dp)
//                        ) {
//                            items(devicesList) { device ->
//                                Text(text = device, fontSize = 16.sp)
//                                Divider()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(receiver)
//    }
//}
//

package com.example.newapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import android.bluetooth.BluetoothServerSocket


class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val NAME = "MyAppBluetoothServer"
        private val MY_UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    val uuid = java.util.UUID.fromString(
        "00001101-0000-1000-8000-00805F9B34FB"
    )

    private lateinit var bluetoothAdapter: BluetoothAdapter

    val devicesList = mutableStateListOf<BluetoothDevice>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    val name = device.name ?: "Unknown"
                    val address = device.address
                    if ( name != "Unknown") {
                        val info = "$name\n$address"
                        if (!devicesList.contains(device)) {
                            devicesList.add(device)
                            Toast.makeText(context, "Found: $info", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
//        BluetoothConnectionManager.socket = socket
//        BluetoothConnectionManager.startListening()

//
//        BluetoothConnectionManager.socket = socket
//
        runOnUiThread {

            Toast.makeText(
                this,
                "Connection successful",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }
//
        Thread {

            try {

                val inputStream = socket.inputStream
                val outputStream = socket.outputStream

                // Example: send message
//                val message = "Hello from ${bluetoothAdapter.name}"
//                outputStream.write(message.toByteArray())

                ChatActivity.sendCallback = { message ->

                    Thread {
                        try {

                            outputStream.write(message.toByteArray())

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }.start()

                }

                // Listen for incoming messages
                val buffer = ByteArray(1024)

                while (true) {

                    val bytes = inputStream.read(buffer)

                    val receivedMessage = String(buffer, 0, bytes)

                    runOnUiThread {

                        ChatActivity.messages.add(
                            ChatMessage(
                                text = receivedMessage,
                                isMine = false
                            )
                        )

                    }


                    runOnUiThread {

                        Toast.makeText(
                            this,
                            "Received: $receivedMessage",
                            Toast.LENGTH_LONG
                        ).show()

                    }
//                    BluetoothConnectionManager.receiveCallback?.invoke(receivedMessage)

                }
//
            } catch (e: IOException) {

                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Connection lost",
                        Toast.LENGTH_LONG
                    ).show()
                }

                e.printStackTrace()
            }

        }.start()
    }


    private fun connectToDevice(device: BluetoothDevice) {

        Thread {

            try {

                runOnUiThread {
                    Toast.makeText(this, "Connecting to ${device.name}", Toast.LENGTH_SHORT).show()
                }

                bluetoothAdapter.cancelDiscovery()

//                val uuid = java.util.UUID.fromString(
//                    "00001101-0000-1000-8000-00805F9B34FB"
//                )

                val socket = device.createRfcommSocketToServiceRecord(uuid)

                socket.connect()

                manageMyConnectedSocket(socket)

                runOnUiThread {
                    Toast.makeText(this, "Connected to ${device.name}", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {

                runOnUiThread {
                    Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show()
                }

                e.printStackTrace()
            }

        }.start()
    }

    private var acceptThread: AcceptThread? = null

    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, uuid)
        }

        override fun run() {
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept() // Blocks until a client connects
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }

                socket?.also {
                    // Connection accepted, now handle communication
                    manageMyConnectedSocket(it)

                    // Close server socket to stop listening after first connection
                    try { mmServerSocket?.close() } catch (_: IOException) {}
                    shouldLoop = false
                }
            }
        }

        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the server socket", e)
            }
        }
    }

    fun startServer() {
        acceptThread = AcceptThread()
        acceptThread?.start()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show()
            finish()
            return
        }


        // Register the receiver
        registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        // Permissions launcher
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.all { it.value }
            if (!allGranted) {
                Toast.makeText(
                    this,
                    "Permissions required for Bluetooth scanning",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Request all permissions
        val permissionList = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionList.add(Manifest.permission.BLUETOOTH_SCAN)
            permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        permissionLauncher.launch(permissionList.toTypedArray())




        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(
                            onClick = {
                                // Check permissions before scanning
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                                        checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Bluetooth permissions not granted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }
                                }

                                if (!bluetoothAdapter.isEnabled) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Please enable Bluetooth",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                devicesList.clear()
                                val started = bluetoothAdapter.startDiscovery()
                                if (started) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Scanning for devices...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Could not start scanning",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Scan Nearby Bluetooth Devices", fontSize = 18.sp)
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            {
                                startServer()
                            }
                        ) {
                            Text("Start Server")
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(16.dp)
                        ) {

                            items(devicesList) { device ->

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            connectToDevice(device)

//                                            acceptThread?.start()
                                        }
                                        .padding(12.dp)
                                ) {

                                    Text(
                                        text = device.name ?: "Unknown",
                                        fontSize = 18.sp
                                    )

                                    Text(
                                        text = device.address,
                                        fontSize = 14.sp
                                    )
                                }

                                Divider()
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(receiver) } catch (e: Exception) { }
    }
}
