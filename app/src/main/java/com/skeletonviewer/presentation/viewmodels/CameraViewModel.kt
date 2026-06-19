package com.skeletonviewer.presentation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skeletonviewer.data.camera.CameraManager
import com.skeletonviewer.data.preferences.UserPreferencesRepository
import com.skeletonviewer.domain.models.PoseResultModel
import com.skeletonviewer.domain.models.SkeletonConfig
import com.skeletonviewer.domain.usecases.DetectPoseUseCase
import com.skeletonviewer.domain.usecases.SaveImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import kotlin.math.roundToInt

data class CameraUiState(
    val isLoading: Boolean = false,
    val poseResult: PoseResultModel? = null,
    val fps: Float = 0f,
    val captureSuccess: Boolean = false,
    val captureError: String? = null,
    val isFrontCamera: Boolean = true
)

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraManager: CameraManager,
    private val detectPoseUseCase: DetectPoseUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    val skeletonConfig: StateFlow<SkeletonConfig> = prefsRepository.skeletonConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SkeletonConfig())

    private var lastBitmap: Bitmap? = null
    private val frameCount = AtomicLong(0)
    private var lastFpsTime = System.currentTimeMillis()
    private var framesSinceLastFps = 0

    init {
        setupFrameAnalysis()
    }

    private fun setupFrameAnalysis() {
        cameraManager.setFrameAnalysisCallback { bitmap, _ ->
            lastBitmap = bitmap
            val currentTime = System.currentTimeMillis()
            framesSinceLastFps++

            val elapsed = currentTime - lastFpsTime
            if (elapsed >= 1000) {
                val fps = (framesSinceLastFps * 1000f / elapsed)
                lastFpsTime = currentTime
                framesSinceLastFps = 0
                _uiState.value = _uiState.value.copy(fps = fps)
            }

            val result = detectPoseUseCase.execute(bitmap, frameCount.incrementAndGet())
            _uiState.value = _uiState.value.copy(poseResult = result)
        }
    }

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        val useFrontCamera = skeletonConfig.value.defaultFrontCamera
        _uiState.value = _uiState.value.copy(isFrontCamera = useFrontCamera)
        cameraManager.startCamera(context, lifecycleOwner, previewView, useFrontCamera)
    }

    fun switchCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        cameraManager.switchCamera(lifecycleOwner, previewView)
        _uiState.value = _uiState.value.copy(
            isFrontCamera = cameraManager.isFrontCamera.value
        )
    }

    fun setZoom(zoom: Float) {
        cameraManager.setZoom(zoom)
    }

    fun captureWithSkeleton(context: Context) {
        val currentBitmap = lastBitmap ?: return
        val poseResult = _uiState.value.poseResult
        val config = skeletonConfig.value

        viewModelScope.launch {
            try {
                val outputBitmap = currentBitmap.copy(Bitmap.Config.ARGB_8888, true)
                if (poseResult != null && config.showSkeleton) {
                    drawSkeletonOnBitmap(outputBitmap, poseResult, config)
                }
                val result = saveImageUseCase.execute(context, outputBitmap)
                result.fold(
                    onSuccess = { _uiState.value = _uiState.value.copy(captureSuccess = true, captureError = null) },
                    onFailure = { _uiState.value = _uiState.value.copy(captureError = it.message, captureSuccess = false) }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(captureError = e.message)
            }
        }
    }

    private fun drawSkeletonOnBitmap(bitmap: Bitmap, result: PoseResultModel, config: SkeletonConfig) {
        val canvas = Canvas(bitmap)
        val linePaint = Paint().apply {
            color = config.skeletonColor.toInt()
            strokeWidth = config.lineWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
        }
        val pointPaint = Paint().apply {
            color = config.landmarkColor.toInt()
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()

        if (config.showSkeleton) {
            com.skeletonviewer.domain.models.LandmarkIndex.BODY_CONNECTIONS.forEach { (start, end) ->
                if (start.index < result.landmarks.size && end.index < result.landmarks.size) {
                    val startLm = result.landmarks[start.index]
                    val endLm = result.landmarks[end.index]
                    if (startLm.visibility > config.visibilityThreshold && endLm.visibility > config.visibilityThreshold) {
                        canvas.drawLine(
                            startLm.x * width, startLm.y * height,
                            endLm.x * width, endLm.y * height,
                            linePaint
                        )
                    }
                }
            }
        }

        if (config.showLandmarks) {
            result.landmarks.forEach { lm ->
                if (lm.visibility > config.visibilityThreshold) {
                    canvas.drawCircle(lm.x * width, lm.y * height, config.landmarkRadius, pointPaint)
                }
            }
        }
    }

    fun clearCaptureState() {
        _uiState.value = _uiState.value.copy(captureSuccess = false, captureError = null)
    }

    fun updateConfig(config: SkeletonConfig) {
        viewModelScope.launch {
            prefsRepository.updateConfig(config)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraManager.stopCamera()
    }
}
