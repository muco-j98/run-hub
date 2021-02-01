package johan.run_hub.ui

import android.text.Editable
import android.text.TextWatcher
import java.util.*
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

    var timer = Timer()
    var stopTime: Long = 2000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        recipeField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer.cancel()
                timer.purge()
            }

            override fun afterTextChanged(s: Editable?) {
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        viewModel.getRecipes(s.toString())
                    }
                }, stopTime)
            }

        })

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