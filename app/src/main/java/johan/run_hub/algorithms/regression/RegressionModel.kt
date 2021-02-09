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

    private val yIntercept = yMean - slope * xMean

    //merr x dhe y dhe calc square error
}