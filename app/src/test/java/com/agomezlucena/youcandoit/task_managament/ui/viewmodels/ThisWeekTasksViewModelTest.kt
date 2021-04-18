package com.agomezlucena.youcandoit.task_managament.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agomezlucena.youcandoit.data_managament.db.TaskDao
import com.agomezlucena.youcandoit.data_managament.repos.NotificationWorkerMock
import com.agomezlucena.youcandoit.data_managament.repos.TaskDaoMock
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.daysToDay
import com.agomezlucena.youcandoit.getOrAwaitValue
import com.agomezlucena.youcandoit.task_managament.Difficulty
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.Task
import com.agomezlucena.youcandoit.task_managament.TasksPerDay
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ThisWeekTasksViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.stream.Collectors
import kotlin.random.Random

class ThisWeekTasksViewModelTest{
    private lateinit var listOfTasks : List<Task>
    private lateinit var taskDao: TaskDao
    private lateinit var taskRepository: TaskRepository
    private lateinit var viewModel: ThisWeekTasksViewModel

    val today = LocalDate.now()
    val monday = today.plusDays(today.daysToDay(DayOfWeek.MONDAY))
    val sunday = today.plusDays(today.daysToDay(DayOfWeek.SUNDAY))

    @get:Rule
    var instantTaskExecutorRule : InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        listOfTasks = generateTaskList(8, monday, LocalTime.of(10,30),123456789123456789L)
        taskDao = TaskDaoMock()
        listOfTasks.forEach(taskDao::insert)
        taskRepository = TaskRepository(NotificationWorkerMock(),taskDao)
        viewModel = ThisWeekTasksViewModel(taskRepository)
    }

    @Test
    fun `check if we obtain this week unfinished task must return a list with task grouped and ordered by execution date`(){
        val expectedValue = listOfTasks.parallelStream().filter {
                    it.finished.not() &&
                    (it.executionDate == monday || it.executionDate.isAfter(monday)) &&
                    (it.executionDate == sunday || it.executionDate.isBefore(sunday))
                }.collect(Collectors.toList())
                .groupBy { it.executionDate }
                .map { TasksPerDay(it.key,it.value) }
                .sortedBy { it.date }

        val result = viewModel.getThisWeekUnfinishedTask().getOrAwaitValue()

        assertThat(result).isInOrder { taskPerDay1, taskPerDay2 ->
            (taskPerDay1 as TasksPerDay).date.compareTo((taskPerDay2 as TasksPerDay).date)
        }

        assertThat(result).containsExactlyElementsIn(expectedValue)
    }

    fun generateTaskList(quantityOfDays:Int, executionDate: LocalDate, executionHour: LocalTime, seed:Long) = run {
        var id = 0
        val random = Random(seed)
        (0 until quantityOfDays).flatMap { day ->
            (0..9).map{ hour ->
                Task(id++,"prueba","prueba",executionDate.plusDays(day.toLong()),executionHour.plusHours(hour.toLong()), Difficulty.EASY,random.nextBoolean() )
            }
        }
    }
}