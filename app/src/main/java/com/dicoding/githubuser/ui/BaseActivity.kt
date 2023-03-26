package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract fun getViewBinding(): VB

    private var _binding: VB? = null
    protected val binding: VB
        get() {
            if (_binding != null) return _binding as VB
            else _binding = getViewBinding()
            return _binding as VB
        }

    private fun getLayoutResource(): View = binding.root

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(getLayoutResource())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}