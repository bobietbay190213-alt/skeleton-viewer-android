package com.skeletonviewer.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.skeletonviewer.domain.models.SkeletonConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "skeleton_prefs")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val SKELETON_COLOR = longPreferencesKey("skeleton_color")
        val LANDMARK_COLOR = longPreferencesKey("landmark_color")
        val LINE_WIDTH = floatPreferencesKey("line_width")
        val LANDMARK_RADIUS = floatPreferencesKey("landmark_radius")
        val SHOW_SKELETON = booleanPreferencesKey("show_skeleton")
        val SHOW_LANDMARKS = booleanPreferencesKey("show_landmarks")
        val SHOW_FPS = booleanPreferencesKey("show_fps")
        val SHOW_LABELS = booleanPreferencesKey("show_labels")
        val DEFAULT_FRONT_CAMERA = booleanPreferencesKey("default_front_camera")
        val DETECTION_CONFIDENCE = floatPreferencesKey("detection_confidence")
        val TRACKING_CONFIDENCE = floatPreferencesKey("tracking_confidence")
        val VISIBILITY_THRESHOLD = floatPreferencesKey("visibility_threshold")
    }

    val skeletonConfig: Flow<SkeletonConfig> = context.dataStore.data.map { prefs ->
        SkeletonConfig(
            skeletonColor = prefs[Keys.SKELETON_COLOR] ?: 0xFF00FF41,
            landmarkColor = prefs[Keys.LANDMARK_COLOR] ?: 0xFFFFFFFF,
            lineWidth = prefs[Keys.LINE_WIDTH] ?: 4f,
            landmarkRadius = prefs[Keys.LANDMARK_RADIUS] ?: 8f,
            showSkeleton = prefs[Keys.SHOW_SKELETON] ?: true,
            showLandmarks = prefs[Keys.SHOW_LANDMARKS] ?: true,
            showFps = prefs[Keys.SHOW_FPS] ?: true,
            showLabels = prefs[Keys.SHOW_LABELS] ?: false,
            defaultFrontCamera = prefs[Keys.DEFAULT_FRONT_CAMERA] ?: true,
            detectionConfidence = prefs[Keys.DETECTION_CONFIDENCE] ?: 0.5f,
            trackingConfidence = prefs[Keys.TRACKING_CONFIDENCE] ?: 0.5f,
            visibilityThreshold = prefs[Keys.VISIBILITY_THRESHOLD] ?: 0.5f
        )
    }

    suspend fun updateConfig(config: SkeletonConfig) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SKELETON_COLOR] = config.skeletonColor
            prefs[Keys.LANDMARK_COLOR] = config.landmarkColor
            prefs[Keys.LINE_WIDTH] = config.lineWidth
            prefs[Keys.LANDMARK_RADIUS] = config.landmarkRadius
            prefs[Keys.SHOW_SKELETON] = config.showSkeleton
            prefs[Keys.SHOW_LANDMARKS] = config.showLandmarks
            prefs[Keys.SHOW_FPS] = config.showFps
            prefs[Keys.SHOW_LABELS] = config.showLabels
            prefs[Keys.DEFAULT_FRONT_CAMERA] = config.defaultFrontCamera
            prefs[Keys.DETECTION_CONFIDENCE] = config.detectionConfidence
            prefs[Keys.TRACKING_CONFIDENCE] = config.trackingConfidence
            prefs[Keys.VISIBILITY_THRESHOLD] = config.visibilityThreshold
        }
    }
}
