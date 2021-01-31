package johan.run_hub.network.api

import johan.run_hub.network.models.FoodResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: FoodApi
): ApiHelper {
    override suspend fun searchRecipes(
        ingredient: String,
        from: Int,
        to: Int
    ): Response<FoodResponse> = apiService.searchRecipes(ingredient, from = from, to = to)

}