package com.example.backlogoverflow.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime
import java.util.*

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "course_name")
    var courseName: String,

    @ColumnInfo(name = "lecture_timings")
    var timings: MutableList<String?>,

    @ColumnInfo(name = "lecture_links")
    var links: MutableList<String>,

    @ColumnInfo(name = "recording_count")
    var count: Int = 0,

    @ColumnInfo(name = "deadline")
    var deadline: Long

)