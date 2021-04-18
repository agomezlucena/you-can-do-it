package com.agomezlucena.youcandoit.data_managament.db

import com.agomezlucena.youcandoit.task_managament.Difficulty
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TaskTypeConverterTest {
    private val converter = TaskTypeConverter()

    @Test
    fun `when hour passed return the correct quantity of seconds`(){
        val expectedSeconds = 55800
        assertEquals(expectedSeconds,converter.toSecondOfDay(LocalTime.of(15,30)))
    }

    @Test
    fun `when you pass a null quantityOfSecond return null`(){
        assertNull(converter.fromSecondOfDayToLocalTime(null))
    }

    @Test
    fun `when you pass a quantity of seconds must return the equivalent LocalTime`(){
        val expectedTime = LocalTime.of(15,30)
        val quantityOfSecond = expectedTime.toSecondOfDay()
        assertEquals(expectedTime,converter.fromSecondOfDayToLocalTime(quantityOfSecond))
    }

    @Test
    fun `when you pass a Difficulty must return its equivalent score`(){
        val difficulty = Difficulty.EASY
        val expectedScore = difficulty.score
        assertEquals(expectedScore,converter.toValueRepresentation(difficulty))
    }

    @Test
    fun `when you pass a ordinal value must return his equivalent Difficulty` (){
        val expectedDifficulty = Difficulty.EASY
        val score = expectedDifficulty.score
        assertEquals(expectedDifficulty,converter.fromScoreRepresentationToDifficulty(score))
    }

    @Test
    fun `when you pass a null ordinal value must return a null`(){
        assertNull(converter.fromScoreRepresentationToDifficulty(null))
    }

    @Test
    fun `when a LocalDate is passed must return a its string representation` (){
        val date = LocalDate.of(2021,1,21)
        val expectedValue = date.toString()
        assertEquals(expectedValue,converter.toStringRepresentation(date))
    }

    @Test
    fun `when a null is passed must return null`(){
        assertNull(converter.fromStringRepresentation(null))
    }

    @Test
    fun `when a string is passed must return its equivalent LocalDate`(){
        val expectedDate = LocalDate.of(2021,1,21)
        assertEquals(expectedDate,converter.fromStringRepresentation(expectedDate.toString()))
    }
}