package com.mindddos.githubclient.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

object TextUtils {
    fun getHyperlinkSpannable(text: String, link: String): Spanned {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml("<a href='$link'> $text </a>", Html.FROM_HTML_MODE_LEGACY)
        else
            Html.fromHtml("<a href='$link'> $text </a>")


    }

}