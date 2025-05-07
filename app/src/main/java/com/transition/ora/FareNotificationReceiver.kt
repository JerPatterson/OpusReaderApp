package com.transition.ora

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import java.util.UUID

private const val ARG_CARD = "card"
private const val ARG_TITLE = "title"
private const val ARG_MESSAGE = "message"
private const val CHANNEL_ID = "fare_validity"


class FareNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.validity_notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.validity_notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val card = Gson().fromJson(intent.getStringExtra(ARG_CARD), Card::class.java)
        val title = intent.getStringExtra(ARG_TITLE)
        val message = intent.getStringExtra(ARG_MESSAGE)

        val notificationClickIntent = Intent(context, CardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        notificationClickIntent.putExtra(ARG_CARD, intent.getStringExtra(ARG_CARD))

        val notificationPendingIntent = PendingIntent.getActivity(
            context,
            card.id.hashCode(),
            notificationClickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(R.drawable.opusreader)
            .setContentIntent(notificationPendingIntent)
            .build()

        notificationManager.notify(
            UUID.nameUUIDFromBytes("${card.id}:fare".toByteArray()).hashCode(),
            notification
        )

        NotificationScheduler().scheduleNotification(card, context)
    }
}