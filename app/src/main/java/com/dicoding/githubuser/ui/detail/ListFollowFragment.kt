package com.dicoding.githubuser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.remote.response.UserItem
import com.dicoding.githubuser.databinding.FragmentListFollowBinding
import com.dicoding.githubuser.ui.adapter.ListUserAdapter
import com.dicoding.githubuser.ui.main.MainViewModel

class ListFollowFragment : Fragment(), ListUserAdapter.OnUserItemClick {

    private lateinit var binding: FragmentListFollowBinding

    private val username by lazy { arguments?.getString(USERNAME) }
    private val page by lazy { arguments?.getInt(PAGE, 0) }

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()

        when (page) {
            1 -> mainViewModel.getFollowing(username ?: "")
            2 -> mainViewModel.getFollowers(username ?: "")
        }
    }

    private fun initObserver() {
        mainViewModel.listFollowing.observe(viewLifecycleOwner) {
            setListFollowing(it)
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        mainViewModel.listFollowers.observe(viewLifecycleOwner) {
            setListFollowing(it)
        }
        mainViewModel.toastText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(value: Boolean) {
        binding.apply {
            pbLoadingScreen.isVisible = value
            rvListUser.isVisible = !value
            tvEmptyState.isVisible = !value
        }
    }

    private fun setListFollowing(list: List<UserItem>?) {
        with(binding) {
            if (list.isNullOrEmpty()) {
                tvEmptyState.apply {
                    text = when (page) {
                        1 -> getString(R.string.no_following)
                        2 -> getString(R.string.no_followers)
                        else -> getString(R.string.no_data)
                    }
                    isVisible = true
                }
                initAdapter(listOf())

            } else {
                tvEmptyState.isVisible = false
                initAdapter(list)
            }
        }
    }

    private fun initAdapter(newList: List<UserItem>) {
        binding.rvListUser.apply {
            adapter = ListUserAdapter(newList, this@ListFollowFragment)
            val manager = LinearLayoutManager(requireActivity())
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(requireActivity(), manager.orientation))
        }
    }

    override fun onUserItemClick(username: String) {
        DetailUserActivity.start(requireContext(), username)
    }

    companion object {
        fun newInstance(page: Int, username: String): ListFollowFragment {
            val args = Bundle().apply {
                putString(USERNAME, username)
                putInt(PAGE, page)
            }
            val fragment = ListFollowFragment()
            fragment.arguments = args
            return fragment
        }

        private const val USERNAME = "username"
        private const val PAGE = "page"
    }
}