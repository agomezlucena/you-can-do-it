package com.agomezlucena.youcandoit.task_managament

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Parcelize
data class Task (
        @PrimaryKey(autoGenerate = true)
        var id : Int? = null,
        var title : String = "",
        var description : String  = "",
        var executionDate: LocalDate = LocalDate.now(),
        var executionHour : LocalTime = LocalTime.now().withSecond(0).withNano(0),
        var difficulty : Difficulty = Difficulty.EASY,
        var finished: Boolean = false
    ) : Parcelable
