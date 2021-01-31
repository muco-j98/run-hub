package johan.run_hub.network.api

import johan.run_hub.constantValues.FoodApiKeys.APP_ID
import johan.run_hub.constantValues.FoodApiKeys.APP_KEY
import johan.run_hub.network.models.FoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {

    @GET("search")
    suspend fun searchRecipes(
        @Query("q")
        ingredient: String,
        @Query("app_id")
        appId: String = APP_ID,
        @Query("app_key")
        appKey: String = APP_KEY,
        @Query("from")
        from: Int,
        @Query("to")
        to: Int
    ): Response<FoodResponse>
}