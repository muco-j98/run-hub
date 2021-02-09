package johan.run_hub.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.viewmodels.AlgorithmViewModel

@AndroidEntryPoint
class RegressionAlgorithmFragment : Fragment(R.layout.fragment_regression_algorithm) {

    private val algorithmViewModel: AlgorithmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}