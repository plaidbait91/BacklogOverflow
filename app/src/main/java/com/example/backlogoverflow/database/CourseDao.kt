package com.example.backlogoverflow.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao








interface CourseDao {

    @Insert
    fun insertCourse(course: Course)

    @Query(value = "SELECT * FROM courses WHERE id = :id")
    fun getCourse(id: Int): Course?

    @Query(value = "SELECT * FROM courses")
    fun getAllCourses(): LiveData<List<Course>>

    @Query(value = "SELECT * FROM courses WHERE recording_count > 0 ORDER BY deadline")
    fun getPendingCoursesDeadlineSort(): LiveData<List<Course>>

    @Query(value = "SELECT * FROM courses WHERE recording_count > 0 ORDER BY recording_count")
    fun getPendingCoursesRecordingSort(): LiveData<List<Course>>

    @Update
    fun updateCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Query(value = "DELETE FROM courses")
    fun clear()
}