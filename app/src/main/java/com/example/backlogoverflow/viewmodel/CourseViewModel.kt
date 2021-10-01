package com.example.backlogoverflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.backlogoverflow.database.Course
import com.example.backlogoverflow.database.CourseDao
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.util.*

class CourseViewModel(val database: CourseDao): ViewModel() {
    var list: LiveData<List<Course>> = database.getAllCourses()

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)


    fun addCourse(course: Course) {
        uiScope.launch {
            insert(course)
        }
    }

    private suspend fun insert(entry: Course) {
        withContext(Dispatchers.IO) {
            database.insertCourse(entry)
        }
    }

    fun editCourse(course: Course) {
        uiScope.launch {
            update(course)
        }
    }

    private suspend fun update(course: Course) {
        withContext(Dispatchers.IO) {
            database.updateCourse(course)
        }
    }

    fun clearCourses() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.clear()
            }
        }
    }

    fun deleteCourse(course: Course) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteCourse(course)
            }
        }
    }
}