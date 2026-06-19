# 🦴 Skeleton Viewer

<div align="center">

![Android](https://img.shields.io/badge/Android-API%2026%2B-green?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin)
![MediaPipe](https://img.shields.io/badge/MediaPipe-0.10.18-blue?logo=google)
![License](https://img.shields.io/badge/License-MIT-yellow)
![CI](https://img.shields.io/github/actions/workflow/status/YOUR_USERNAME/skeleton-viewer-android/android-build.yml?label=Build)

**Real-time human body skeleton detection using MediaPipe Pose on Android**

</div>

---

## 📱 Features

- 🎥 **Real-time camera feed** — front & back camera support
- 🦴 **33-point body skeleton** — full pose detection with MediaPipe Pose Landmarker
- 🎨 **Skeleton overlay** — live rendering with Canvas on top of camera preview
- 📸 **Capture with skeleton** — save photos including the skeleton overlay to gallery
- ⚡ **FPS counter** — real-time frame rate display
- 🔍 **Pinch to zoom** — smooth camera zoom via gesture
- 🎛️ **Full customization** — colors, line width, landmark size, confidence thresholds
- 💾 **Persistent settings** — preferences saved via DataStore

## 🦴 Body Landmarks Tracked

| Region | Landmarks |
|--------|-----------|
| **Head** | Nose, Left Eye, Right Eye, Left Ear, Right Ear |
| **Arms** | Shoulders, Elbows, Wrists (left & right) |
| **Torso** | Shoulder-to-shoulder, Hip-to-hip, Torso sides |
| **Legs** | Hips, Knees, Ankles, Heels, Foot index (left & right) |

## 🏗️ Architecture

```
com.skeletonviewer/
├── presentation/
│   ├── MainActivity.kt              # Hilt entry point
│   ├── navigation/NavGraph.kt       # Compose NavHost
│   ├── screens/
│   │   ├── CameraScreen.kt          # Main camera view + overlay
│   │   └── SettingsScreen.kt        # Settings UI
│   ├── viewmodels/
│   │   ├── CameraViewModel.kt       # Camera + pose state
│   │   └── SettingsViewModel.kt     # Config management
│   ├── overlay/OverlayView.kt       # Canvas skeleton renderer
│   └── theme/                       # Material 3 theming
├── domain/
│   ├── models/
│   │   ├── PoseLandmarkModel.kt     # Pose data + LandmarkIndex
│   │   └── SkeletonConfig.kt        # User config model
│   └── usecases/
│       ├── DetectPoseUseCase.kt     # Pose detection use case
│       └── SaveImageUseCase.kt      # Image saving use case
├── data/
│   ├── camera/CameraManager.kt      # CameraX management
│   ├── detector/PoseDetector.kt     # MediaPipe integration
│   ├── storage/StorageManager.kt    # Gallery save logic
│   └── preferences/UserPreferencesRepository.kt  # DataStore
└── di/AppModule.kt                  # Hilt dependency injection
```

**Pattern:** MVVM + Clean Architecture + Hilt DI + Jetpack Compose

## 🛠️ Tech Stack

| Category | Library | Version |
|----------|---------|---------|
| Language | Kotlin | 2.0.21 |
| UI | Jetpack Compose + Material 3 | BOM 2024.12 |
| Camera | CameraX | 1.4.1 |
| AI/ML | MediaPipe Tasks Vision | 0.10.18 |
| DI | Hilt | 2.53.1 |
| Navigation | Navigation Compose | 2.8.5 |
| Preferences | DataStore Preferences | 1.1.2 |
| Permissions | Accompanist Permissions | 0.36.0 |
| Build | Gradle 8.9 + KSP | — |

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17+
- Android SDK API 35
- A physical Android device (API 26+) or emulator with camera

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/skeleton-viewer-android.git
cd skeleton-viewer-android
```

### 2. Download the MediaPipe Model

The MediaPipe Pose Landmarker model must be placed in `app/src/main/assets/`:

```bash
mkdir -p app/src/main/assets
wget "https://storage.googleapis.com/mediapipe-models/pose_landmarker/pose_landmarker_full/float16/latest/pose_landmarker_full.task" \
  -O app/src/main/assets/pose_landmarker_full.task
```

Or use the download script:
```bash
chmod +x scripts/download_model.sh
./scripts/download_model.sh
```

### 3. Open in Android Studio

```
File → Open → Select the project folder
```

### 4. Build & Run

```bash
./gradlew assembleDebug
# or
./gradlew assembleRelease
```

## ⚙️ Settings

| Setting | Description | Default |
|---------|-------------|---------|
| Skeleton Color | Color of connection lines | Green (#00FF41) |
| Line Width | Thickness of skeleton lines | 4px |
| Landmark Size | Radius of joint dots | 8px |
| Show Skeleton | Toggle connection lines | On |
| Show Landmarks | Toggle joint dots | On |
| Show FPS | Frame rate counter | On |
| Show Labels | Landmark name labels | Off |
| Detection Confidence | Min pose detection score | 50% |
| Tracking Confidence | Min tracking score | 50% |
| Default Front Camera | Camera on launch | Front |

## 🤖 GitHub Actions — Automatic APK Build

This project uses GitHub Actions to automatically build and distribute APKs.

### Workflow Triggers

| Event | Action |
|-------|--------|
| Push to `main`/`master` | Build release APK + create GitHub Release |
| Pull Request | Build and validate |
| Manual dispatch | Choose debug or release build |

### Downloading the APK

1. Go to your repository on GitHub
2. Click **Actions** tab
3. Select the latest **Android CI — Build & Release APK** run
4. Scroll to **Artifacts** at the bottom
5. Download **SkeletonViewer-Release-APK**

Or download from the **Releases** page (auto-created on push to main).

### Workflow Steps

```
1. Checkout Code
2. Setup JDK 17 (Temurin)
3. Setup Android SDK API 35
4. Cache Gradle dependencies
5. Download MediaPipe model
6. Run Unit Tests
7. Build Release APK
8. Upload APK Artifact (30-day retention)
9. Create GitHub Release (on push to main)
```

## 📋 Requirements

- **Min SDK:** Android 8.0 (API 26)
- **Target SDK:** Android 15 (API 35)
- **Permissions:** `CAMERA`, `READ_MEDIA_IMAGES`
- **Hardware:** Camera with auto-focus recommended

## 🧪 Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

## 📁 Project Structure

```
skeleton-viewer-android/
├── app/                          # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/           # MediaPipe model (.task file)
│   │   │   ├── java/com/skeletonviewer/
│   │   │   └── res/              # Layouts, drawables, strings
│   │   ├── test/                 # Unit tests
│   │   └── androidTest/          # Instrumentation tests
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml        # Version catalog
│   └── wrapper/
├── .github/
│   └── workflows/
│       └── android-build.yml     # CI/CD pipeline
├── scripts/
│   └── download_model.sh         # MediaPipe model downloader
├── docs/                         # Documentation
├── screenshots/                  # App screenshots
├── settings.gradle.kts
├── build.gradle.kts
├── gradlew
└── README.md
```

## 🤝 Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## 📝 Changelog

See [CHANGELOG.md](CHANGELOG.md) for version history.

## 📄 License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.

---

<div align="center">
Built with ❤️ using MediaPipe, CameraX, and Jetpack Compose
</div>
