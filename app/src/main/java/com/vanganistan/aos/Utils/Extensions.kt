package com.vanganistan.aos.Utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_start.*

fun Activity.setStatusBarTransparent(color: Int? = null) {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = color ?: Color.TRANSPARENT
    }
}

fun Activity.showLoading(show: Boolean){
    progress.isVisible = show
}

fun View.hideKeyboard() {
    val imm = ContextCompat.getSystemService(
        this.context,
        InputMethodManager::class.java
    ) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}
