package com.devatrii.dailynews.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun showMessage(message: String = "Message", context: Context, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, length).show()
}

@SuppressLint("CheckResult")
fun loadImageWithGlide(url: String, imageView: ImageView, context: Context) {
    Glide.with(context)
        .load(url)
        .transition(withCrossFade())
        .thumbnail(0.5f)
        .into(imageView)
}

fun errorDialog(
    message: String,
    listener: DialogInterface.OnClickListener? = null,
    context: Context
) {
    MaterialAlertDialogBuilder(context)
        .apply {
            setTitle("Error")
            setMessage(message)
            setCancelable(false)
            setPositiveButton("Retry", listener)
            show()
        }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertDateFormat(dateTimeString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("MMMM,d,yyyy", Locale.ENGLISH)
    val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
    return outputFormatter.format(dateTime)
}

fun openChromeTab(context: Context, url: String) {
    try {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    } catch (e: Exception) {
        logError("Chrome_Tab","Error ${e.localizedMessage}")
    }
}