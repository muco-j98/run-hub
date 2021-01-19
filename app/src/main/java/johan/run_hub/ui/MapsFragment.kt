package johan.run_hub.ui

import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.db.constantValues.ConstantValues.START_TRACKING
import johan.run_hub.db.constantValues.ConstantValues.STOP_TRACKING
import johan.run_hub.service.LocationTrackingService
import johan.run_hub.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_maps.*
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    private val viewModel: MainViewModel by viewModels()

    private var googleMap: GoogleMap? = null

    private lateinit var exerciseType: String

    private val args: MapsFragmentArgs by navArgs()

    private var currentlyTracking = false
    private var pathTrack = mutableListOf<LatLng>()

    @set:Inject
    var weight = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        exerciseType = args.exerciseType

        startExercise_btn.setOnClickListener {
            sendIntent()
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
    }

    private fun checkTrackingState(currentlyTracking: Boolean) {
        this.currentlyTracking = currentlyTracking
        if (currentlyTracking) {
            startExercise_btn.visibility = View.GONE

        } else {
            startExercise_btn.text = "Start"
        }
    }

    private fun sendIntent() {
        if (!currentlyTracking) {
            sendIntentToService(START_TRACKING)
        } else {
            sendIntentToService(STOP_TRACKING)
        }
    }

    private fun connectLastTwoPoints() {
        if (pathTrack.size >=2) {
            var lastPoint = pathTrack.last()
            var prelastPoint = pathTrack[pathTrack.lastIndex - 1]

            val polylineOptions = PolylineOptions()
                .add(prelastPoint)
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