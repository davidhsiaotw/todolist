package com.example.todolist.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import java.util.Arrays

/**
 * Compare two drawables by their bytes or pixels.
 *
 * <a href="https://gist.github.com/XinyueZ/3cca89416a1e443f914ed37f80ed59f2">Code Source</a>
 */
object DrawableComparator {
    fun <T : Drawable> T.bytesEqualTo(t: T?) = toBitmap().bytesEqualTo(t?.toBitmap(), true)

    fun <T : Drawable> T.pixelsEqualTo(t: T?) = toBitmap().pixelsEqualTo(t?.toBitmap(), true)

    private fun Bitmap.bytesEqualTo(otherBitmap: Bitmap?, shouldRecycle: Boolean = false) =
        otherBitmap?.let { other ->
            if (width == other.width && height == other.height) {
                val res = toBytes().contentEquals(other.toBytes())
                if (shouldRecycle) {
                    doRecycle().also { otherBitmap.doRecycle() }
                }
                res
            } else false
        } ?: kotlin.run { false }

    private fun Bitmap.pixelsEqualTo(otherBitmap: Bitmap?, shouldRecycle: Boolean = false) =
        otherBitmap?.let { other ->
            if (width == other.width && height == other.height) {
                val res = Arrays.equals(toPixels(), other.toPixels())
                if (shouldRecycle) {
                    doRecycle().also { otherBitmap.doRecycle() }
                }
                res
            } else false
        } ?: kotlin.run { false }

    private fun Bitmap.doRecycle() {
        if (!isRecycled) recycle()
    }

    private fun <T : Drawable> T.toBitmap(): Bitmap {
        if (this is BitmapDrawable) return bitmap

        val drawable: Drawable = this
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun Bitmap.toBytes(): ByteArray = ByteArrayOutputStream().use { stream ->
        compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.toByteArray()
    }

    private fun Bitmap.toPixels() =
        IntArray(width * height).apply { getPixels(this, 0, width, 0, 0, width, height) }
}