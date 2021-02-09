package johan.run_hub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import johan.run_hub.repositories.MainRepository

class AlgorithmViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    fun getJoinedCalories() = mainRepository.getJoinedCalories()
}