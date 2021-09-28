package com.example.backlogoverflow.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.Types.newParameterizedType
import java.lang.reflect.ParameterizedType
import java.sql.Date

class Converters {

    private val moshi = Moshi.Builder().build()

    private val listDate : ParameterizedType = newParameterizedType(List::class.java, Long::class.javaObjectType)
    private val listDateJsonAdapter: JsonAdapter<List<Long>> = moshi.adapter(listDate)

    private val listString : ParameterizedType = newParameterizedType(List::class.java, String::class.java)
    private val listStringJsonAdapter: JsonAdapter<List<String>> = moshi.adapter(listString)


    @TypeConverter
    fun listDateToJsonStr(listMyModel: List<Long>?): String? {
        return listDateJsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListDate(jsonStr: String?): List<Long>? {
        return jsonStr?.let { listDateJsonAdapter.fromJson(jsonStr) }
    }

    @TypeConverter
    fun listStringToJsonStr(listMyModel: List<String>?): String? {
        return listStringJsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListString(jsonStr: String?): List<String>? {
        return jsonStr?.let { listStringJsonAdapter.fromJson(jsonStr) }
    }
}