package johan.run_hub.utils

import johan.run_hub.constantValues.ConstantValues.RUN_EXERCISE
import johan.run_hub.db.entities.Exercise
import johan.run_hub.network.models.Recipe

object PrePopulateUtil {

    fun getExercises(): MutableList<Exercise> {
        val exec1 = Exercise(10,10,1612888782000,
                        2f, 700.0, RUN_EXERCISE)
        val exec2 = Exercise(10,10,1612975182000,
                        2f, 750.0, RUN_EXERCISE)
        val exec3 = Exercise(10,10,1613061582000,
                        2f, 800.0, RUN_EXERCISE)
        val exec4 = Exercise(10,10,1613147982000,
                        2f, 720.0, RUN_EXERCISE)
        val exec5 = Exercise(10,10,1613234382000,
                        2f, 790.0, RUN_EXERCISE)
        val exec6 = Exercise(10,10,1613320782000,
                        2f, 820.0, RUN_EXERCISE)
        val exec7 = Exercise(10,10,1613407182000,
                        2f, 870.0, RUN_EXERCISE)

        return mutableListOf(exec1, exec2, exec3, exec4, exec5, exec6, exec7)
    }

    fun getRecipes(): MutableList<Recipe> {
        val recipe1 = Recipe(2000.0,
            "https://www.edamam.com/web-img/e42/e42f9119813e890af34c259785ae1cfb.jpg",
            "testLabel",
            "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
            "http://www.seriouseats.com/recipes/2011/12/chicken-vesuvio-recipe.html",
            1612888782000)
        val recipe2 = Recipe(2200.0,
            "https://www.edamam.com/web-img/e42/e42f9119813e890af34c259785ae1cfb.jpg",
            "testLabel",
            "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
            "http://www.seriouseats.com/recipes/2011/12/chicken-vesuvio-recipe.html",
            1612975182000)
        val recipe3 = Recipe(2300.0,
            "https://www.edamam.com/web-img/e42/e42f9119813e890af34c259785ae1cfb.jpg",
            "testLabel",
            "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
            "http://www.seriouseats.com/recipes/2011/12/chicken-vesuvio-recipe.html",
            1613061582000)
        val recipe4 = Recipe(2150.0,
            "https://www.edamam.com/web-img/e42/e42f9119813e890af34c259785ae1cfb.jpg",
            "testLabel",
            "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
            "http://www.seriouseats.com/recipes/2011/12/chicken-vesuvio-recipe.html",
            1613147982000)
        val recipe5 = Recipe(2500.0,
            "https://www.edamam.com/web-img/e42/e42f9119813e890af34c259785ae1cfb.jpg",
            "testLabel",
            "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
            "http://www.seriouseats.com/recipes/2011/12/chicken-vesuvio-recipe.html",
            1613234382000)
        val recipe6 = Recipe(2200.0,
            "https://www.edamam.com/web-img/e42/e42f9119813e890af34c259785ae1cfb.jpg",
            "testLabel",
            "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
            "http://www.seriouseats.com/recipes/2011/12/chicken-vesuvio-recipe.html",
            1613320782000)
        val recipe7 = Recipe(2350.0,
            "https://www.edamam.com/web-img/e42/e42f9119813e890af34c259785ae1cfb.jpg",
            "testLabel",
            "http://www.edamam.com/ontologies/edamam.owl#recipe_b79327d05b8e5b838ad6cfd9576b30b6",
            "http://www.seriouseats.com/recipes/2011/12/chicken-vesuvio-recipe.html",
            1613407182000)

        return mutableListOf(recipe1, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7)
    }
}