package johan.run_hub.repositories

import johan.run_hub.db.ExerciseDao
import johan.run_hub.db.entities.Exercise
import javax.inject.Inject

class MainRepository @Inject constructor(
    val exerciseDao: ExerciseDao
){
    suspend fun insertExercise(exercise: Exercise) = exerciseDao.insertExercise(exercise)

    suspend fun deleteExercise(exercise: Exercise) = exerciseDao.deleteExercise(exercise)

    fun getAllExercisesByDate() = exerciseDao.getAllExercisesByDate()
}