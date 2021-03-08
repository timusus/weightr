package com.acompany.weightr.di

import android.content.Context
import com.acompany.data.DataImporter
import com.acompany.data.WeightrRepository
import com.acompany.data.room.database.AppDatabase
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.create(context)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(database: AppDatabase): WeightrRepository {
        return WeightrRepository(database.sessionDao(), database.exerciseDao())
    }

    @Provides
    fun provideDataImporter(@ApplicationContext context: Context, moshi: Moshi, weightrRepository: WeightrRepository): DataImporter {
        return DataImporter(context, moshi, weightrRepository)
    }
}