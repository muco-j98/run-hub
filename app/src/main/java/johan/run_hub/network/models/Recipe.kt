package johan.run_hub.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class Recipe(
    val calories: Double,
    val image: String,
    val label: String,
    val uri: String,
    val url: String,
) {
    @PrimaryKey(autoGenerate = true)
    var recipeId: Int? = null
}