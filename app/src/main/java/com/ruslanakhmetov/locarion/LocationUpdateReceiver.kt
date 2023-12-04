package com.ruslanakhmetov.locarion

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.util.Log

class LocationUpdateReceiver: BroadcastReceiver() {
    val TAG = "LocationUpdateReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        val key = LocationManager.KEY_LOCATION_CHANGED
        val location = intent?.extras?.get(key) as Location

        val message = "Новое местоположение \n Долгота: ${location.longitude} \n Широта: ${location.latitude}"
        Log.i(TAG, "onReceive: $message")
    }
}