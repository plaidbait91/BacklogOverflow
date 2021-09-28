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

    fun addCourse() {
        uiScope.launch {
            val test = Course(
                courseName = "testing",
                timings = listOf(
                    (System.currentTimeMillis()),
                    (System.currentTimeMillis() + 84600000L),
                    (System.currentTimeMillis() + 42300000L)),
                deadline = (System.currentTimeMillis()),
                links = listOf())
            insert(test)
            loadCourses()
        }
    }

    private suspend fun insert(test: Course) {
        withContext(Dispatchers.IO) {
            database.insertCourse(test)
        }
    }
}