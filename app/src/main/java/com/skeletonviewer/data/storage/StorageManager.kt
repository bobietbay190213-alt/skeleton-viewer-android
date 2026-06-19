package com.skeletonviewer.data.storage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor() {

    suspend fun saveImageToGallery(context: Context, bitmap: Bitmap): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val filename = generateFileName()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveImageQ(context, bitmap, filename)
                } else {
                    saveImageLegacy(context, bitmap, filename)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun saveImageQ(context: Context, bitmap: Bitmap, filename: String): Result<String> {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/SkeletonViewer")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri: Uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: return Result.failure(Exception("Failed to create MediaStore entry"))

        resolver.openOutputStream(uri)?.use { stream ->
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                return Result.failure(Exception("Bitmap compression failed"))
            }
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)

        return Result.success(uri.toString())
    }

    private fun saveImageLegacy(context: Context, bitmap: Bitmap, filename: String): Result<String> {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val appDir = File(picturesDir, "SkeletonViewer").apply { mkdirs() }
        val imageFile = File(appDir, filename)

        FileOutputStream(imageFile).use { stream ->
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                return Result.failure(Exception("Bitmap compression failed"))
            }
        }

        return Result.success(imageFile.absolutePath)
    }

    private fun generateFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "SKELETON_$timestamp.jpg"
    }

    fun getCacheImageFile(context: Context): File {
        val cacheDir = File(context.cacheDir, "skeleton_captures").apply { mkdirs() }
        return File(cacheDir, "${System.currentTimeMillis()}.jpg")
    }
}
