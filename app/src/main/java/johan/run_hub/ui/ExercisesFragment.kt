package johan.run_hub.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import johan.run_hub.R
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_initial.*

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim) }

    private var clicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_btn.setOnClickListener {
            onAddButtonClicked()
        }
    }

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