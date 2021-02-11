package johan.run_hub.algorithms.regression

import johan.run_hub.utils.JoinedCalories
import kotlin.math.pow

class RegressionModel(var joinedCalories: List<JoinedCalories>) {

    private fun getXValues(): MutableList<Double> {
        val xCor = mutableListOf<Double>()
        for (i in 0..joinedCalories.size) {
            xCor.add(joinedCalories[i].caloriesBurned.toDouble())
        }
        return xCor
    }

    private fun getYValues(): MutableList<Double> {
        val xCor = mutableListOf<Double>()
        for (i in 0..joinedCalories.size) {
            xCor.add(joinedCalories[i].caloriesConsumed)
        }
        return xCor
    }

    private var xCoordinates = getXValues()
    private var yCoordinates = getYValues()

    private val xMean = xCoordinates.average()
    private val yMean = yCoordinates.average()

    private fun getSlope(): Double {
        var num = 0.0
        var denum = 0.0
        for (i in 0..xCoordinates.size) {
            num += (xCoordinates[i] - xMean) * (yCoordinates[i] - yMean)
            denum += (xCoordinates[i] - xMean).pow(2)
        }
        return num/denum
    }

    private val slope = getSlope()

    private val yIntercept = yMean - (slope * xMean)

    val smallestRegressionXValue = (yCoordinates.minOrNull()!!.minus(yIntercept)).div(slope)
    val smallestRegressionYValue = slope.times(xCoordinates.minOrNull()!!).plus(yIntercept)
    val largestRegressionXValue = (yCoordinates.maxOrNull()!!.minus(yIntercept)).div(slope)
    val largestRegressionYValue = slope.times(xCoordinates.maxOrNull()!!).plus(yIntercept)

    //calculate sum of square errors
    fun getFitOfTheModel(): Double {
        //sum of squares error
        var sse = 0.0
        //sum of squares residuals
        var ssr = 0.0
        //sum of squares total
        var sst = 0.0
        var mean = yCoordinates.average()
        var predictedValue = 0.0

        for (i in 0..xCoordinates.size) {
            ssr += (yCoordinates[i] - mean).pow(2)
            predictedValue = getPredictedValue(xCoordinates[i])
            sse += (yCoordinates[i] - predictedValue).pow(2)
        }

        //fit of the model | the higher the better
        return ssr/sst
    }

    private fun getPredictedValue(xValue: Double) = yIntercept + slope * xValue

}