# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in SDK/tools/proguard/proguard-android.txt

# Keep application class
-keep class com.skeletonviewer.** { *; }

# MediaPipe
-keep class com.google.mediapipe.** { *; }
-keep class com.google.protobuf.** { *; }
-dontwarn com.google.mediapipe.**

# Hilt
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# CameraX
-keep class androidx.camera.** { *; }

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }

# DataStore
-keep class androidx.datastore.** { *; }

# Gson / JSON (if used)
-keepattributes Signature
-keepattributes *Annotation*

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
}
