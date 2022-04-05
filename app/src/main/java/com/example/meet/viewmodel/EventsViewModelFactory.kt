package com.example.meet.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class EventsViewModelFactory (private var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventsViewModel::class.java)){
            return EventsViewModel(application) as T
        }
        throw IllegalArgumentException("EventsViewModel not found")
    }

}