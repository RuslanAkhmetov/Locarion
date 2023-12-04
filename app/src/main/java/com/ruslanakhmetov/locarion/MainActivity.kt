package com.ruslanakhmetov.locarion

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_LOCATION_PERMISSION = 2
        const val TAG = "MainActivity"
        const val MINIMUM_DISTANCE_FOR_UPDATES = 1f // в метрах
        const val MINIMUM_TIME_BETWEEN_UPDATES: Long = 500L //millisec
        const val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        const val locationUpdateRC = 0
        const val flags = PendingIntent.FLAG_MUTABLE

    }

    lateinit var locationManager: LocationManager

    //lateinit var locationListener: LocationListener
    lateinit var latitudeTextView: TextView
    lateinit var longitudeTextView: TextView
    lateinit var altitudeTextView: TextView
    lateinit var location: Location
    lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        latitudeTextView = findViewById<TextView>(R.id.textLatitude)
        longitudeTextView = findViewById<TextView>(R.id.textLongitude)
        altitudeTextView = findViewById<TextView>(R.id.textAltitude)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val permissions = arrayOf<String>(locationPermission)

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE
        criteria.powerRequirement = Criteria.POWER_LOW
        criteria.isAltitudeRequired = true
        criteria.isBearingRequired = false
        criteria.isCostAllowed = true

        val provider = locationManager.getBestProvider(criteria, true)?.let { provider ->

            if (checkSelfPermission(locationPermission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, REQUEST_LOCATION_PERMISSION)
            } else {
                locationManager.getLastKnownLocation(provider)?.let {
                    location = it
                    showCurrentLocation(location)

                    val intent = Intent(this, LocationUpdateReceiver::class.java)

                    pendingIntent =
                        PendingIntent.getBroadcast(this, locationUpdateRC, intent, flags)

                    locationManager.requestLocationUpdates(
                        provider,
                        MINIMUM_TIME_BETWEEN_UPDATES,
                        MINIMUM_DISTANCE_FOR_UPDATES,
                        pendingIntent
                    )
                }
            }
        }


/*        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

        val permissions = arrayOf<String>(locationPermission)
        if (checkSelfPermission(locationPermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_LOCATION_PERMISSION)

        } else {
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    latitudeTextView.text = location.latitude.toString()
                    longitudeTextView.text = location.longitude.toString()
                    altitudeTextView.text = location.altitude.toString()
                }

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    Toast.makeText(
                        this@MainActivity,
                        "Статус провайдера поменялся",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onProviderDisabled(provider: String) {
                    Toast.makeText(
                        this@MainActivity,
                        "Провайдер заблокирован пользователем. GPS выключен",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onProviderEnabled(provider: String) {
                    Toast.makeText(
                        this@MainActivity,
                        "Провайдер включён пользователем. GPS включён",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        }*/

        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
/*
        buttonUpdate.setOnClickListener {
            showCurrentLocation()
        }
*/

    }

/*    override fun onResume() {
        super.onResume()
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Просим пользователя включить GPS
            val builder = AlertDialog.Builder(this).apply {
                setTitle("Настройка")
                setMessage("GPS выключен. Включить?")
                setPositiveButton(
                    "Да"
                ) { _, _ ->
                    val intent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    startActivity(intent)
                }
                setNegativeButton("Нет") { _, _ ->
                    finish()
                }
                create().show()
            }
        }
        // Регистрируемся для обновлений
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_FOR_UPDATES,
                locationListener
            )

            showCurrentLocation()
        }


    }*/

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(pendingIntent)
     //   locationManager.removeUpdates(locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Мы получили разрешение", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Пользователь отклонил запрос", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    protected fun showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                latitudeTextView.text = location.latitude.toString()
                longitudeTextView.text = location.longitude.toString()
                altitudeTextView.text = location.altitude.toString()
            }
        }
    }

    protected fun showCurrentLocation(location: Location) {
        latitudeTextView.text = location.latitude.toString()
        longitudeTextView.text = location.longitude.toString()
        altitudeTextView.text = location.altitude.toString()
    }


}
