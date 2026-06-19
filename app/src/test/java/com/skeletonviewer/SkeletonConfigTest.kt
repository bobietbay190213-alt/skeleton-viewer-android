package com.skeletonviewer

import com.skeletonviewer.domain.models.SkeletonConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SkeletonConfigTest {

    @Test
    fun `default config has expected skeleton color`() {
        val config = SkeletonConfig()
        assertEquals(0xFF00FF41, config.skeletonColor)
    }

    @Test
    fun `copy preserves other fields when updating single field`() {
        val original = SkeletonConfig()
        val updated = original.copy(lineWidth = 7f)
        assertEquals(7f, updated.lineWidth)
        assertEquals(original.skeletonColor, updated.skeletonColor)
        assertEquals(original.showSkeleton, updated.showSkeleton)
    }

    @Test
    fun `show flags can be toggled independently`() {
        val config = SkeletonConfig(
            showSkeleton = true,
            showLandmarks = false,
            showFps = true,
            showLabels = false
        )
        assertTrue(config.showSkeleton)
        assertFalse(config.showLandmarks)
        assertTrue(config.showFps)
        assertFalse(config.showLabels)
    }

    @Test
    fun `confidence values are within valid range`() {
        val config = SkeletonConfig()
        assertTrue(config.detectionConfidence in 0f..1f)
        assertTrue(config.trackingConfidence in 0f..1f)
        assertTrue(config.visibilityThreshold in 0f..1f)
        assertTrue(config.presenceConfidence in 0f..1f)
    }

    @Test
    fun `numPoses is at least 1`() {
        val config = SkeletonConfig()
        assertTrue(config.numPoses >= 1)
    }
}
