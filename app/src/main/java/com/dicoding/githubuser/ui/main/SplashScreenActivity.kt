package com.dicoding.githubuser.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.githubuser.databinding.ActivitySplashScreenBinding
import com.dicoding.githubuser.preferences.SettingPreferences
import com.dicoding.githubuser.preferences.SettingViewModelFactory
import com.dicoding.githubuser.preferences.dataStore
import com.dicoding.githubuser.ui.BaseActivity
import com.dicoding.githubuser.ui.settings.SettingViewModel
import com.dicoding.githubuser.utils.Constants
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel by viewModels<SettingViewModel> { SettingViewModelFactory(pref) }
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        activityScope.launch {
            delay(Constants.DELAY_SPLASH_SCREEN)
            runOnUiThread {
                MainActivity.start(this@SplashScreenActivity)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.coroutineContext.cancelChildren()
    }

    override fun getViewBinding(): ActivitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
}