package com.example.homework33

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var startWorkingButton: Button
    private lateinit var stopWorkingButton: Button

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startWorkingButton = findViewById(R.id.startWorkingButton)
        stopWorkingButton = findViewById(R.id.stopWorkingButton)

        startWorkingButton.setOnClickListener {
            checkLocationPermissions()
        }

        stopWorkingButton.setOnClickListener {
            stopService(Intent(this, WorkService::class.java))
        }
    }

    private fun checkLocationPermissions() {
        val fineLocationGranted: Boolean = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted: Boolean = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted || !coarseLocationGranted) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            startService(Intent(this, WorkService::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startService(Intent(this, WorkService::class.java))
            } else {
                Toast.makeText(this, "Доступ к местоположению запрещен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}