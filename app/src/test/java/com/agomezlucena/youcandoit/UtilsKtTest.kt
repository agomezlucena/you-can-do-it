package com.agomezlucena.youcandoit

import com.agomezlucena.youcandoit.task_managament.Difficulty
import com.agomezlucena.youcandoit.task_managament.Task
import org.junit.Test

import org.junit.Assert.*
import java.time.*
import java.util.*

class UtilsKtTest {

    @Test
    fun daysToDayTest() {
        val expectedValue = -3L
        val result = LocalDate.of(2021,2,11).daysToDay(DayOfWeek.MONDAY)
        assertEquals(expectedValue,result)
    }

    @Test
    fun toCalendar() {
        val expectedValue = Calendar.getInstance()
        expectedValue.set(Calendar.YEAR,2021)
        expectedValue.set(Calendar.MONTH, 1)
        expectedValue.set(Calendar.DAY_OF_MONTH,8)
        expectedValue.set(Calendar.MILLISECOND,0)
        val result = LocalDate.of(2021,2,8).toCalendar()
        assertEquals(expectedValue,result)
    }

    @Test
    fun toDateTimePair(){
        val expectedValue = Pair(LocalDate.of(2021,2,8),LocalTime.of(10,30,0,0))
        val result = LocalDateTime.of(2021,2,8,10,30).toDateTimePair()
        assertEquals(expectedValue,result)
    }

    @Test
    fun fromCalendar(){
        val expectedValue = LocalDateTime.of(2021,2,8,13,30,0,0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR,expectedValue.year)
        calendar.set(Calendar.MONTH,expectedValue.monthValue-1)
        calendar.set(Calendar.DAY_OF_MONTH,expectedValue.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY,expectedValue.hour)
        calendar.set(Calendar.MINUTE,expectedValue.minute)

        val result = calendar.toLocalDateTime()

        assertEquals(expectedValue,result)
    }

    @Test
    fun `check we generate a Task with all fields using builder must return the expected task`(){
        val expectedValue = Task(1,
                "prueba",
                "prueba",
                LocalDate.of(2021,1,1),
                LocalTime.of(10,30),
                Difficulty.EASY,
                false)

        val result = task {
            id=1
            title = "prueba"
            description = "prueba"
            executionDate = LocalDate.of(2021,1,1)
            executionHour = LocalTime.of(10,30)
            difficulty = Difficulty.EASY
            finished = false
        }

        assertEquals(result,expectedValue)
    }

    @Test
    fun `check if we generate a empty task must return a task with all default value`(){
        val expectedValue = Task()
        val result = task{}
        assertEquals(result,expectedValue)
    }

    @Test
    fun `check if we generate a task with some field empty must return a task with the default value of that field`(){
        val expectedTask = Task(
                title="prueba",
                description = "prueba"
        )

        val result = task{
            title = "prueba"
            description = "prueba"
        }

        assertEquals(result, expectedTask)
    }

}