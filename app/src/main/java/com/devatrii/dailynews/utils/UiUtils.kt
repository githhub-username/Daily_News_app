package com.devatrii.dailynews.utils

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

fun View.removeView() {
    visibility = View.GONE
}
fun TextView.setHtmlAsText(html:String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
    } else {
        this.text = Html.fromHtml(html);
    }
}
fun View.hideView() {
    visibility = View.INVISIBLE
}

fun View.showView() {
    visibility = View.VISIBLE
}

