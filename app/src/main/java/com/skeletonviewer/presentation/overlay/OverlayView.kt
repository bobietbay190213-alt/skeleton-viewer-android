package com.skeletonviewer.presentation.overlay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.skeletonviewer.domain.models.LandmarkIndex
import com.skeletonviewer.domain.models.PoseLandmarkModel
import com.skeletonviewer.domain.models.PoseResultModel
import com.skeletonviewer.domain.models.SkeletonConfig

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var poseResult: PoseResultModel? = null
    private var config: SkeletonConfig = SkeletonConfig()

    private val linePaint = Paint().apply {
        color = Color.parseColor("#00FF41")
        strokeWidth = 4f
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val landmarkPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val headPaint = Paint().apply {
        color = Color.parseColor("#00FF41")
        style = Paint.Style.FILL
        isAntiAlias = true
        alpha = 100
    }

    private val labelPaint = Paint().apply {
        color = Color.WHITE
        textSize = 24f
        isAntiAlias = true
        setShadowLayer(2f, 1f, 1f, Color.BLACK)
    }

    private val fpsPaint = Paint().apply {
        color = Color.YELLOW
        textSize = 40f
        isAntiAlias = true
        isFakeBoldText = true
        setShadowLayer(3f, 2f, 2f, Color.BLACK)
    }

    private var currentFps: Float = 0f
    private val headRect = RectF()

    fun updateConfig(newConfig: SkeletonConfig) {
        config = newConfig
        linePaint.color = newConfig.skeletonColor.toInt()
        linePaint.strokeWidth = newConfig.lineWidth
        landmarkPaint.color = newConfig.landmarkColor.toInt()
        headPaint.color = newConfig.skeletonColor.toInt()
        invalidate()
    }

    fun updatePoseResult(result: PoseResultModel?, fps: Float) {
        poseResult = result
        currentFps = fps
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val result = poseResult ?: run {
            drawFpsIfNeeded(canvas)
            return
        }
        if (result.landmarks.isEmpty()) {
            drawFpsIfNeeded(canvas)
            return
        }

        val scaleX = width.toFloat()
        val scaleY = height.toFloat()

        if (config.showSkeleton) {
            drawConnections(canvas, result.landmarks, scaleX, scaleY)
        }

        if (config.showLandmarks) {
            drawLandmarks(canvas, result.landmarks, scaleX, scaleY)
        }

        drawHead(canvas, result.landmarks, scaleX, scaleY)
        drawFpsIfNeeded(canvas)
    }

    private fun drawConnections(
        canvas: Canvas,
        landmarks: List<PoseLandmarkModel>,
        scaleX: Float,
        scaleY: Float
    ) {
        LandmarkIndex.BODY_CONNECTIONS.forEach { (start, end) ->
            if (start.index < landmarks.size && end.index < landmarks.size) {
                val startLm = landmarks[start.index]
                val endLm = landmarks[end.index]
                if (startLm.visibility > config.visibilityThreshold &&
                    endLm.visibility > config.visibilityThreshold
                ) {
                    val startX = startLm.x * scaleX
                    val startY = startLm.y * scaleY
                    val endX = endLm.x * scaleX
                    val endY = endLm.y * scaleY
                    drawLine(canvas, startX, startY, endX, endY)
                }
            }
        }
    }

    private fun drawLine(canvas: Canvas, x1: Float, y1: Float, x2: Float, y2: Float) {
        canvas.drawLine(x1, y1, x2, y2, linePaint)
    }

    private fun drawLandmarks(
        canvas: Canvas,
        landmarks: List<PoseLandmarkModel>,
        scaleX: Float,
        scaleY: Float
    ) {
        landmarks.forEachIndexed { index, landmark ->
            if (landmark.visibility > config.visibilityThreshold) {
                val cx = landmark.x * scaleX
                val cy = landmark.y * scaleY
                drawCircle(canvas, cx, cy, config.landmarkRadius)

                if (config.showLabels && index < LandmarkIndex.entries.size) {
                    val label = LandmarkIndex.entries[index].label
                    drawLabel(canvas, label, cx, cy - config.landmarkRadius - 4)
                }
            }
        }
    }

    private fun drawCircle(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        canvas.drawCircle(cx, cy, radius, landmarkPaint)
    }

    private fun drawHead(
        canvas: Canvas,
        landmarks: List<PoseLandmarkModel>,
        scaleX: Float,
        scaleY: Float
    ) {
        if (landmarks.size <= LandmarkIndex.NOSE.index) return
        val nose = landmarks[LandmarkIndex.NOSE.index]
        if (nose.visibility < config.visibilityThreshold) return

        val noseX = nose.x * scaleX
        val noseY = nose.y * scaleY
        val headRadius = config.landmarkRadius * 3.5f
        headRect.set(noseX - headRadius, noseY - headRadius * 1.2f, noseX + headRadius, noseY + headRadius * 0.8f)
        canvas.drawOval(headRect, headPaint)
    }

    private fun drawLabel(canvas: Canvas, text: String, x: Float, y: Float) {
        canvas.drawText(text, x, y, labelPaint)
    }

    private fun drawFpsIfNeeded(canvas: Canvas) {
        if (config.showFps) {
            canvas.drawText("FPS: ${currentFps.toInt()}", 32f, 80f, fpsPaint)
        }
    }
}
