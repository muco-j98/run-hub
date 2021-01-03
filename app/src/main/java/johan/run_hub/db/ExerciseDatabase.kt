package johan.run_hub.db

import androidx.room.Database
import androidx.room.RoomDatabase
import johan.run_hub.db.entities.Exercise

@Database(
    entities = [Exercise::class],
    version = 1
)
abstract class ExerciseDatabase :RoomDatabase() {

    abstract fun getExerciseDao(): ExerciseDao
}