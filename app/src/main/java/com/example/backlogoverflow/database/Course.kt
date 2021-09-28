package com.example.backlogoverflow.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "course_name")
    val courseName: String,

    @ColumnInfo(name = "lecture_timings")
    val timings: List<Long>,

    @ColumnInfo(name = "lecture_links")
    val links: List<String>,

    @ColumnInfo(name = "recording_count")
    val count: Int = 0,

    @ColumnInfo(name = "deadline")
    val deadline: Long

)