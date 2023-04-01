package com.dicoding.githubuser.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuser.preferences.FavoriteUserViewModelFactory
import com.dicoding.githubuser.ui.BaseActivity
import com.dicoding.githubuser.ui.adapter.ListFavoriteUserAdapter
import com.dicoding.githubuser.ui.detail.DetailUserActivity
import com.dicoding.githubuser.ui.settings.SettingsActivity

class FavoriteUserActivity : BaseActivity<ActivityFavoriteUserBinding>(), ListFavoriteUserAdapter.OnUserFavoriteClick {

    private val detailViewModel by viewModels<FavoriteUserViewModel> { FavoriteUserViewModelFactory.getInstance(application) }
    private lateinit var adapter: ListFavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        detailViewModel.getAllFavoriteUser().observe(this) { favUserList ->
            if (favUserList != null) {
                adapter.setListFavorite(favUserList)
                binding.tvNoDataUser.isVisible = favUserList.isEmpty()
            } else {
                binding.tvNoDataUser.isVisible = true
            }
        }

        adapter = ListFavoriteUserAdapter(this)

        binding.rvListUser.apply {
            val manager = LinearLayoutManager(this@FavoriteUserActivity)
            val decoration = DividerItemDecoration(this@FavoriteUserActivity, manager.orientation)
            adapter = this@FavoriteUserActivity.adapter
            layoutManager = manager
            addItemDecoration(decoration)
        }
        supportActionBar?.title = getString(R.string.favorite_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        menu.removeItem(R.id.search)
        menu.removeItem(R.id.favorite)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        } else if (item.itemId == R.id.setting) {
            SettingsActivity.start(this@FavoriteUserActivity)
        }
        onBackPressedDispatcher.addCallback(this@FavoriteUserActivity) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewBinding(): ActivityFavoriteUserBinding =
        ActivityFavoriteUserBinding.inflate(layoutInflater)

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, FavoriteUserActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onUserFavoriteClick(username: String) {
        DetailUserActivity.start(this, username)
    }
}