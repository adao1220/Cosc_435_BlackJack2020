package com.example.blackjack2020

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationServices : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val id = THREAD_ID++
        val copy = id
        showNotification(copy)

        return START_NOT_STICKY
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Todo Channel"
            val descriptionText = "Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIF_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun showNotification(id: Int) {
        val intent = Intent(this, HowToPlayActivity::class.java)
            .putExtra("EXTRA", "Tell the user to go to HowtoPlay")
            .putExtra(NOTIF_ID, id)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val builder =
            NotificationCompat.Builder(this, NOTIF_ID)
        builder.setContentTitle("First Time? Checkout How to play first!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentText("Click here to go to How-2-Play!")
            .setSmallIcon(android.R.drawable.btn_dialog)
            .setContentIntent(pendingIntent) // the intent to launch the activity
            .setAutoCancel(true) // make sure the notification closes on click
        MainActivity.load = 1
        val notification = builder.build()
        NotificationManagerCompat.from(this).notify(id, notification)
    }

    private fun cancelNotification(id: Int) {
        NotificationManagerCompat
            .from(this)
            .cancel(id)
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(MainActivity.TAG, "Progress bar Start")

        Log.d(TAG, "onCreate()")
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    companion object {
        var THREAD_ID = 0
        val TAG = "TESTING"
        val NOTIF_ID = "edu.towson.cosc435"
    }

}
