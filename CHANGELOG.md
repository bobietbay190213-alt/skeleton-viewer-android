# Changelog

All notable changes to Skeleton Viewer are documented in this file.

Format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

---

## [1.0.0] — 2024-01-01

### Added
- Real-time pose detection using MediaPipe Pose Landmarker Full model
- 33-point body landmark detection (head, torso, arms, legs, feet)
- Live skeleton overlay on CameraX preview using Canvas
- Front and back camera switching
- Pinch-to-zoom gesture support
- Photo capture with skeleton overlay included
- Automatic save to device gallery (MediaStore API, Android 10+ compatible)
- FPS counter display
- Customizable skeleton color (7 preset colors)
- Adjustable line width (1–10px)
- Adjustable landmark dot size (4–20px)
- Toggle: show/hide skeleton connections
- Toggle: show/hide landmark dots
- Toggle: show/hide FPS counter
- Toggle: show/hide landmark labels
- Detection and tracking confidence sliders
- Persistent settings via DataStore Preferences
- Default camera preference (front/back)
- MVVM + Clean Architecture
- Hilt dependency injection
- Jetpack Compose UI with Material Design 3
- Dark mode support with custom green (#00FF41) accent
- GitHub Actions CI/CD pipeline for automatic APK builds
- Unit tests for models and use cases
- Instrumented UI tests
- ProGuard rules for release optimization

### Technical Details
- Min SDK: 26 (Android 8.0)
- Target SDK: 35 (Android 15)
- Kotlin 2.0.21
- Jetpack Compose BOM 2024.12.01
- CameraX 1.4.1
- MediaPipe Tasks Vision 0.10.18
- Hilt 2.53.1
- DataStore Preferences 1.1.2
