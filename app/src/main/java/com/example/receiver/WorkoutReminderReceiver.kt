package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.config.AppConfig
import com.example.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WorkoutReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule notification on reboot based on stored user preferences
            CoroutineScope(Dispatchers.IO).launch {
                val prefsRepo = UserPreferencesRepository(context)
                val prefs = prefsRepo.userPreferencesFlow.first()
                if (prefs.isReminderEnabled) {
                    NotificationScheduler.scheduleDailyReminder(
                        context,
                        prefs.reminderHour,
                        prefs.reminderMinute
                    )
                }
            }
            return
        }

        // Show workout reminder notification
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "fitlife_daily_reminders"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Workout Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily notification encouraging your workout routine in FitLife Pro"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Time to Train! / ব্যায়ামের সময় হয়েছে!")
            .setContentText("Complete today's workout on FitLife Pro and maintain your health streak.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Keep your body moving! Even 10 minutes of daily exercise strengthens your heart, improves posture, and reduces stress.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1001, notification)
    }
}
