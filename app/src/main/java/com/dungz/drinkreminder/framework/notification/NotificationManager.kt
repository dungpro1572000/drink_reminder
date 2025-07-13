package com.dungz.drinkreminder.framework.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dungz.drinkreminder.framework.receiver.NotificationReceiver
import com.dungz.drinkreminder.utilities.AppConstant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationManager @Inject constructor(@ApplicationContext private val context: Context) {

    init {
        createNotificationChannel()
    }

    fun createNotificationChannel() {
        val name = "Reminder Channel"
        val descriptionText = "Channel for reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("reminder_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, notificationData: NotificationData) {
        val checkThisAction = Intent(context, NotificationReceiver::class.java).apply {
            action = AppConstant.NOTIFICATION_ACTION_RECEIVER
            `package` = AppConstant.packageName
            putExtra(AppConstant.NOTIFICATION_BUNDLE_ID, notificationData.channelId)
        }
        val checkThisActionIntent = PendingIntent.getBroadcast(
            context,
            notificationData.channelId,
            checkThisAction,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(notificationData.notificationIcon) // đảm bảo bạn có icon này
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.message)
            .addAction(notificationData.notificationIcon, "Check this", checkThisActionIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationData.channelId, notificationBuilder.build())
    }
}