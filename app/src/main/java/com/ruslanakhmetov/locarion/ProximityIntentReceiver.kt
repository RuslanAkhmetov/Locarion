package com.ruslanakhmetov.locarion

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.util.Log

class ProximityIntentReceiver: BroadcastReceiver() {
    val NOTIFICATION_ID = 1000
    val TAG = ProximityIntentReceiver::class.java.name
    override fun onReceive(context: Context?, intent: Intent?) {
        val key = LocationManager.KEY_PROXIMITY_ENTERING

        val entering = intent?.getBooleanExtra(key, false)?:false
        if(entering){
            Log.i(TAG, "onReceive: вход в зону")
        } else{
            Log.i(TAG, "onReceive: выход из зоны")
        }
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notification = createNotification(context, "Внимание", "Вы находитесь рядом с выбранной точкой", pendingIntent)
    }

    private fun createNotification(context: Context?, title: String, text: String, pendingIntent: PendingIntent?): Notification {
        val builder = Notification.Builder(context).apply {
            setWhen(System.currentTimeMillis())
            setAutoCancel(true)
            setLights(Color.WHITE, 1500, 1500)
            setDefaults(Notification.DEFAULT_VIBRATE and Notification.DEFAULT_LIGHTS)
            setContentTitle(title)
            setContentText(text)
        }
        return builder.build()

    }
}