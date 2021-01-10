package johan.run_hub.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import johan.run_hub.MainActivity
import johan.run_hub.R
import johan.run_hub.db.constantValues.ConstantValues.CHANNEL_ID
import johan.run_hub.db.constantValues.ConstantValues.PAUSE_TRACKING
import johan.run_hub.db.constantValues.ConstantValues.START_OR_RESUME_TRACKING
import johan.run_hub.db.constantValues.ConstantValues.STOP_TRACKING

class LocationTrackingService: LifecycleService() {

    var isFirstTime = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == START_OR_RESUME_TRACKING) {
            if (isFirstTime) {
                startForegroundService()
                isFirstTime = false
            }
        } else if (intent?.action == STOP_TRACKING) {
            Log.DEBUG
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel("my_service", "My Background Service")
        } else {
            CHANNEL_ID
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
            0,
            notificationIntent,
            FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("RunHub")
            .setContentText("An exercise is currently in process")
            .setSmallIcon(R.drawable.ic_bicycle)
            .setContentIntent(pendingIntent)

        startForeground(1, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String, channelName: String): String {

        val channel = NotificationChannel(channelId,
                                       channelName,
                                        NotificationManager.IMPORTANCE_LOW)

        val service = getSystemService(Context  .NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }


}