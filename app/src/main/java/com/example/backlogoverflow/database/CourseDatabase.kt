package com.example.backlogoverflow.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Course::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CourseDatabase : RoomDatabase(){
    abstract val courseDao: CourseDao

    companion object {

        @Volatile
        private var INSTANCE: CourseDatabase? = null

        fun getInstance(context: Context): CourseDatabase {

            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CourseDatabase::class.java,
                        "weather_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}