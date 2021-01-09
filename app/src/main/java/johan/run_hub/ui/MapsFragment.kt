package johan.run_hub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.service.LocationTrackingService
import johan.run_hub.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_maps.*

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    private val viewModel: MainViewModel by viewModels()

    private var googleMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        startExercise_btn.setOnClickListener {
            sendIntentToService("test")
        }

        mapView.getMapAsync{
            googleMap = it
        }
    }

    private fun sendIntentToService(command: String) =
        run {
            val serviceIntent = Intent(requireContext(), LocationTrackingService::class.java)
            serviceIntent.action = command
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