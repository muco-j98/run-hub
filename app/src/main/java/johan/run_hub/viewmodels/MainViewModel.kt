package johan.run_hub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import johan.run_hub.constantValues.CategoryType
import johan.run_hub.constantValues.SortType
import johan.run_hub.db.entities.Exercise
import johan.run_hub.network.models.FoodResponse
import johan.run_hub.network.models.Recipe
import johan.run_hub.network.util.Resource
import johan.run_hub.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    fun insertExercise(exercise: Exercise) = viewModelScope.launch {
        mainRepository.insertExercise(exercise)
    }

    fun insertRecipe(recipe: Recipe) = viewModelScope.launch {
        mainRepository.insertRecipe(recipe)
    }

    val recipes: MutableLiveData<Resource<FoodResponse>> = MutableLiveData()
    var from = 0
    var to = 10

    fun getRecipes(ingredient: String) = viewModelScope.launch {
        recipes.postValue(Resource.Loading())
        mainRepository.searchRecipes(ingredient, from, to).let { response1 ->
            if (response1.isSuccessful) {
                response1.body()?.let {
                    recipes.postValue(Resource.Success(it))
                }
            } else {
                    recipes.postValue(Resource.Error(response1.message()))
            }
        }
    }

    private val getAllExercisesByDate = mainRepository.getAllExercisesByDate()
    private val getAllExercisesBySpeed = mainRepository.getAllExercisesBySpeed()
    private val getAllExercisesByCalories = mainRepository.getAllExercisesByCalories()
    private val getAllBikingBySpeed = mainRepository.getAllBikingBySpeed()
    private val getAllBikingByCalories = mainRepository.getAllBikingByCalories()
    private val getAllBikingByDate = mainRepository.getAllBikingByDate()
    private val getAllRunsByDate = mainRepository.getAllRunsByDate()
    private val getAllRunsBySpeed = mainRepository.getAllRunsBySpeed()
    private val getAllRunsByCalories = mainRepository.getAllRunsByCalories()


    var exercises = MediatorLiveData<List<Exercise>>()

    var categoryType = CategoryType.ALL
    var sortType = SortType.DATE

    var joinedSorting = "$categoryType $sortType"

    init {
        exercises.addSource(getAllExercisesByDate) { result ->
            if (joinedSorting == "ALL DATE") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllExercisesBySpeed) { result ->
            if (joinedSorting == "ALL AVG_SPEED") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllExercisesByCalories) { result ->
            if (joinedSorting == "ALL CALORIES") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllBikingByDate) { result ->
            if (joinedSorting == "BIKING DATE") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllBikingBySpeed) { result ->
            if (joinedSorting == "BIKING AVG_SPEED") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllBikingByCalories) { result ->
            if (joinedSorting == "BIKING CALORIES") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllRunsByDate) { result ->
            if (joinedSorting == "RUNS DATE") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllRunsBySpeed) { result ->
            if (joinedSorting == "RUNS AVG_SPEED") {
                result?.let { exercises.value = it }
            }
        }
        exercises.addSource(getAllRunsByCalories) { result ->
            if (joinedSorting == "RUNS CALORIES") {
                result?.let { exercises.value = it }
            }
        }
    }

    fun sortExercises(categoryType: CategoryType, sortType: SortType) {
        if (categoryType == CategoryType.ALL && sortType == SortType.DATE) {
            getAllExercisesByDate.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.ALL && sortType == SortType.AVG_SPEED) {
            getAllExercisesBySpeed.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.ALL && sortType == SortType.CALORIES) {
            getAllExercisesByCalories.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.BIKING && sortType == SortType.DATE) {
            getAllBikingByDate.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.BIKING && sortType == SortType.AVG_SPEED) {
            getAllBikingBySpeed.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.BIKING && sortType == SortType.CALORIES) {
            getAllBikingByCalories.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.RUNS && sortType == SortType.DATE) {
            getAllRunsByDate.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.RUNS && sortType == SortType.AVG_SPEED) {
            getAllRunsBySpeed.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        } else if (categoryType == CategoryType.RUNS && sortType == SortType.CALORIES) {
            getAllRunsByCalories.value?.let { exercises.value = it }
            this.categoryType = categoryType
            this.sortType = sortType
        }
    }

}