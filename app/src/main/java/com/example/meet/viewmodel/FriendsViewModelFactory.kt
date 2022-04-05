package com.example.meet.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class FriendsViewModelFactory(private var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FriendsViewModel::class.java)){
            return FriendsViewModel(application) as T
        }
        throw IllegalArgumentException("FriendsViewModel not found")
    }
}