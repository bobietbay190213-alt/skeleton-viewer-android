package com.skeletonviewer.di

import android.content.Context
import com.skeletonviewer.data.camera.CameraManager
import com.skeletonviewer.data.detector.PoseDetector
import com.skeletonviewer.data.storage.StorageManager
import com.skeletonviewer.data.preferences.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun providePoseDetector(@ApplicationContext context: Context): PoseDetector {
        return PoseDetector(context)
    }

    @Provides
    @Singleton
    fun provideCameraManager(): CameraManager = CameraManager()

    @Provides
    @Singleton
    fun provideStorageManager(): StorageManager = StorageManager()

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository = UserPreferencesRepository(context)
}
