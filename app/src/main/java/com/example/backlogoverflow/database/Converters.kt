package com.example.backlogoverflow.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.Types.newParameterizedType
import java.lang.reflect.ParameterizedType
import java.sql.Date
import java.time.LocalTime

class Converters {

    private val moshi = Moshi.Builder().build()

    private val listString : ParameterizedType = newParameterizedType(MutableList::class.java, String::class.java)
    private val listStringJsonAdapter: JsonAdapter<List<String>> = moshi.adapter(listString)

    @TypeConverter
    fun listStringToJsonStr(listMyModel: List<String>?): String? {
        return listStringJsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListString(jsonStr: String?): List<String>? {
        return jsonStr?.let { listStringJsonAdapter.fromJson(jsonStr) }
    }
}