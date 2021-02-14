package johan.run_hub.utils

import androidx.room.ColumnInfo

data class JoinedCalories(
    @ColumnInfo(name = "calories")
    val caloriesConsumed: Double,
    @ColumnInfo(name = "caloriesBurned")
    val caloriesBurned: Int) {
}