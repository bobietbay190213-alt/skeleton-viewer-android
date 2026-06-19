package com.skeletonviewer.domain.models

import androidx.compose.ui.graphics.Color

data class SkeletonConfig(
    val skeletonColor: Long = 0xFF00FF41,
    val landmarkColor: Long = 0xFFFFFFFF,
    val lineWidth: Float = 4f,
    val landmarkRadius: Float = 8f,
    val showSkeleton: Boolean = true,
    val showLandmarks: Boolean = true,
    val showFps: Boolean = true,
    val showLabels: Boolean = false,
    val visibilityThreshold: Float = 0.5f,
    val defaultFrontCamera: Boolean = true,
    val detectionConfidence: Float = 0.5f,
    val trackingConfidence: Float = 0.5f,
    val presenceConfidence: Float = 0.5f,
    val numPoses: Int = 1
) {
    fun getSkeletonColor(): Color = Color(skeletonColor)
    fun getLandmarkColor(): Color = Color(landmarkColor)
}
