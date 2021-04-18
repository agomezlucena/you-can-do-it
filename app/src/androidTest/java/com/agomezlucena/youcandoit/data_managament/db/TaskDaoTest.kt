package com.agomezlucena.youcandoit.data_managament.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.Difficulty
import com.agomezlucena.youcandoit.task_managament.Task
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTestTest
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TaskDaoTest {
    companion object{
        private val EXECUTION_DATE = LocalDate.of(2021,1,1)
        private val EXECUTION_HOUR = LocalTime.of(10,0)
        private const val SEED = 350854L
    }

    @get:Rule
    var instantTaskExecutorRule : InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db : YouCanDoITDatabase
    private lateinit var taskDao : TaskDao
    private lateinit var listOfTask: List<Task>

    @Before
    fun createDB(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context,YouCanDoITDatabase::class.java).build()
        taskDao = db.taskDao()
        listOfTask = generateTaskList(8, EXECUTION_DATE, EXECUTION_HOUR, SEED)
        listOfTask.forEach(taskDao::insert)
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    fun writeNewTaskToDbAndGetThisNewTask() = runBlockingTestTest {
        val expectedTask = Task(listOfTask[listOfTask.lastIndex].id!!+1,"titulo", description = "descripcion", executionDate = LocalDate.now(), executionHour =  LocalTime.of(15,30), difficulty = Difficulty.EASY)
        taskDao.insert(expectedTask)
        val obtainedTask = taskDao.findById(expectedTask.id!!).getOrAwaitValue()
        assertThat(obtainedTask).isEqualTo(expectedTask)
    }

    @Test
    fun updatingAnExistingTaskMustChangeItsValues () = runBlockingTestTest {
        val taskToUpdate = Task(1, "titulo", "descripcion", LocalDate.now(), LocalTime.of(15,30), Difficulty.EASY)
        taskDao.insert(taskToUpdate)
        taskToUpdate.title = "titulo2"
        taskDao.insert(taskToUpdate)
        val obtainedTask = taskDao.findById(taskToUpdate.id!!).getOrAwaitValue()
        assertNotEquals(obtainedTask.title,"titulo")
    }

    @Test
    fun whenWeObtainTotalScoreOfDifferentDaysMustReturnItOrderedByExecutionDate() = runBlockingTestTest{
        val listOfExpectedDayScore = listOfTask.filter {
            (it.executionDate == EXECUTION_DATE || it.executionDate.isAfter(EXECUTION_DATE)) &&
            (it.executionDate == EXECUTION_DATE.plusDays(6) || it.executionDate.isBefore(EXECUTION_DATE.plusDays(6)))
        }.groupBy {
            it.executionDate
        }.map { dayScore ->
            val sum = dayScore.value.map { it.difficulty.score }.sum().toLong()
            DayScore(dayScore.key,sum)
        }.sortedByDescending { it.executionDate }


        val result = taskDao.getTotalScorePerDaysInRange(EXECUTION_DATE, EXECUTION_DATE.plusDays(6)).getOrAwaitValue()

        assertThat(result).isInOrder{
            dayScore1,dayScore2 -> (dayScore1 as DayScore).executionDate.compareTo((dayScore2 as DayScore).executionDate)
        }

        assertThat(result).containsExactlyElementsIn(listOfExpectedDayScore)
    }

    @Test
    fun whenWeObtainAcomplishedScoreOfDifferentsDaysMustReturnItOrderedByExecutionDate() = runBlockingTestTest {
        val listOfExpectedDayScore = listOfTask.filter {
            it.finished &&
            (it.executionDate == EXECUTION_DATE || it.executionDate.isAfter(EXECUTION_DATE)) &&
            (it.executionDate == EXECUTION_DATE.plusDays(6) || it.executionDate.isBefore(EXECUTION_DATE.plusDays(6)))
        }.groupBy {
            it.executionDate
        }.map { dayScore ->
            val sum = dayScore.value.map { it.difficulty.score }.sum().toLong()
            DayScore(dayScore.key,sum)
        }.sortedByDescending { it.executionDate }

        val result = taskDao.getAccomplishedScorePerDaysInRange(EXECUTION_DATE, EXECUTION_DATE.plusDays(6)).getOrAwaitValue()

        assertThat(result).containsExactlyElementsIn(listOfExpectedDayScore)
    }

    @Test
    fun checkIfWeObtainAllUnfinishedTaskFromADayReturnsOnlyUnfinishedOrdered() = runBlockingTestTest {

        val allTask = generateTaskList(7,EXECUTION_DATE,EXECUTION_HOUR,SEED)
        allTask.forEach(taskDao::insert)

        val expectedTask = allTask
                .filter { !it.finished && it.executionDate == EXECUTION_DATE }
                .sortedByDescending { it.executionHour }

        val result = taskDao.getUnfinishedTaskFromADay(EXECUTION_DATE).getOrAwaitValue()

        assertThat(result).isInOrder { task1, task2 -> when {
                (task1 as Task).executionHour < (task2 as Task).executionHour -> 1
                task1.executionHour > task2.executionHour -> -1
                else -> 0
            }
        }

        assertThat(result).containsExactlyElementsIn(expectedTask)
    }

    @Test
    fun checkIfWeObtainAllUnfinishedTaskFromARangeOfDatesOrderByExecutionDate() = runBlockingTestTest {
        val generatedList = generateTaskList(30, EXECUTION_DATE, EXECUTION_HOUR,SEED)
        generatedList.forEach(taskDao::insert)
        val expectedList = generatedList.filter { !it.finished }

        val result = taskDao.getUnfinishedTaskFromARangeOfDates(EXECUTION_DATE, EXECUTION_DATE.plusDays(29)).getOrAwaitValue()

        assertThat(result).isInOrder { task1, task2 -> when {
                (task1 as Task).executionDate < (task2 as Task).executionDate -> 1
                task1.executionDate > task2.executionDate -> -1
                else -> 0
            }
        }

        assertThat(result).containsExactlyElementsIn(expectedList)
    }

    @Test
    fun checkIfWeObtainADayTotalScoreReturnsTheCorrectValue() = runBlockingTestTest {
        val generatedList = generateTaskList(2, EXECUTION_DATE, EXECUTION_HOUR,SEED)
        val expectedValue = generatedList
                .filter { it.executionDate == EXECUTION_DATE }
                .map { it.difficulty.score }
                .reduce {acc,it -> acc + it}.toLong()

        generatedList.forEach(taskDao::insert)
        val result = taskDao.getTotalScoreFromADay(EXECUTION_DATE).getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }

    @Test
    fun checkIfWeObtainADayAccomplishedScoreReturnsTheCorrectValue()= runBlockingTestTest {
        val generatedList = generateTaskList(2, EXECUTION_DATE, EXECUTION_HOUR,SEED)
        val expectedValue = generatedList
                .filter { it.executionDate == EXECUTION_DATE && it.finished }
                .map { it.difficulty.score }
                .reduce{acc,it -> acc + it}.toLong()

        generatedList.forEach(taskDao::insert)

        val result = taskDao.getAccomplishedScoreFromADay(EXECUTION_DATE).getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }

    @Test
    fun whenWeObtainTotalScoreFromARangeOfDatesReturnTheCorrectValue()= runBlockingTestTest {
        val generatedList = generateTaskList(10, EXECUTION_DATE, EXECUTION_HOUR,SEED)
        val expectedValue = generatedList
                .filter {
                    it.executionDate.isAfter(EXECUTION_DATE.minusDays(1L)) &&
                            it.executionDate.isBefore(EXECUTION_DATE.plusDays(7L))
                }.map { it.difficulty.score }
                .reduce{acc,it -> acc+it}.toLong()

        generatedList.forEach(taskDao::insert)
        val result = taskDao.getTotalScoreFromARange(EXECUTION_DATE, EXECUTION_DATE.plusDays(6L)).getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }

    /**
     * Generates a list of task that half are finished
     */
    fun generateTaskList(quantityOfDays:Int,executionDate:LocalDate,executionHour:LocalTime,seed:Long) = run {
        var id = 0
        val random = Random(seed)
        (0 until quantityOfDays).flatMap { day ->
            (0..9).map{ hour ->
                Task(id++,"prueba","prueba",executionDate.plusDays(day.toLong()),executionHour.plusHours(hour.toLong()),Difficulty.EASY,random.nextBoolean() )
            }
        }
    }
}