package com.icysamon.robotmaster

import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
        ble.requestBluetoothPermission()
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