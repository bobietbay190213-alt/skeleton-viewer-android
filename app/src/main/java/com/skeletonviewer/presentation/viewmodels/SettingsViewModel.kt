package com.skeletonviewer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skeletonviewer.data.preferences.UserPreferencesRepository
import com.skeletonviewer.domain.models.SkeletonConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    val config: StateFlow<SkeletonConfig> = prefsRepository.skeletonConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SkeletonConfig())

    fun updateSkeletonColor(color: Long) {
        save(config.value.copy(skeletonColor = color))
    }

    fun updateLandmarkColor(color: Long) {
        save(config.value.copy(landmarkColor = color))
    }

    fun updateLineWidth(width: Float) {
        save(config.value.copy(lineWidth = width))
    }

    fun updateLandmarkRadius(radius: Float) {
        save(config.value.copy(landmarkRadius = radius))
    }

    fun updateShowSkeleton(show: Boolean) {
        save(config.value.copy(showSkeleton = show))
    }

    fun updateShowLandmarks(show: Boolean) {
        save(config.value.copy(showLandmarks = show))
    }

    fun updateShowFps(show: Boolean) {
        save(config.value.copy(showFps = show))
    }

    fun updateShowLabels(show: Boolean) {
        save(config.value.copy(showLabels = show))
    }

    fun updateDefaultFrontCamera(front: Boolean) {
        save(config.value.copy(defaultFrontCamera = front))
    }

    fun updateDetectionConfidence(confidence: Float) {
        save(config.value.copy(detectionConfidence = confidence))
    }

    fun updateTrackingConfidence(confidence: Float) {
        save(config.value.copy(trackingConfidence = confidence))
    }

    fun resetToDefaults() {
        save(SkeletonConfig())
    }

    private fun save(newConfig: SkeletonConfig) {
        viewModelScope.launch {
            prefsRepository.updateConfig(newConfig)
        }
    }
}
