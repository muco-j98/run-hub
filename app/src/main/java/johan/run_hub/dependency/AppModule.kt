package johan.run_hub.dependency

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import johan.run_hub.db.ExerciseDatabase
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
    ).build()

    @Provides
    @Singleton
    fun provideExerciseDao(database: ExerciseDatabase) = database.getExerciseDao()
}