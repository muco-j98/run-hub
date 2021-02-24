package johan.run_hub.algorithms.regression

import johan.run_hub.utils.JoinedCalories

class GradientRegression(var joinedCalories: List<JoinedCalories>) {

    private val xCoordinates = joinedCalories.map { i -> i.caloriesConsumed }
    private val yCoordinates = joinedCalories.map { i -> i.caloriesBurned.toDouble() }

    //slope
    private var m: Double = 0.0
    //y-intercept
    private var b: Double = 0.0
    private var learningRate = 0.05

    fun getMBForLine(xyCoordinates: List<JoinedCalories>): Pair<Double, Double> {
        for (i in xyCoordinates.indices) {
            val x = xyCoordinates[i].caloriesConsumed
            val y = xyCoordinates[i].caloriesBurned

            val estimation = m * x + b
            val error = y - estimation

            m += error * x * learningRate
            b += error * learningRate
        }
        return Pair(m,b)
    }

}