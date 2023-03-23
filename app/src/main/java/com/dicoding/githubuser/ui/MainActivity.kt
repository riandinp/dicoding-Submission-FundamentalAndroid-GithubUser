package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.response.UserItem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.tvWelcome.isVisible = true

        initObserver()
        initAdapter()
        initListener()
    }

    private fun initListener() {
        binding.edSearchUser.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.tvWelcome.isVisible = false
                mainViewModel.postSearchuser(binding.edSearchUser.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListUser.addItemDecoration(itemDecoration)
    }

    private fun initObserver() {
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.listSearch.observe(this) {
            setListUsers(it)
        }
    }

    private fun setListUsers(listUsers: List<UserItem>?) {

        if (listUsers != null) {
            val adapter = ListUserAdapter(listUsers)
            binding.rvListUser.adapter = adapter

            binding.rvListUser.isVisible = true
            binding.tvNoDataUser.isVisible = false
        } else {
            binding.tvNoDataUser.isVisible = true
        }
    }

    private fun showLoading(value: Boolean) {
        binding.pbLoadingScreen.isVisible = value
    }
}