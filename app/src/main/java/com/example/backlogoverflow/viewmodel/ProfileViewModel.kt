package com.example.backlogoverflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProfileViewModel {
    private val _darkTheme = MutableLiveData<Boolean>()
    val darkTheme: LiveData<Boolean>
        get() = _darkTheme

    fun darkTheme() {
        _darkTheme.value = true
    }

    fun lightTheme() {
        _darkTheme.value = false
    }
}