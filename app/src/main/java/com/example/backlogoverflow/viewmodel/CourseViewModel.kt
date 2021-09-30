package com.example.backlogoverflow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.backlogoverflow.database.Course
import com.example.backlogoverflow.database.CourseDao
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.util.*

class CourseViewModel(val database: CourseDao): ViewModel() {
    private var _list = MutableLiveData<List<Course>>()
    val list: MutableLiveData<List<Course>>
    get() = _list

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    init {
        loadCourses()
    }

    private fun loadCourses() {
        uiScope.launch {
            _list.value = query()
        }
    }

    private suspend fun query(): List<Course>? {
        return withContext(Dispatchers.IO) {
            database.getAllCourses()
        }
    }

    fun addCourse(course: Course) {
        uiScope.launch {
            insert(course)
            loadCourses()
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
            loadCourses()
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
                loadCourses()
            }
        }
    }

    fun deleteCourse(course: Course) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteCourse(course)
                loadCourses()
            }
        }
    }
}