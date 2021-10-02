package com.example.backlogoverflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.backlogoverflow.database.Course
import com.example.backlogoverflow.database.CourseDao
import kotlinx.coroutines.*

class PendingViewModel(val database: CourseDao): ViewModel() {
    var list: LiveData<List<Course>> = database.getPendingCoursesRecordingSort()
    var selectedIndex = MutableLiveData(0)

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun recordingSort() {
        list = database.getPendingCoursesRecordingSort()
        selectedIndex.value = 0
    }

    fun deadlineSort() {
        list = database.getPendingCoursesDeadlineSort()
        selectedIndex.value = 1
    }


}