package co.storibank.presentation.ui.screens.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image =
        File.createTempFile(
            imageFileName,
            ".jpg",
            externalCacheDir,
        )
    return image
}

fun uriToByteArray(
    contentResolver: ContentResolver,
    uri: Uri,
): ByteArray? {
    var inputStream: InputStream? = null
    return try {
        inputStream = contentResolver.openInputStream(uri)
        val buffer = ByteArrayOutputStream()
        if (inputStream != null) {
            val bufferSize = 1024
            val bufferArray = ByteArray(bufferSize)
            var len: Int
            while (inputStream.read(bufferArray).also { len = it } != -1) {
                buffer.write(bufferArray, 0, len)
            }
            buffer.flush()
            buffer.toByteArray()
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
    }
}
