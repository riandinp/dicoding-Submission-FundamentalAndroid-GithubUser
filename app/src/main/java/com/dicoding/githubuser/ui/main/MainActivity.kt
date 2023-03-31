package com.dicoding.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.response.UserItem
import com.dicoding.githubuser.ui.BaseActivity
import com.dicoding.githubuser.ui.adapter.ListUserAdapter
import com.dicoding.githubuser.ui.detail.DetailUserActivity
import com.dicoding.githubuser.ui.settings.SettingsActivity

class MainActivity : BaseActivity<ActivityMainBinding>(), ListUserAdapter.OnUserItemClick {

    private val mainViewModel by viewModels<MainViewModel>()


    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvWelcome.isVisible = true
        initObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService<SearchManager>()
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.queryHint = resources.getString(R.string.search_user_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.postSearchuser(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            SettingsActivity.start(this@MainActivity)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initObserver() {
        mainViewModel.isLoading.observe(this) {
            binding.tvWelcome.isVisible = false
            showLoading(it)
        }
        mainViewModel.listSearch.observe(this) {
            setListUsers(it)
        }
        mainViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                showToast(toastText)
            }
        }
    }

    private fun setListUsers(listUsers: List<UserItem>?) {
        with(binding) {
            if (listUsers.isNullOrEmpty()) {
                tvNoDataUser.isVisible = true
                rvListUser.isVisible = false
            } else {
                tvNoDataUser.isVisible = false
                val manager = LinearLayoutManager(this@MainActivity)
                val itemDecoration = DividerItemDecoration(this@MainActivity, manager.orientation)
                rvListUser.apply {
                    adapter = ListUserAdapter(listUsers, this@MainActivity)
                    layoutManager = manager
                    addItemDecoration(itemDecoration)
                }
            }
        }
    }

    private fun showLoading(value: Boolean) {
        with(binding) {
            pbLoadingScreen.isVisible = value
            rvListUser.isVisible = !value
            tvNoDataUser.isVisible = !value
        }

    }

    override fun onUserItemClick(username: String) {
        DetailUserActivity.start(this@MainActivity, username)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }
}