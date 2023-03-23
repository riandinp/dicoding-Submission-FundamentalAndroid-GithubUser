package com.dicoding.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.ApiConfig
import com.dicoding.githubuser.response.SearchUserResponse
import com.dicoding.githubuser.response.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _listSearch = MutableLiveData<List<UserItem>>()
    val listSearch: LiveData<List<UserItem>> = _listSearch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postSearchuser(userName: String) {
        _isLoading.value = true

        val cilent = ApiConfig.getApiService().searchUser(userName)
        cilent.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listSearch.value = responseBody.items
                } else {
                    Log.e(TAG, "onFailure2: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}