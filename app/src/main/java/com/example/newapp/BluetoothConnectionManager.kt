package com.example.newapp

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.Executors

object BluetoothConnectionManager {

    private val TAG = "BluetoothConnectionMgr"

     var socket: BluetoothSocket? = null




     var inputStream: InputStream? = null
     var outputStream: OutputStream? = null

    private val executor = Executors.newSingleThreadExecutor()

    private var receiveCallback: ((String) -> Unit)? = null

//    fun setSocket(btSocket: BluetoothSocket) {
//        socket = btSocket
//        try {
//            inputStream = btSocket.inputStream
//            outputStream = btSocket.outputStream
//        } catch (e: IOException) {
//            Log.e(TAG, "Error getting streams", e)
//        }
//
//        startListening()
//    }

    fun sendMessage(message: String) {
        executor.execute {
            try {
                outputStream?.write((message + "\n").toByteArray())
            } catch (e: IOException) {
                Log.e(TAG, "Error sending message", e)
            }
        }
    }

//    private fun startListening() {
//        executor.execute {
//            val buffer = ByteArray(1024)
//            while (socket != null) {
//                try {
//                    val bytesRead = inputStream!!.read(buffer)
//                    if (bytesRead > 0) {
//                        val msg = String(buffer, 0, bytesRead).trim()
//                        receiveCallback?.invoke(msg) // Send to chat screen
//                    }
//                } catch (e: IOException) {
//                    Log.e(TAG, "Error reading message", e)
//                    break
//                }
//            }
//        }
//    }

    fun startListening() {
        executor.execute {
            val buffer = ByteArray(1024)

            while (inputStream != null) {

                val bytes = inputStream?.read(buffer)

                val receivedMessage = String(buffer, 0, bytes!!)
                receiveCallback?.invoke(receivedMessage)


            }
        }
    }



    fun setReceiveCallback(callback: (String) -> Unit) {
        receiveCallback = callback
    }

    fun closeConnection() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing socket", e)
        }
        socket = null
        inputStream = null
        outputStream = null
    }
}
