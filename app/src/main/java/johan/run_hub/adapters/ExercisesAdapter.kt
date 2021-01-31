package johan.run_hub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import johan.run_hub.R
import johan.run_hub.constantValues.ConstantValues.BIKE_EXERCISE
import johan.run_hub.constantValues.ConstantValues.RUN_EXERCISE
import johan.run_hub.db.entities.Exercise
import java.text.SimpleDateFormat
import java.util.*

class ExercisesAdapter: ListAdapter<Exercise, ExercisesAdapter.ViewHolder>(ExerciseDiffCallback()) {

    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        private val date: TextView = itemView.findViewById(R.id.date_text)
        private val distance: TextView = itemView.findViewById(R.id.distance_text)
        private val avgSpeed: TextView = itemView.findViewById(R.id.avg_speed_text)
        private val caloriesBurned: TextView = itemView.findViewById(R.id.calBurned_text)
        private val exerciseIcon: ImageView = itemView.findViewById(R.id.imV_ExerciseIc)

        fun set(exercise: Exercise) {
            val format = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            date.text = format.format(exercise.exerciseDate)

            val distanceText = "${exercise.distanceCovered}km"
            distance.text = distanceText

            val avgSpeedText = "${exercise.averageSpeed}km/h"
            avgSpeed.text = avgSpeedText

            val calBurnedText = "${exercise.caloriesBurned}kcal"
            caloriesBurned.text = calBurnedText

            exerciseIcon.setImageResource(when (exercise.exerciseType) {
                BIKE_EXERCISE -> R.drawable.ic_bicycle_black
                RUN_EXERCISE -> R.drawable.ic_run_black
                else -> R.drawable.ic_exercise
            })
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater
                    .inflate(R.layout.exercise_item_layout, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.set(item)
    }
}

class ExerciseDiffCallback: DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.exerciseId == newItem.exerciseId
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }

}