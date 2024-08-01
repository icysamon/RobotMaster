package com.icysamon.robotmaster

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Bluetooth(private val appCompatActivity: AppCompatActivity) {


    private var bluetoothAdapter: BluetoothAdapter? = null

    private val startForResult = appCompatActivity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            Toast.makeText(appCompatActivity, "RESULT_OK", Toast.LENGTH_LONG).show()
            // Handle the Intent
        }
    }


    // 権限リクエストランチャー
    private val requestPermissionLauncher =
        appCompatActivity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // 権限を取得できた
                Log.i("Permission: ", "Granted")
            } else {
                // 権限を取得できなかった
                Log.i("Permission: ", "Denied")
            }
        }


    // Create a BroadcastReceiver for ACTION_FOUND
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(appCompatActivity, "onReceive", Toast.LENGTH_SHORT).show()
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    if (ActivityCompat.checkSelfPermission(
                            appCompatActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED) {
                        requestBluetoothConnectPermission()
                    } else {
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        val deviceName = device?.name
                        val deviceHardwareAddress = device?.address // Mac address
                        Toast.makeText(appCompatActivity, deviceName.toString(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(appCompatActivity, deviceHardwareAddress.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    // Bluetooth の初期化
    fun init() {
        // bluetooth init
        val bluetoothManager: BluetoothManager = appCompatActivity.getSystemService (
            BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter


        // check bluetoothAdapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(appCompatActivity, "Device doesn't support Bluetooth.", Toast.LENGTH_LONG).show()
        }

        // get bluetooth
        if (bluetoothAdapter?.isEnabled == false) {
            Toast.makeText(appCompatActivity, "bluetoothAdapter is not enable", Toast.LENGTH_LONG).show()

            // Bluetooth を有効する
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startForResult.launch(intent)
        }


        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        appCompatActivity.registerReceiver(receiver, filter)
        Log.i("bluetooth class", "init")

    }


    // BLUETOOTH_CONNECT 権限を要求する
    fun requestBluetoothConnectPermission() {
        when {
            ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED -> {
                init()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                appCompatActivity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) -> {
                Toast.makeText(appCompatActivity, "Bluetooth 権限は必要にゃ。", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }

            else -> {
                Toast.makeText(appCompatActivity, "Bluetooth 権限は必要にゃ。", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }
        }
    }

    // BLUETOOTH_SCAN 権限を要求する
    fun requestBluetoothScanPermission() {
        when {
            ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED -> {
                init()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                appCompatActivity,
                Manifest.permission.BLUETOOTH_SCAN
            ) -> {
                Toast.makeText(appCompatActivity, "BLUETOOTH_SCAN 権限は必要にゃ。", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH_SCAN
                )
            }

            else -> {
                Toast.makeText(appCompatActivity, "BLUETOOTH_SCAN 権限は必要にゃ。", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH_SCAN
                )
            }
        }
    }


    fun scanDevice() {
        if (ActivityCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBluetoothScanPermission()
            return
        } else {
            bluetoothAdapter?.startDiscovery()
            Toast.makeText(appCompatActivity, "startDiscovery()", Toast.LENGTH_SHORT).show()
        }
    }





    fun onDestroy() {
        appCompatActivity.unregisterReceiver(receiver)
    }

    fun checkBondedDevices() {
        //Toast.makeText(appCompatActivity, "check bonded", Toast.LENGTH_LONG).show()
        if (ActivityCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED) {
            requestBluetoothConnectPermission()
        } else {
            var deviceName: String = "null"
            var deviceHardwareAddress: String = "null"
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                deviceName = device.name
                deviceHardwareAddress = device.address // MAC address
            }
            Toast.makeText(appCompatActivity, "bonded: $deviceName", Toast.LENGTH_LONG).show()
        }
    }
}