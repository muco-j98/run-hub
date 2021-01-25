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

    @Query("SELECT * FROM exercise_table ORDER BY averageSpeed DESC")
    fun getAllExercisesBySpeed(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table ORDER BY caloriesBurned DESC")
    fun getAllExercisesByCalories(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseType = 'BIKE_EXERCISE' ORDER BY averageSpeed DESC")
    fun getAllBikingBySpeed(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseType = 'BIKE_EXERCISE' ORDER BY caloriesBurned DESC")
    fun getAllBikingByCalories(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseType = 'BIKE_EXERCISE' ORDER BY exerciseDate DESC")
    fun getAllBikingByDate(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseType = 'RUN_EXERCISE' ORDER BY exerciseDate DESC")
    fun getAllRunsByDate(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseType = 'RUN_EXERCISE' ORDER BY averageSpeed DESC")
    fun getAllRunsBySpeed(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseType = 'RUN_EXERCISE' ORDER BY caloriesBurned DESC")
    fun getAllRunsByCalories(): LiveData<List<Exercise>>

}