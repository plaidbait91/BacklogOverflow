package com.example.backlogoverflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.backlogoverflow.database.Course
import com.example.backlogoverflow.database.CourseDao
import kotlinx.coroutines.*

class PendingViewModel(val database: CourseDao): ViewModel() {
    var list: LiveData<List<Course>> = database.getPendingCoursesRecordingSort()

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}