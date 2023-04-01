package com.dicoding.githubuser.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.repository.FavoriteUserRepository

class DetailUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun isFavorite(userId: String): LiveData<Boolean> =
        mFavoriteUserRepository.isFavorite(userId)
}