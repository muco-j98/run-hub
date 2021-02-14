package johan.run_hub.algorithms.regression

import johan.run_hub.utils.JoinedCalories
import kotlin.math.pow

class RegressionModel(var joinedCalories: List<JoinedCalories>) {

    private val xCoordinates = joinedCalories.map { i -> i.caloriesConsumed }
    private val yCoordinates = joinedCalories.map { i -> i.caloriesBurned.toDouble() }

    private val xMean = xCoordinates.average()
    private val yMean = yCoordinates.average()

    private fun getSlope(): Double {
        var num = 0.0
        var denum = 0.0
        for (i in xCoordinates.indices) {
            num += (xCoordinates[i] - xMean) * (yCoordinates[i] - yMean)
            denum += (xCoordinates[i] - xMean).pow(2)
        }
        return num/denum
    }

    private val slope = getSlope()

    private val yIntercept = yMean - slope * xMean

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
            predictedValue = getPredictedYValue(xCoordinates[i])
            sse += (yCoordinates[i] - predictedValue).pow(2)
        }

        //fit of the model | the higher the better
        return ssr/sst
    }

    fun getPredictedYValue(xValue: Double) = slope * xValue + yIntercept
}