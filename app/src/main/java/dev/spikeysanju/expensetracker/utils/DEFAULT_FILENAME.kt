package dev.spikeysanju.expensetracker.utils

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore

@JvmField
val DEFAULT_FILENAME = "${"Expenso" + System.currentTimeMillis()}.png"

fun saveBitmap(activity: Activity, bitmap: Bitmap, filename: String = DEFAULT_FILENAME): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    }

    val contentResolver = activity.contentResolver

    val imageUri: Uri? = contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )

    return imageUri.also {
        val fileOutputStream = imageUri?.let { contentResolver.openOutputStream(it) }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream?.close()
    }
}
