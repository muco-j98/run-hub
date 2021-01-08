package johan.run_hub.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import johan.run_hub.R
import kotlinx.android.synthetic.main.fragment_initial.*
import java.lang.Double.parseDouble
import java.lang.NumberFormatException


class InitialFragment : Fragment(R.layout.fragment_initial) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continue_btn.setOnClickListener {
            val name = editName.text.trim().toString()
            val weight = editWeight.text.toString()

            if (validateEntries(name, weight)) {
                findNavController().navigate(R.id.action_initialFragment_to_exercisesFragment)
            } else {
                Toast.makeText(activity, "Please enter valid values", Toast.LENGTH_SHORT).show()
                editName.text.clear()
                editWeight.text.clear()
            }
        }

    }

    private fun validateEntries(name: String, weight: String): Boolean {
        var nameResult = false
        var weightResult = false
        var weightVal = 0.0

        try {
            weightVal = parseDouble(weight)
        } catch (e: NumberFormatException) {
            Log.e("weightError", e.toString())
        }

        val nameRegex = """^[A-z]+$""".toRegex()
        if (nameRegex.matches(name)) {
            nameResult = true
        }

        if (weightVal in 30.0..250.0) {
            weightResult = true
        }

        return nameResult && weightResult
    }

}