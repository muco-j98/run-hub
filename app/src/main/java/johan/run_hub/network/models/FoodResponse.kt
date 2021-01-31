package johan.run_hub.network.models

data class FoodResponse(
    val from: Int,
    val hits: List<Hit>,
    val q: String,
    val to: Int
)