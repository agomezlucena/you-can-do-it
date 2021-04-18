package com.agomezlucena.youcandoit.data_managament.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.agomezlucena.youcandoit.task_managament.Task

@Database(version = 1, entities = [Task::class])
@TypeConverters(TaskTypeConverter::class)
abstract class YouCanDoITDatabase : RoomDatabase() {
    abstract fun taskDao():TaskDao
}