package com.skeletonviewer.data.detector

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.skeletonviewer.domain.models.PoseLandmarkModel
import com.skeletonviewer.domain.models.PoseResultModel
import com.skeletonviewer.domain.models.SkeletonConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoseDetector @Inject constructor(
    private val context: Context
) {

    private var poseLandmarker: PoseLandmarker? = null
    private var currentConfig: SkeletonConfig = SkeletonConfig()
    private var isInitialized = false

    fun initialize(config: SkeletonConfig = SkeletonConfig()) {
        currentConfig = config
        setupPoseLandmarker()
    }

    private fun setupPoseLandmarker() {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath(MODEL_POSE_LANDMARKER)
                .setDelegate(Delegate.GPU)
                .build()

            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinPoseDetectionConfidence(currentConfig.detectionConfidence)
                .setMinTrackingConfidence(currentConfig.trackingConfidence)
                .setMinPosePresenceConfidence(currentConfig.presenceConfidence)
                .setNumPoses(currentConfig.numPoses)
                .setRunningMode(RunningMode.IMAGE)
                .build()

            poseLandmarker?.close()
            poseLandmarker = PoseLandmarker.createFromOptions(context, options)
            isInitialized = true
        } catch (e: Exception) {
            try {
                val baseOptionsCpu = BaseOptions.builder()
                    .setModelAssetPath(MODEL_POSE_LANDMARKER)
                    .setDelegate(Delegate.CPU)
                    .build()

                val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                    .setBaseOptions(baseOptionsCpu)
                    .setMinPoseDetectionConfidence(currentConfig.detectionConfidence)
                    .setMinTrackingConfidence(currentConfig.trackingConfidence)
                    .setMinPosePresenceConfidence(currentConfig.presenceConfidence)
                    .setNumPoses(currentConfig.numPoses)
                    .setRunningMode(RunningMode.IMAGE)
                    .build()

                poseLandmarker?.close()
                poseLandmarker = PoseLandmarker.createFromOptions(context, options)
                isInitialized = true
            } catch (ex: Exception) {
                isInitialized = false
            }
        }
    }

    fun detectPose(bitmap: Bitmap, frameTime: Long = SystemClock.uptimeMillis()): PoseResultModel? {
        if (!isInitialized || poseLandmarker == null) return null

        return try {
            val startTime = SystemClock.uptimeMillis()
            val mpImage = BitmapImageBuilder(bitmap).build()
            val result = poseLandmarker!!.detect(mpImage)
            val inferenceTime = SystemClock.uptimeMillis() - startTime

            if (result.landmarks().isEmpty()) return null

            val landmarks = result.landmarks()[0].map { landmark ->
                PoseLandmarkModel(
                    x = landmark.x(),
                    y = landmark.y(),
                    z = landmark.z(),
                    visibility = landmark.visibility().orElse(0f),
                    presence = landmark.presence().orElse(0f)
                )
            }

            PoseResultModel(
                landmarks = landmarks,
                inferenceTime = inferenceTime,
                inputImageWidth = bitmap.width,
                inputImageHeight = bitmap.height
            )
        } catch (e: Exception) {
            null
        }
    }

    fun updateConfig(config: SkeletonConfig) {
        currentConfig = config
        setupPoseLandmarker()
    }

    fun close() {
        poseLandmarker?.close()
        poseLandmarker = null
        isInitialized = false
    }

    companion object {
        private const val MODEL_POSE_LANDMARKER = "pose_landmarker_full.task"
    }
}