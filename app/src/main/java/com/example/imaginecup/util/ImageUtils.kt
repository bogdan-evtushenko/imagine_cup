package com.example.imaginecup.util

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

object ImageUtils {
    private val IMAGE_SIDE = 400

    /**
     * Crops and scale down source bitmap as a centered square.
     *
     * @param srcBitmap is bitmap to crop
     * @return square bitmap
     */
    fun scaleAndCropBitmap(srcBitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(cropSquare(srcBitmap), IMAGE_SIDE, IMAGE_SIDE, false)
    }

    /**
     * Crops source bitmap as a centered square.
     *
     * @param srcBmp is bitmap to crop
     * @return square bitmap
     */
    private fun cropSquare(srcBmp: Bitmap): Bitmap {
        return if (srcBmp.width >= srcBmp.height) {
            Bitmap.createBitmap(
                srcBmp,
                srcBmp.width / 2 - srcBmp.height / 2,
                0,
                srcBmp.height,
                srcBmp.height
            )
        } else {
            Bitmap.createBitmap(
                srcBmp,
                0,
                srcBmp.height / 2 - srcBmp.width / 2,
                srcBmp.width,
                srcBmp.width
            )
        }
    }

    fun displayRoundedPicture(context: Context, bitmap: Bitmap, into: ImageView) {
        val roundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(context.resources, scaleAndCropBitmap(bitmap))
        roundedBitmapDrawable.isCircular = true
        roundedBitmapDrawable.setAntiAlias(true)

        into.scaleType = ImageView.ScaleType.FIT_XY
        into.setImageDrawable(roundedBitmapDrawable)
    }

    fun displayRoundedPicture(
        context: Context,
        from: String,
        into: ImageView,
        errorRes: Int
    ): Target {
        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                displayRoundedPicture(context, bitmap, into)
            }

            override fun onBitmapFailed(errorDrawable: Drawable) {
                displayRoundedPicture(context, getBitmap(context, errorRes), into)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }
        }

        Picasso.with(context).cancelRequest(into)
        Picasso.with(context).load(getPath(from)).error(errorRes).into(target)

        return target
    }

    private fun getPath(path: String): String {
        return path
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun getBitmap(vectorDrawable: VectorDrawableCompat): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun getBitmap(context: Context, @DrawableRes drawableResId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableResId)
        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is VectorDrawableCompat -> getBitmap(drawable)
            is VectorDrawable -> getBitmap(drawable)
            else -> throw IllegalArgumentException("Unsupported drawable type")
        }
    }

}