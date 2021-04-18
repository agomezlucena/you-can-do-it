package com.agomezlucena.youcandoit.data_managament.db

import androidx.room.TypeConverter
import com.agomezlucena.youcandoit.task_managament.Difficulty
import java.time.LocalDate
import java.time.LocalTime

class TaskTypeConverter {
    @TypeConverter
    fun toSecondOfDay(hour:LocalTime) = hour.toSecondOfDay()
    @TypeConverter
    fun fromSecondOfDayToLocalTime(secondOfDay:Int?) = secondOfDay?.let { LocalTime.ofSecondOfDay(it.toLong()) }
    @TypeConverter
    fun toValueRepresentation(difficulty: Difficulty) = difficulty.score
    @TypeConverter
    fun fromScoreRepresentationToDifficulty(score:Int?) = score?.let { Difficulty.values().first { it.score == score } }
    @TypeConverter
    fun toStringRepresentation(date: LocalDate) = date.toString()
    @TypeConverter
    fun fromStringRepresentation(stringRepresentation:String?) = stringRepresentation?.let { LocalDate.parse(it) }
}