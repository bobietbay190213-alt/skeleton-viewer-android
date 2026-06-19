package com.skeletonviewer.domain.usecases

import android.graphics.Bitmap
import com.skeletonviewer.data.detector.PoseDetector
import com.skeletonviewer.domain.models.PoseResultModel
import javax.inject.Inject

class DetectPoseUseCase @Inject constructor(
    private val poseDetector: PoseDetector
) {
    fun execute(bitmap: Bitmap, frameTime: Long): PoseResultModel? {
        return poseDetector.detectPose(bitmap, frameTime)
    }
}
