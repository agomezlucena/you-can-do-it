package com.agomezlucena.youcandoit.data_managament.repos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agomezlucena.youcandoit.daysToDay
import com.agomezlucena.youcandoit.getOrAwaitValue
import com.agomezlucena.youcandoit.task_managament.Difficulty
import com.agomezlucena.youcandoit.task_managament.Task
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import kotlin.random.Random

@ExperimentalCoroutinesApi
class TaskRepositoryTest {
    private lateinit var taskRepository : TaskRepository
    private lateinit var listOfTask:List<Task>
    private lateinit var dao :TaskDaoMock

    @get:Rule
    var instantTaskExecutorRule : InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        dao = TaskDaoMock()
        taskRepository = TaskRepository(NotificationWorkerMock(),dao)
        listOfTask = generateTaskList(8, MONDAY, EXECUTION_HOUR,SEED)
        listOfTask.forEach(dao::insert)
    }


    @Test
    fun `check if we obtain a day unfinished task must return a list with the unfinished tasks of that day order by execution hour desc`() = runBlockingTest{
        val expectedValue = listOfTask
                .filter { it.executionDate == MONDAY.plusDays(1) && it.finished.not() }
                .sortedByDescending { it.executionHour }

        val result = taskRepository.getDateUnfinishedTask(MONDAY.plusDays(1)).getOrAwaitValue()

        assertThat(result).isInOrder { task1, task2 ->
            when {
                (task1 as Task).executionHour < (task2 as Task).executionHour -> 1
                task1.executionHour > task2.executionHour -> -1
                else -> 0
            }
        }
        assertThat(result).containsExactlyElementsIn(expectedValue)
    }

    @Test
    fun `check if we obtain today unfinished task must return a list with the unfinished tasks of that day ordered descending by execution hour`() =runBlockingTest{
        val expectedValue = listOfTask
                .filter { it.executionDate == LocalDate.now() && it.finished.not() }
                .sortedByDescending { it.executionHour }
        val result = taskRepository.getTodayUnfinishedTask().getOrAwaitValue()

        assertThat(result).isInOrder { task1, task2 ->
            when {
                (task1 as Task).executionHour < (task2 as Task).executionHour -> 1
                task1.executionHour > task2.executionHour -> -1
                else -> 0
            }
        }

        assertThat(result).containsExactlyElementsIn(expectedValue)
    }

    @Test
    fun `check if we obtain the unfinished tasks from a range of dates it's must return a list of tasks who are ordered by execution date descending`() = runBlockingTest {
        val expectedValue = listOfTask
                .filter {
                    (it.executionDate.isBefore(MONDAY.plusDays(2)) || it.executionDate == MONDAY.plusDays(2)) &&
                    (it.executionDate.isAfter(MONDAY) || it.executionDate == MONDAY) &&
                    it.finished.not()
                }.sortedByDescending { it.executionDate }

        val result = taskRepository.getUnfinishedTaskFromARange(MONDAY, MONDAY.plusDays(2)).getOrAwaitValue()

        assertThat(result).isInOrder{ task1, task2 -> when{
            (task1 as Task).executionDate < (task2 as Task).executionDate -> 1
            task1.executionDate > task2.executionDate -> -1
            else -> 0
        }}

        assertThat(result).containsExactlyElementsIn(expectedValue)
    }

    @Test
    fun `check if we obtain unfinished task from this week return it a list with all task from that range ordered by date desc` () = runBlockingTest{
        val expectedValue = listOfTask
                .filter {
                    it.finished.not() &&
                    (it.executionDate.isBefore(SUNDAY) || it.executionDate == SUNDAY) &&
                    (it.executionDate.isAfter(MONDAY) || it.executionDate == MONDAY)
                }.sortedByDescending { it.executionDate }

        val result = taskRepository.getThisWeekUnfinishedTasks().getOrAwaitValue()

        assertThat(result).isInOrder { task1, task2 ->
            when {
                (task1 as Task).executionDate < (task2 as Task).executionDate -> 1
                task1.executionDate > task2.executionDate -> -1
                else -> 0
            }
        }

        assertThat(result).containsExactlyElementsIn(expectedValue)
    }

    @Test
    fun `check if we obtain unfinished task from this month return it a list with all task from that range ordered by date desc` () = runBlockingTest{
        val firstDayOfMonth = MONDAY.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = MONDAY.with(TemporalAdjusters.lastDayOfMonth())

        val expectedValue = listOfTask.filter {
            it.finished.not() &&
            (it.executionDate.isAfter(firstDayOfMonth) || it.executionDate.isEqual(firstDayOfMonth)) &&
            (it.executionDate.isBefore(lastDayOfMonth) || it.executionDate.isEqual(lastDayOfMonth))
        }

        val result = taskRepository.getThisMonthUnfinishedTasks().getOrAwaitValue()

        assertThat(result).isInOrder { task1, task2 ->
            when {
                (task1 as Task).executionDate < (task2 as Task).executionDate -> 1
                task1.executionDate > task2.executionDate -> -1
                else -> 0
            }
        }

        assertThat(result).containsExactlyElementsIn(expectedValue)

    }

    @Test
    fun `check if we obtain today accomplished score returns the correct accomplished score for today`() = runBlockingTest{
        val expectedValue = listOfTask
                .filter { it.finished && it.executionDate == TODAY }
                .map { it.difficulty.score }
                .sum()
                .toLong()
        val result = taskRepository.getTodayAccomplishedScore().getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }

    @Test
    fun `check if we obtain today total score returns the correct totalScore for today`(){
        val expectedValue = listOfTask.filter { it.executionDate == TODAY }
                .map{it.difficulty.score}
                .sum()
                .toLong()
        val result = taskRepository.getTodayTotalScore().getOrAwaitValue()

        assertThat(result).isEqualTo(expectedValue)
    }

    @Test
    fun `check if we obtain this week accomplished score return the correct accomplished score for this week`() = runBlockingTest{
        val expectedValue = listOfTask
                .filter { it.finished &&
                        (it.executionDate.isBefore(SUNDAY) || it.executionDate == SUNDAY) &&
                        (it.executionDate.isAfter(MONDAY) || it.executionDate == MONDAY)
                }.map { it.difficulty.score }
                .sum().toLong()
        val result = taskRepository.getThisWeekAccomplishedScore().getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }
    @Test
    fun `check if we obtain this week total score return the correct total score for this week`()= runBlockingTest{
        val expectedValue = listOfTask
                .filter { (it.executionDate.isBefore(SUNDAY) || it.executionDate == SUNDAY) &&
                        (it.executionDate.isAfter(MONDAY) || it.executionDate == MONDAY)
                }.map { it.difficulty.score }
                .sum().toLong()
        val result = taskRepository.getThisWeekTotalScore().getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }
    @Test
    fun `check if we obtain this month accomplished score return the correct accomplished score for this month`() = runBlockingTest{
        val lastDayOfMonth = TODAY.with(TemporalAdjusters.lastDayOfMonth())
        val firstDayOfMonth = TODAY.with(TemporalAdjusters.firstDayOfMonth())

        val expectedValue = listOfTask
                .filter {
                    it.finished &&
                    (it.executionDate.isBefore(lastDayOfMonth) || it.executionDate == lastDayOfMonth) &&
                    (it.executionDate.isAfter(firstDayOfMonth) || it.executionDate == firstDayOfMonth)
                }.map { it.difficulty.score }
                .sum().toLong()

        val result = taskRepository.getThisMonthAccomplishedScore().getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }
    @Test
    fun `check if we obtain this month total score return the total score for this month`() = runBlockingTest{
        val lastDayOfMonth = TODAY.with(TemporalAdjusters.lastDayOfMonth())
        val firstDayOfMonth = TODAY.with(TemporalAdjusters.firstDayOfMonth())

        val expectedValue = listOfTask
                .filter {
                    (it.executionDate.isBefore(lastDayOfMonth) || it.executionDate == lastDayOfMonth) &&
                    (it.executionDate.isAfter(firstDayOfMonth) || it.executionDate == firstDayOfMonth)
                }.map { it.difficulty.score }
                .sum().toLong()

        val result = taskRepository.getThisMonthTotalScore().getOrAwaitValue()
        assertThat(result).isEqualTo(expectedValue)
    }
    /**
     * Generates a list of task that half are finished
     */
    private fun generateTaskList(quantityOfDays:Int, executionDate: LocalDate, executionHour: LocalTime, seed:Long) :List<Task>{
        var id = 0
        val random = Random(seed)
        return (0 until quantityOfDays).flatMap { day ->
            (0..9).map{ hour ->
                Task(id++,"prueba","prueba",executionDate.plusDays(day.toLong()),executionHour.plusHours(hour.toLong()), Difficulty.EASY,random.nextBoolean() )
            }
        }
    }

    companion object{
        private val TODAY = LocalDate.now()
        private val MONDAY = TODAY.plusDays(TODAY.daysToDay(DayOfWeek.MONDAY))
        private val SUNDAY = MONDAY.plusDays(6)
        private val EXECUTION_HOUR = LocalTime.of(10,0)
        private const val SEED = 350854L
    }


}