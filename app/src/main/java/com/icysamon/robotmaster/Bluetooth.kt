package com.icysamon.robotmaster

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Bluetooth(private val appCompatActivity: AppCompatActivity) {

    private val startForResult = appCompatActivity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            Toast.makeText(appCompatActivity, "RESULT_OK", Toast.LENGTH_LONG).show()
            // Handle the Intent
        }
    }


    // 権限ランチャー
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


    // Bluetooth
    fun init() {
        // bluetooth init
        val bluetoothManager: BluetoothManager = appCompatActivity.getSystemService (
            BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter


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

        Log.i("bluetooth class", "init")
    }



    fun requestBluetoothPermission() {
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

}