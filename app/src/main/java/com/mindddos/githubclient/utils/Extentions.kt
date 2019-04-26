package com.mindddos.githubclient.utils

import android.view.View

inline fun View.initDebouncedClickListener(crossinline body: () -> Unit) {
    this.setOnClickListener(
        object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                body()
            }
        })
}


