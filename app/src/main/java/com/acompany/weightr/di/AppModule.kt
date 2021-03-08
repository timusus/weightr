package com.acompany.weightr.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun coroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
        }
    }

    @Provides
    @Singleton
    @Named("AppSupervisorJob")
    fun appSupervisorJob(): Job {
        return SupervisorJob()
    }

    @Provides
    @Singleton
    @Named("AppCoroutineScope")
    fun provideAppCoroutineScope(@Named("AppSupervisorJob") job: Job, coroutineExceptionHandler: CoroutineExceptionHandler): CoroutineScope {
        return CoroutineScope(Dispatchers.Main + job + coroutineExceptionHandler)
    }
}