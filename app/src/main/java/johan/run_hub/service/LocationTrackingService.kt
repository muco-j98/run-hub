package johan.run_hub.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import johan.run_hub.MainActivity
import johan.run_hub.R
import johan.run_hub.constantValues.ConstantValues.CHANNEL_ID
import johan.run_hub.constantValues.ConstantValues.START_TRACKING
import johan.run_hub.constantValues.ConstantValues.STOP_TRACKING
import johan.run_hub.utils.TrackUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

typealias Polyline = MutableList<LatLng>

class LocationTrackingService: LifecycleService() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var exerciseType: String

    companion object {
        val currentlyRunning = MutableLiveData<Boolean>()
        val pathTrack = MutableLiveData<Polyline>()
        val elapsedTime = MutableLiveData<Long>()
    }

    private fun setupFirstValues() {
        currentlyRunning.postValue(false)
        pathTrack.postValue(mutableListOf())
        elapsedTime.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        setupFirstValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        currentlyRunning.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == START_TRACKING) {
            exerciseType = intent.getStringExtra("EXERCISE_TYPE")!!
            startForegroundService()
            Timber.d("service started...")
        } else if (intent?.action == STOP_TRACKING) {
            stopTracking()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private var totalTime = 0L
    private var startedExerciseTime = 0L

    private fun getTime() {
        startedExerciseTime = System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            while (currentlyRunning.value!!) {
                totalTime = System.currentTimeMillis() - startedExerciseTime
                elapsedTime.postValue(totalTime)
                delay(50L)
            }

        }
    }

    private fun getFormattedExerciseString(exerciseType: String): String {
        return when (exerciseType) {
            "RUN_EXERCISE" -> "running"
            "BIKE_EXERCISE" -> "biking"
            else -> ""
        }
    }

    private fun getExerciseIcon(exerciseType: String): Int {
        return when (exerciseType) {
            "RUN_EXERCISE" -> R.drawable.ic_run
            "BIKE_EXERCISE" -> R.drawable.ic_bicycle
            else -> 0
        }
    }

    private fun stopTracking() {
        currentlyRunning.postValue(false)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(currentlyRunning: Boolean) {
        if(currentlyRunning && TrackUtil.hasLocationPermissions(this)) {
            Timber.d("went into update location tracking")
            fusedLocationProviderClient.requestLocationUpdates(
                setupLocationRequest(),
                locationCallback,
                Looper.getMainLooper()
            )
            }
            else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun setupLocationRequest(): LocationRequest {
        var locationRequest = LocationRequest()
        locationRequest.interval = 5000L
        locationRequest.fastestInterval = 2000L
        locationRequest.priority = PRIORITY_HIGH_ACCURACY
        return locationRequest
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(currentlyRunning.value!!) {
                result?.locations?.let { locations ->
                    for(location in locations) {
                        addRoutePositions(location)
                    }
                }
            }
        }
    }

    private fun addRoutePositions(location: Location?) {
        if (location != null) {
            val geoPoint = LatLng(location.latitude, location.longitude)
            pathTrack.value?.add(geoPoint)
            pathTrack.postValue(pathTrack.value)
        }
    }

    private fun startForegroundService() {
        currentlyRunning.postValue(true)
        getTime()

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel("my_service_id", "channel_name")
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
            .setContentText("A ${getFormattedExerciseString(exerciseType)} exercise is currently in process")
            .setSmallIcon(getExerciseIcon(exerciseType))
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