package com.skeletonviewer

import com.skeletonviewer.domain.models.LandmarkIndex
import com.skeletonviewer.domain.models.PoseLandmarkModel
import com.skeletonviewer.domain.models.SkeletonConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PoseDetectorTest {

    @Test
    fun `LandmarkIndex has correct indices`() {
        assertEquals(0, LandmarkIndex.NOSE.index)
        assertEquals(11, LandmarkIndex.LEFT_SHOULDER.index)
        assertEquals(12, LandmarkIndex.RIGHT_SHOULDER.index)
        assertEquals(23, LandmarkIndex.LEFT_HIP.index)
        assertEquals(24, LandmarkIndex.RIGHT_HIP.index)
        assertEquals(27, LandmarkIndex.LEFT_ANKLE.index)
        assertEquals(28, LandmarkIndex.RIGHT_ANKLE.index)
    }

    @Test
    fun `BODY_CONNECTIONS contains all required connections`() {
        val connections = LandmarkIndex.BODY_CONNECTIONS
        assertTrue(connections.isNotEmpty())
        assertTrue(connections.any { it.first == LandmarkIndex.LEFT_SHOULDER && it.second == LandmarkIndex.LEFT_ELBOW })
        assertTrue(connections.any { it.first == LandmarkIndex.LEFT_ELBOW && it.second == LandmarkIndex.LEFT_WRIST })
        assertTrue(connections.any { it.first == LandmarkIndex.RIGHT_SHOULDER && it.second == LandmarkIndex.RIGHT_ELBOW })
        assertTrue(connections.any { it.first == LandmarkIndex.RIGHT_ELBOW && it.second == LandmarkIndex.RIGHT_WRIST })
        assertTrue(connections.any { it.first == LandmarkIndex.LEFT_HIP && it.second == LandmarkIndex.LEFT_KNEE })
        assertTrue(connections.any { it.first == LandmarkIndex.LEFT_KNEE && it.second == LandmarkIndex.LEFT_ANKLE })
        assertTrue(connections.any { it.first == LandmarkIndex.RIGHT_HIP && it.second == LandmarkIndex.RIGHT_KNEE })
        assertTrue(connections.any { it.first == LandmarkIndex.RIGHT_KNEE && it.second == LandmarkIndex.RIGHT_ANKLE })
        assertTrue(connections.any { it.first == LandmarkIndex.LEFT_SHOULDER && it.second == LandmarkIndex.RIGHT_SHOULDER })
    }

    @Test
    fun `SkeletonConfig default values are correct`() {
        val config = SkeletonConfig()
        assertEquals(0xFF00FF41, config.skeletonColor)
        assertEquals(0xFFFFFFFF, config.landmarkColor)
        assertEquals(4f, config.lineWidth)
        assertEquals(8f, config.landmarkRadius)
        assertTrue(config.showSkeleton)
        assertTrue(config.showLandmarks)
        assertTrue(config.showFps)
        assertFalse(config.showLabels)
        assertTrue(config.defaultFrontCamera)
        assertEquals(0.5f, config.detectionConfidence)
        assertEquals(0.5f, config.trackingConfidence)
        assertEquals(0.5f, config.visibilityThreshold)
    }

    @Test
    fun `PoseLandmarkModel holds correct values`() {
        val lm = PoseLandmarkModel(x = 0.5f, y = 0.3f, z = -0.1f, visibility = 0.99f, presence = 0.98f)
        assertEquals(0.5f, lm.x, 0.001f)
        assertEquals(0.3f, lm.y, 0.001f)
        assertEquals(-0.1f, lm.z, 0.001f)
        assertEquals(0.99f, lm.visibility, 0.001f)
        assertEquals(0.98f, lm.presence, 0.001f)
    }

    @Test
    fun `SkeletonConfig color methods return correct Color`() {
        val config = SkeletonConfig(skeletonColor = 0xFF00FF41, landmarkColor = 0xFFFFFFFF)
        val skeletonColor = config.getSkeletonColor()
        val landmarkColor = config.getLandmarkColor()
        assertEquals(0xFF00FF41.toLong(), skeletonColor.value.toLong())
        assertEquals(0xFFFFFFFF.toLong(), landmarkColor.value.toLong())
    }

    @Test
    fun `visibility threshold filters landmarks correctly`() {
        val threshold = 0.5f
        val visibleLandmark = PoseLandmarkModel(0.5f, 0.5f, 0f, 0.9f, 0.9f)
        val hiddenLandmark = PoseLandmarkModel(0.5f, 0.5f, 0f, 0.2f, 0.2f)
        assertTrue(visibleLandmark.visibility > threshold)
        assertFalse(hiddenLandmark.visibility > threshold)
    }
}
