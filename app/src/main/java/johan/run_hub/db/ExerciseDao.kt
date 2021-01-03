package johan.run_hub.db

import androidx.lifecycle.LiveData
import androidx.room.*
import johan.run_hub.db.entities.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise_table ORDER BY exerciseDate DESC")
    fun getAllExercisesByDate(): LiveData<List<Exercise>>
}