package johan.run_hub.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class Exercise (
    var distanceCovered: Int = 0,
    var elapsedTimeInMillis: Long = 0L,
    var exerciseDate: Int = 0,
    var averageSpeed: Float = 0f,
    var caloriesBurned: Int = 0,
    var exerciseType: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var exerciseId: Int? = null
}