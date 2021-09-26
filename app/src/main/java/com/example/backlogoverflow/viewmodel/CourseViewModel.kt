package com.example.backlogoverflow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.backlogoverflow.database.CourseDao

class CourseViewModel(database: CourseDao): ViewModel() {
    private var _list = MutableLiveData<List<String>>()
    val list: MutableLiveData<List<String>>
    get() = _list

    init {
        _list.value = mutableListOf<String>("one", "two", "three", "four")
    }
}