package com.devatrii.dailynews.utils


import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
This Class Credit goes to geeks4Geeks
Link to full article: https://www.geeksforgeeks.org/how-to-display-html-in-textview-along-with-images-in-android/
I changed little bit made it compatible with glide
 */
// Class to download Images which extends [Html.ImageGetter]
class ImageGetter(
    private val res: Resources, private val htmlTextView: TextView, private val context: Context
) : Html.ImageGetter {
    // Function needs to overridden when extending [Html.ImageGetter] ,
    // which will download the image
    override fun getDrawable(url: String): Drawable {
        val holder = BitmapDrawablePlaceHolder(res, null)
        GlobalScope.launch(Dispatchers.IO) {
            runCatching {
                // downloading image in bitmap format using [Glide] Library
                val futureTarget: FutureTarget<Bitmap> =
                    Glide.with(context).asBitmap().load(url).submit()
                val bitmap = futureTarget.get()
                val drawable = BitmapDrawable(res, bitmap)
                // To make sure Images don't go out of screen , Setting width less
                // than screen width, You can change image size if you want
                val width = getScreenWidth() - 150
                // Images may stretch out if you will only resize width,
                // hence resize height to according to aspect ratio
                val aspectRatio: Float =
                    (drawable.intrinsicWidth.toFloat()) / (drawable.intrinsicHeight.toFloat())
                val height = width / aspectRatio
                drawable.setBounds(10, 20, width, height.toInt())
                holder.setDrawable(drawable)
                holder.setBounds(10, 20, width, height.toInt())
                withContext(Dispatchers.Main) {
                    htmlTextView.text = htmlTextView.text
                }
            }
        }
        return holder
    }

    // Actually Putting images
    internal class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) :
        BitmapDrawable(res, bitmap) {
        private var drawable: Drawable? = null
        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
        }
    }

    // Function to get screenWidth used above
    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels
}