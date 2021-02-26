package johan.run_hub.algorithms.regression

import johan.run_hub.utils.JoinedCalories

class GradientRegression(joinedCalories: List<JoinedCalories>) {

    private val xCoordinates = joinedCalories.map { i -> i.caloriesConsumed }
    private val yCoordinates = joinedCalories.map { i -> i.caloriesBurned.toDouble() }

    //slope
    private var m: Double = 0.0

    //y-intercept
    private var b: Double = 0.0
    private var learningRate = 0.0000001

    fun getMBForLine(): Pair<Double, Double> {
        for (i in xCoordinates.indices) {
            val x = xCoordinates[i]
            val y = yCoordinates[i]

            val estimation = m * x + b
            val error = y - estimation

            m += (error * x) * learningRate
            b += error * learningRate
        }
        return Pair(m, b)
    }

    private val predictedParameters = getMBForLine()

    fun getYValue(x: Double) = predictedParameters.first * x + predictedParameters.second
}