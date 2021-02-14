package johan.run_hub.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import johan.run_hub.repositories.MainRepository
import javax.inject.Inject

@HiltViewModel
class AlgorithmViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    fun getJoinedCalories() = mainRepository.getJoinedCalories()
}