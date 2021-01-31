package johan.run_hub.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.adapters.RecipesAdapter
import johan.run_hub.network.util.Resource
import johan.run_hub.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_recipes.*
import timber.log.Timber

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipeResponse ->
            when (recipeResponse) {
                is Resource.Success -> {
                    recipeResponse.data.let { response ->
                        response?.hits.let { hits ->
                            hits?.let {
                                recipesAdapter.submitList(it)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    recipeResponse.message?.let {
                        Timber.e(it)
                    }
                }
                is Resource.Loading -> {
                    Timber.i("Loading")
                }
            }
        })
    }

    private fun setupRecycler() {
        recipesAdapter = RecipesAdapter()
        rvRecipes.adapter = recipesAdapter
        rvRecipes.layoutManager = LinearLayoutManager(requireContext())
    }
}