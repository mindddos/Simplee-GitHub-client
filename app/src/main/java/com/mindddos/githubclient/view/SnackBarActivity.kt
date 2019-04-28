package com.mindddos.githubclient.view

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mindddos.githubclient.R

open class SnackBarActivity : AppCompatActivity() {
    protected var snackBar: Snackbar? = null

    protected fun showRetrySnackBar(rootView: View, text: String, body: () -> Unit) {
        snackBar = Snackbar.make(rootView, text, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) { body() }
        snackBar?.show()
    }

}