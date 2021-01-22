package johan.run_hub.ui

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.constantValues.ConstantValues.START_TRACKING
import johan.run_hub.constantValues.ConstantValues.STOP_TRACKING
import johan.run_hub.db.entities.Exercise
import johan.run_hub.service.LocationTrackingService
import johan.run_hub.service.Polyline
import johan.run_hub.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_maps.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    private val mainViewModel: MainViewModel by viewModels()

    private var googleMap: GoogleMap? = null

    private lateinit var exerciseType: String

    private val args: MapsFragmentArgs by navArgs()

    private var currentlyTracking = false
    private var pathTrack = mutableListOf<LatLng>()

    @set:Inject
    var weight = 0f

    private var curTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        exerciseType = args.exerciseType

        startExercise_btn.setOnClickListener {
            sendIntentToService(START_TRACKING)
        }
        stopExercise_btn.setOnClickListener {
            sendIntentToService(STOP_TRACKING)
            saveExercise()
            findNavController().navigate(R.id.action_mapsFragment_to_exercisesFragment)
        }

        mapView.getMapAsync{
            googleMap = it
        }

        linkWithObservers()
    }

    private fun linkWithObservers(){
        LocationTrackingService.currentlyRunning.observe(viewLifecycleOwner, Observer {
            checkTrackingState(it)
        })

        LocationTrackingService.pathTrack.observe(viewLifecycleOwner, Observer {
            pathTrack = it
            connectLastTwoPoints()
            zoomCameraToLastLocation()
        })

        LocationTrackingService.elapsedTime.observe(viewLifecycleOwner, Observer {
            curTime = it
            val timeText = setTimeText(curTime)
            millis_text.text = timeText
        })
    }

    private fun saveExercise() {
        val distance = calculateDistance(pathTrack).toInt()
        var averageSpeed = (distance / 1000f) / getTimeInHours(curTime)
        val roundedAverageSpeed = round(averageSpeed * 10) / 10f
        val exerciseDate = Date().time
        val caloriesBurned = ((distance / 1000f) * weight).toInt()
        val exercise = Exercise(distance, curTime, exerciseDate,
            roundedAverageSpeed, caloriesBurned, exerciseType)
        mainViewModel.insertExercise(exercise)

    }

    private fun getTimeInHours(millis: Long): Float {
        return (millis / 1000f / 60 / 60)
    }

    private fun calculateDistance(path: Polyline): Float {
        var distance = 0f
        for (i in 0..path.size-2) {
            val point1 = path[i]
            val point2 = path[i + 1]

            val output = FloatArray(1)
            Location.distanceBetween(
                point1.latitude,
                point1.longitude,
                point2.latitude,
                point2.longitude,
                output
            )
            distance = output[0]
        }

        return distance
    }

    private fun setTimeText(milliseconds: Long): String {
        var ms = milliseconds
        val hours = TimeUnit.MILLISECONDS.toHours(ms)
        ms -= TimeUnit.HOURS.toMillis((hours))
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        ms -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms)
        ms -= TimeUnit.SECONDS.toMillis(seconds)
        ms /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds:" +
                "${if(ms < 10) "0" else ""}$ms:"
    }

    private fun checkTrackingState(currentlyTracking: Boolean) {
        this.currentlyTracking = currentlyTracking
        if (currentlyTracking) {
            startExercise_btn.visibility = View.GONE
            stopExercise_btn.visibility = View.VISIBLE
        } else {
            startExercise_btn.visibility = View.VISIBLE
            stopExercise_btn.visibility = View.GONE
        }
    }

    private fun connectLastTwoPoints() {
        if (pathTrack.size >=2) {
            var lastPoint = pathTrack.last()
            var preLastPoint = pathTrack[pathTrack.lastIndex - 1]

            val polylineOptions = PolylineOptions()
                .add(preLastPoint)
                .add(lastPoint)
                .color(Color.MAGENTA)

            googleMap?.addPolyline(polylineOptions)
        }
    }

    private fun zoomCameraToLastLocation(){
        if (pathTrack.size >= 1) {
            var lastPoint = pathTrack.last()
            val updateFactory = CameraUpdateFactory.newLatLngZoom(lastPoint, 16f)
            googleMap?.animateCamera(updateFactory)
        }
    }

    private fun sendIntentToService(command: String) =
        run {
            val serviceIntent = Intent(requireContext(), LocationTrackingService::class.java)
            serviceIntent.action = command
            serviceIntent.putExtra("EXERCISE_TYPE", exerciseType)
            requireContext().startService(serviceIntent)
        }

    //managing the lifecycle of the mapView
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}