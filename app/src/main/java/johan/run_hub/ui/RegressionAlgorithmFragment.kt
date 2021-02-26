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
import johan.run_hub.algorithms.regression.GradientRegression
import johan.run_hub.algorithms.regression.RegressionModel
import johan.run_hub.utils.JoinedCalories
import johan.run_hub.viewmodels.AlgorithmViewModel
import kotlinx.android.synthetic.main.fragment_regression_algorithm.*

@AndroidEntryPoint
class RegressionAlgorithmFragment : Fragment(R.layout.fragment_regression_algorithm) {

    private val algorithmViewModel: AlgorithmViewModel by viewModels()
    lateinit var joinedCalories: List<Entry>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegressionChart()
        setupGradientRegressionChart()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        algorithmViewModel.getJoinedCalories().observe(viewLifecycleOwner, Observer {
            it?.let {
                joinedCalories = it.map { i -> Entry(i.caloriesConsumed.toFloat(), i.caloriesBurned.toFloat()) }
                val scatterDataSet = ScatterDataSet(joinedCalories, "Calories Consumed/Burned").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                val scatterData = ScatterData(scatterDataSet)

                val regressionModel = RegressionModel(it)
                val gradientRegression = GradientRegression(it)
                gradientRegression.getMBForLine()

                val modelFitText = regressionModel.getFitOfTheModel() * 100
                val fitText = String.format("%.1f", modelFitText) + "%"

                tvModelFit.text = fitText

                val smallestPoint = it.minByOrNull { x -> x.caloriesConsumed }
                val largestPoint = it.maxByOrNull { x -> x.caloriesConsumed }

                val smallestYValue = smallestPoint?.caloriesConsumed?.let { it1 ->
                    regressionModel.getPredictedYValue(it1)
                }
                val largestYValue = largestPoint?.caloriesConsumed?.let { it1 ->
                    regressionModel.getPredictedYValue(it1)
                }

                val regressionLinePoints = listOf(
                    smallestYValue?.let { it1 -> Entry(smallestPoint.caloriesConsumed.toFloat(),
                        it1.toFloat()) },
                    largestYValue?.let { it1 -> Entry(largestPoint.caloriesConsumed.toFloat(),
                        it1.toFloat()) }
                )

                val gradientLineDataSet = LineDataSet(getGradientRegressionPoints(
                    smallestPoint!!.caloriesConsumed, largestPoint!!.caloriesConsumed,
                    gradientRegression), "Gradient Regression Line")

                val gradientLineData = LineData(gradientLineDataSet)

                val gradientData = CombinedData()
                gradientData.setData(scatterData)
                gradientData.setData(gradientLineData)

                gradientChart.data = gradientData
                gradientChart.invalidate()

                val lineDataSet = LineDataSet(regressionLinePoints, "Best Fit Line")
                val lineData = LineData(lineDataSet)

                val data = CombinedData()
                data.setData(scatterData)
                data.setData(lineData)
                combinedChart.data = data
                combinedChart.invalidate()
            }
        })
    }

    private fun getGradientRegressionPoints(x1: Double, x2: Double,
                                            gradientRegression: GradientRegression): List<Entry> {
        val y1 = gradientRegression.getYValue(x1)
        val y2 = gradientRegression.getYValue(x2)

        val entry1 = Entry(x1.toFloat(), y1.toFloat())
        val entry2 = Entry(x2.toFloat(), y2.toFloat())

        return listOf(entry1, entry2)
    }

    private fun setupGradientRegressionChart() {
        gradientChart.apply {
            setTouchEnabled(true)
            isDragEnabled = true
        }
        gradientChart.description.apply {
            isEnabled = true
            text = "Calories Consumed"
            textSize = 13f
            textColor = Color.BLACK
        }
        gradientChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawAxisLine(true)
            setDrawGridLines(false)
        }
        gradientChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        gradientChart.axisRight.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
    }

    private fun setupRegressionChart() {
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