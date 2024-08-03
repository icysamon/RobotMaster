package com.icysamon.robotmaster

import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val ble = Bluetooth(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ble.init()
        ble.requestBluetoothConnectPermission()
        ble.requestBluetoothScanPermission()

        val bondedDevice: List<String> = ble.checkBondedDevices()
        val listView = findViewById<ListView>(R.id.bonded_device_list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bondedDevice)
        listView.adapter = adapter






    }


    // Robot Transform
    fun onButtonBottomClick(view: View) {
        val robot = findViewById<ImageView>(R.id.simulation_robot)
        robot.translationX += 50F
        println(view)
    }

    fun onButtonFindDeviceClick(view: View) {
        ble.scanDevice()


    }
}