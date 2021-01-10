package johan.run_hub.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import johan.run_hub.R
import johan.run_hub.db.constantValues.ConstantValues.BIKE_EXERCISE
import johan.run_hub.db.constantValues.ConstantValues.LOCATION_PERMISSIONS_CODE
import johan.run_hub.db.constantValues.ConstantValues.RUN_EXERCISE
import johan.run_hub.utils.TrackUtil
import kotlinx.android.synthetic.main.fragment_exercises.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ExercisesFragment : Fragment(R.layout.fragment_exercises), EasyPermissions.PermissionCallbacks  {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim) }

    private var clicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestLocationPermission()

        add_btn.setOnClickListener {
            onAddButtonClicked()
        }

        bike_btn.setOnClickListener {
            val action = ExercisesFragmentDirections.actionExercisesFragmentToMapsFragment(
                BIKE_EXERCISE)
            findNavController().navigate(action)

        }

        run_btn.setOnClickListener {
            val action = ExercisesFragmentDirections.actionExercisesFragmentToMapsFragment(
                RUN_EXERCISE)
            findNavController().navigate(action)
        }
    }

    private fun getSubQPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.locations_rationale),
            LOCATION_PERMISSIONS_CODE,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun getQPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.locations_rationale),
            LOCATION_PERMISSIONS_CODE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    private fun requestLocationPermission() {
        if (TrackUtil.hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            getSubQPermission()
        } else {
            getQPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //Handling animations
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked:Boolean) {
        if (!clicked) {
            run_btn.visibility = View.VISIBLE
            bike_btn.visibility = View.VISIBLE
        } else{
            run_btn.visibility = View.INVISIBLE
            bike_btn.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked:Boolean) {
        if (!clicked){
            run_btn.startAnimation(fromBottom)
            bike_btn.startAnimation(fromBottom)
            add_btn.startAnimation(rotateOpen)
        } else {
            run_btn.startAnimation(toBottom)
            bike_btn.startAnimation(toBottom)
            add_btn.startAnimation(rotateClose)
        }
    }

}