package johan.run_hub.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import dagger.hilt.android.AndroidEntryPoint
import johan.run_hub.R
import johan.run_hub.algorithms.regression.RegressionModel
import johan.run_hub.viewmodels.AlgorithmViewModel
import kotlinx.android.synthetic.main.fragment_regression_algorithm.*

@AndroidEntryPoint
class RegressionAlgorithmFragment : Fragment(R.layout.fragment_regression_algorithm) {

    private val algorithmViewModel: AlgorithmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScatterChart()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        algorithmViewModel.getJoinedCalories().observe(viewLifecycleOwner, Observer {
            it?.let {
                val joinedCalories = it.map { i -> Entry(i.caloriesConsumed.toFloat(), i.caloriesBurned.toFloat()) }
                val scatterDataSet = ScatterDataSet(joinedCalories, "Calories Consumed/Burned").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                val scatterData = ScatterData(scatterDataSet)

                val regressionModel = RegressionModel(it)
                val smallestPoint = it.minByOrNull { x -> x.caloriesConsumed }
                val largestPoint = it.maxByOrNull { x -> x.caloriesConsumed }

                val smallestYValue = smallestPoint?.caloriesConsumed?.let { it1 ->
                    regressionModel.getPredictedYValue(
                        it1.toDouble())
                }
                val largestYValue = largestPoint?.caloriesConsumed?.let { it1 ->
                    regressionModel.getPredictedYValue(
                        it1.toDouble())
                }

                val linePoints = listOf(
//                    largestXValue?.let { it1 -> smallestXValue?.let { it2 -> Entry(it2.toFloat(), it1.toFloat()) } },
//                    largestYValue?.let { it1 -> smallestYValue?.let { it2 -> Entry(it2.toFloat(), it1.toFloat()) } }
                    smallestYValue?.let { it1 -> Entry(smallestPoint.caloriesConsumed.toFloat(), it1.toFloat()) },
                    largestYValue?.let { it1 -> Entry(largestPoint.caloriesConsumed.toFloat(), it1.toFloat()) }
                )

                val lineDataSet = LineDataSet(linePoints, "Best Fit Line")
                val lineData = LineData(lineDataSet)

                val data = CombinedData()
                data.setData(scatterData)
                data.setData(lineData)
                combinedChart.data = data
                combinedChart.invalidate()
            }
        })
    }

    private fun setupScatterChart() {
        combinedChart.apply {
            setTouchEnabled(false)
        }
        combinedChart.description.apply {
            isEnabled = true
            text = "Calories Consumed"
            textSize = 13f
            textColor = Color.WHITE
        }
        combinedChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawAxisLine(true)
            setDrawGridLines(false)
        }
        combinedChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        combinedChart.axisRight.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
    }
}