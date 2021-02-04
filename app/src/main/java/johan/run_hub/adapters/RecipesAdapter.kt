package johan.run_hub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import johan.run_hub.R
import johan.run_hub.network.models.Hit
import johan.run_hub.network.models.Recipe
import kotlinx.android.synthetic.main.recipe_item_layout.view.*
import kotlin.math.roundToInt

class RecipesAdapter: ListAdapter<Hit, RecipesAdapter.RecipeViewHolder>(RecipeDiffCallBack()) {

    class RecipeViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val recipeImage: ImageView = itemView.findViewById(R.id.imV_recipeIcon)
        private val label: TextView = itemView.findViewById(R.id.recipe_label)
        private val calories: TextView = itemView.findViewById(R.id.tv_calories)

        fun set(hit: Hit, holder: RecipeViewHolder) {
            val labelText = hit.recipe.label
            label.text = labelText

            Glide.with(holder.itemView).load(hit.recipe.image).into(recipeImage)

            val caloriesText = "${hit.recipe.calories.roundToInt()} kcal"
            calories.text = caloriesText
        }

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.recipe_item_layout, parent, false)
                return RecipeViewHolder(view)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recip = getItem(position)
        holder.set(recip, holder)
    }
}

class RecipeDiffCallBack: DiffUtil.ItemCallback<Hit>() {
    override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
        return oldItem.recipe.url == newItem.recipe.url
    }

    override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
        return oldItem.recipe == newItem.recipe
    }

}