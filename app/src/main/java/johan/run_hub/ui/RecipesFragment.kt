package johan.run_hub.ui

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import java.util.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.adapters.RecipesAdapter
import johan.run_hub.network.models.Hit
import johan.run_hub.network.util.Resource
import johan.run_hub.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_recipes.*
import timber.log.Timber
import java.time.Duration

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
                s.let {
                    val inputTxt = s.toString().trim()
                    if (inputTxt.isNotEmpty()) {
                        timer = Timer()
                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                viewModel.getRecipes(s.toString())
                            }
                        }, stopTime)
                    }
                }
            }

        })

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipeResponse ->
            when (recipeResponse) {
                is Resource.Success -> {
                    recipeResponse.data.let { response ->
                        response?.hits.let { hits ->
                            hits?.let {
                                if (it.isEmpty()) {
                                    setupRecycler()
                                    Toast.makeText(requireContext(),
                                        "No recipes found.",
                                        Toast.LENGTH_SHORT).show()
                                } else
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
        recipesAdapter = RecipesAdapter() {
            manageOnClickEvent(it)
        }
        rvRecipes.adapter = recipesAdapter
        rvRecipes.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun manageOnClickEvent(hit: Hit) {
        val recipeImage = Glide.with(this)
            .asDrawable()
            .load(hit.recipe.image)

        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to add this recipe?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { dialog, _ ->
                Toast.makeText(requireContext(), "Yes received", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()

    }
}