package johan.run_hub.dependency

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import johan.run_hub.R
import johan.run_hub.constantValues.FoodApiKeys
import johan.run_hub.db.ExerciseDatabase
import johan.run_hub.network.api.ApiHelper
import johan.run_hub.network.api.ApiHelperImpl
import johan.run_hub.network.api.FoodApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExerciseDatabase(
        @ApplicationContext context : Context
    ) = Room.databaseBuilder(
        context,
        ExerciseDatabase::class.java,
        "exercise_db"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideExerciseDao(database: ExerciseDatabase) = database.getExerciseDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ) = context.getSharedPreferences(R.string.preference_file_key.toString(), Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideFirstTimeBoolean(sharedPref: SharedPreferences) =
        sharedPref.getBoolean("FIRST_TIME", true)

    @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) =
        sharedPref.getFloat("WEIGHT", 0f)

    @Provides
    fun provideBaseUrl() = FoodApiKeys.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = run {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideFoodApi(retrofit: Retrofit) = retrofit.create(FoodApi::class.java)

    @Singleton
    @Provides
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}