package com.example.meet.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewEventViewModelFactory(private val application: Application, private val eventId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ViewEventViewModel::class.java)) return ViewEventViewModel(application, eventId) as T
        throw IllegalArgumentException("ViewEventViewModel not found")
    }
}