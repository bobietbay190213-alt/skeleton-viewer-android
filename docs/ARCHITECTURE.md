# Architecture

## Overview

Skeleton Viewer follows **MVVM + Clean Architecture** with strict layer separation enforced by package structure.

```
┌──────────────────────────────────────────────┐
│                Presentation Layer             │
│  Compose Screens → ViewModels → UiState       │
│  OverlayView (Canvas) ← ViewModel            │
└───────────────────┬──────────────────────────┘
                    │ (domain models only)
┌───────────────────▼──────────────────────────┐
│                 Domain Layer                  │
│  UseCases ← Repository Interfaces            │
│  Models: PoseResultModel, SkeletonConfig      │
└───────────────────┬──────────────────────────┘
                    │ (implementations)
┌───────────────────▼──────────────────────────┐
│                  Data Layer                   │
│  PoseDetector (MediaPipe)                     │
│  CameraManager (CameraX)                      │
│  StorageManager (MediaStore)                  │
│  UserPreferencesRepository (DataStore)        │
└──────────────────────────────────────────────┘
```

## Data Flow

```
Camera Frame
    │
    ▼
CameraManager.frameAnalysis()
    │  Bitmap
    ▼
PoseDetector.detectPose()
    │  PoseResultModel
    ▼
CameraViewModel._uiState
    │  CameraUiState
    ▼
CameraScreen observes state
    │
    ├──▶ OverlayView.updatePoseResult() → Canvas.draw()
    └──▶ FPS counter update
```

## Key Decisions

### MediaPipe GPU → CPU Fallback
`PoseDetector` first tries GPU delegate. If initialization fails (e.g., on emulators), it falls back to CPU delegate automatically.

### Image Analysis + Canvas Overlay
CameraX `ImageAnalysis` provides Bitmap frames to `PoseDetector`. Results are posted to `OverlayView` which draws directly on a transparent `View` layered above the `PreviewView`. This avoids re-encoding the camera feed.

### Bitmap Rotation and Mirror
`CameraManager` corrects frame rotation using `imageProxy.imageInfo.rotationDegrees` and mirrors front-camera frames via `Matrix.postScale(-1, 1)` so the skeleton matches what the user sees in the preview.

### DataStore for Settings
Preferences use Jetpack DataStore (not SharedPreferences) for coroutine-safe, type-safe reads and writes with a version-migration-friendly format.

### StateFlow over LiveData
All observable state uses `StateFlow` from Kotlin Coroutines. `SharingStarted.WhileSubscribed(5000)` prevents unnecessary computation when the UI is in the background.
