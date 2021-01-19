package johan.run_hub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.db.constantValues.ConstantValues.START_TRACKING
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

    @set:Inject
    var weight = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        exerciseType = args.exerciseType

        startExercise_btn.setOnClickListener {
            sendIntentToService(START_TRACKING)
        }

        mapView.getMapAsync{
            googleMap = it
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