package johan.run_hub.dependency

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import johan.run_hub.R
import johan.run_hub.constantValues.FoodApiKeys
import johan.run_hub.db.ExerciseDatabase
import johan.run_hub.db.entities.Exercise
import johan.run_hub.network.api.ApiHelper
import johan.run_hub.network.api.ApiHelperImpl
import johan.run_hub.network.api.FoodApi
import johan.run_hub.utils.PrePopulateUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    lateinit var exDb : ExerciseDatabase

    private val exec = Executors.newSingleThreadExecutor()

    @Provides
    @Singleton
    fun provideExerciseDatabase(
        @ApplicationContext context : Context
    ): ExerciseDatabase {
        exDb = Room.databaseBuilder(
            context,
            ExerciseDatabase::class.java,
            "exercise_db"
        ).fallbackToDestructiveMigration()
            .addCallback(object :RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val dao = exDb.getExerciseDao()

                    exec.execute {
                        dao.insertAllExercises(PrePopulateUtil.getExercises())
                        dao.insertAllRecipes(PrePopulateUtil.getRecipes())
                    }
                }
            })
            .build()

        return exDb
    }

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