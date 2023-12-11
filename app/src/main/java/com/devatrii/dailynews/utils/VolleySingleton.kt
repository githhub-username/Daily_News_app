package com.devatrii.dailynews.utils

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton(context: Context) {

    companion object {
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context): VolleySingleton {
            if (INSTANCE == null) {
                INSTANCE = VolleySingleton(context)
            }
            return INSTANCE!!
        }
    }

    val requestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}