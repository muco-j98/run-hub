package johan.run_hub.db

import androidx.room.Database
import androidx.room.RoomDatabase
import johan.run_hub.db.entities.Exercise
import johan.run_hub.network.models.Recipe
import johan.run_hub.utils.JoinedCalories

@Database(
    entities = [Exercise::class, Recipe::class],
    version = 7,
    exportSchema = false
)
abstract class ExerciseDatabase :RoomDatabase() {
    abstract fun getExerciseDao(): ExerciseDao
}