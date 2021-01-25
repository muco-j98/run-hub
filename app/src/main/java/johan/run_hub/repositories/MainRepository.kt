package johan.run_hub.repositories

import johan.run_hub.db.ExerciseDao
import johan.run_hub.db.entities.Exercise
import javax.inject.Inject

class MainRepository @Inject constructor(
    val exerciseDao: ExerciseDao
){
    suspend fun insertExercise(exercise: Exercise) = exerciseDao.insertExercise(exercise)

    fun getAllExercisesByDate() = exerciseDao.getAllExercisesByDate()

    fun getAllExercisesBySpeed() = exerciseDao.getAllExercisesBySpeed()

    fun getAllExercisesByCalories() = exerciseDao.getAllExercisesByCalories()

    fun getAllBikingBySpeed() = exerciseDao.getAllBikingBySpeed()

    fun getAllBikingByCalories() = exerciseDao.getAllBikingByCalories()

    fun getAllBikingByDate() = exerciseDao.getAllBikingByDate()

    fun getAllRunsByDate() = exerciseDao.getAllRunsByDate()

    fun getAllRunsBySpeed() = exerciseDao.getAllRunsBySpeed()

    fun getAllRunsByCalories() = exerciseDao.getAllRunsByCalories()
}