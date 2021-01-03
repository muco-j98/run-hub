package johan.run_hub.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.viewmodels.MainViewModel

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    private val viewModel: MainViewModel by viewModels()
}