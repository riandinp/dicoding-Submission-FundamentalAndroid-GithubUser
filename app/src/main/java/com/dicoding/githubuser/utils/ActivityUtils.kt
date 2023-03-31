package com.dicoding.githubuser.utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.isDarkModeOn(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}