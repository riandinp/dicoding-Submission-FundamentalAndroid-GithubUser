package com.dicoding.githubuser.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.response.DetailUserResponse
import com.dicoding.githubuser.ui.adapter.FollowPagerAdapter
import com.dicoding.githubuser.ui.main.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    private val username by lazy { intent.getStringExtra(USERNAME) }

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainViewModel.getDetailUser(username ?: "")
        initPager()
        initObserver()
    }

    private fun initPager() {
        val sectionsPagerAdapter = FollowPagerAdapter(this, username ?: "")
        val viewPager = binding.layoutFollow.vpFollowingFollowers
        val tabs = binding.layoutFollow.tlFollowingFollowers
        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    private fun initObserver() {
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.detailUser.observe(this) {
            setDetailUser(it)
        }
    }

    private fun setDetailUser(user: DetailUserResponse?) {
        with(binding) {
            Glide.with(this.root.context)
                .load(user?.avatarUrl)
                .into(ivProfilePicture)
            tvIdUser.text = getString(R.string.user_id, user?.id.toString())
            tvUsername.text = user?.login
            tvFullName.text = user?.name ?:getString(R.string.no_name)
            tvDescription.text = user?.bio ?:getString(R.string.no_bio)
            tvFollowing.text =
                getString(R.string.following, user?.following ?:0)
            tvFollowers.text =
                getString(R.string.followers, user?.followers ?:0)
        }
    }

    private fun showLoading(value: Boolean) {
        binding.apply {
            pbLoadingScreenDetail.isVisible = value
            clProfileContent.isInvisible = value
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        onBackPressedDispatcher.addCallback(this@DetailUserActivity) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun start(context: Context, username: String) {
            val starter = Intent(context, DetailUserActivity::class.java)
                .putExtra(USERNAME, username)
            context.startActivity(starter)
        }

        private val TAB_TITLES = arrayListOf(
            "Following",
            "Followers"
        )

        private const val USERNAME = "username"
    }
}