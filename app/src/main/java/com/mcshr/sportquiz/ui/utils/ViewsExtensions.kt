package com.mcshr.sportquiz.ui.utils

import android.view.View

fun View.setDebounceOnClickListener(onClick: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if ( currentTime - lastClickTime > 1000L) {
            lastClickTime = currentTime
            onClick(it)
        }
    }
}