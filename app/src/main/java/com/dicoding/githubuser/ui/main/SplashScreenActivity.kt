package com.dicoding.githubuser.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.githubuser.databinding.ActivitySplashScreenBinding
import com.dicoding.githubuser.ui.BaseActivity
import com.dicoding.githubuser.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        activityScope.launch {
            delay(Constants.DELAY_SPLASH_SCREEN)
            runOnUiThread {
                MainActivity.start(this@SplashScreenActivity)
                finish()
            }
        }
    }

    override fun getViewBinding(): ActivitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
}