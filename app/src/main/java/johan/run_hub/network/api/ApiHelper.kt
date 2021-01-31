package johan.run_hub.network.api

import johan.run_hub.constantValues.FoodApiKeys
import johan.run_hub.network.models.FoodResponse
import retrofit2.Response
import retrofit2.http.Query

interface ApiHelper {
    suspend fun searchRecipes(ingredient: String,
                              from: Int,
                              to: Int): Response<FoodResponse>
}