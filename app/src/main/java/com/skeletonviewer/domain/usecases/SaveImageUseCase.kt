package com.skeletonviewer.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import com.skeletonviewer.data.storage.StorageManager
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val storageManager: StorageManager
) {
    suspend fun execute(context: Context, bitmap: Bitmap): Result<String> {
        return storageManager.saveImageToGallery(context, bitmap)
    }
}
