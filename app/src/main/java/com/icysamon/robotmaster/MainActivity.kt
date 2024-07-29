package com.icysamon.robotmaster

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            Toast.makeText(this, "RESULT_OK", Toast.LENGTH_LONG).show()
            // Handle the Intent
        }
    }


    // 権限ランチャー
    private val requestPermissionLauncher =
        registerForActivityResult(
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestBluetoothPermission()
    }


    // Robot Transform
    fun onButtonBottomClick(view: View) {
        val robot = findViewById<ImageView>(R.id.simulation_robot)
        robot.translationX += 50F
        println(view)
    }


    // Bluetooth
    private fun bluetoothInit() {
        // bluetooth init
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter


        // check bluetoothAdapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Device doesn't support Bluetooth.", Toast.LENGTH_LONG).show()
        }

        // get bluetooth
        if (bluetoothAdapter?.isEnabled == false) {
            Toast.makeText(this, "bluetoothAdapter is not enable", Toast.LENGTH_LONG).show()

            // Bluetooth を有効する
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startForResult.launch(intent)
        }
    }

    private fun requestBluetoothPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED -> {
                bluetoothInit()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) -> {
                Toast.makeText(this, "Bluetooth 権限は必要にゃ。", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }

            else -> {
                Toast.makeText(this, "Bluetooth 権限は必要にゃ。", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }
        }
    }



}