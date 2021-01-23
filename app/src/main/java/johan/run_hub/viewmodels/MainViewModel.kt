package johan.run_hub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import johan.run_hub.db.entities.Exercise
import johan.run_hub.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    fun insertExercise(exercise: Exercise) = viewModelScope.launch {
        mainRepository.insertExercise(exercise)
    }

    fun getAllExercises() = mainRepository.getAllExercisesByDate()
}