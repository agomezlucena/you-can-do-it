package com.agomezlucena.youcandoit.task_managament.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agomezlucena.youcandoit.data_managament.db.TaskDao
import com.agomezlucena.youcandoit.data_managament.repos.NotificationWorkerMock
import com.agomezlucena.youcandoit.data_managament.repos.TaskDaoMock
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.getOrAwaitValue
import com.agomezlucena.youcandoit.task_managament.Difficulty
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.Task
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.TodayTasksFragmentViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

@ExperimentalCoroutinesApi
class TodayTasksFragmentViewModelTest{
    private lateinit var viewmodel: TodayTasksFragmentViewModel
    private lateinit var repository: TaskRepository
    private lateinit var daoMock:TaskDao
    private lateinit var listOfTasks:List<Task>

    @get:Rule
    var instantTaskExecutorRule : InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        listOfTasks = generateTaskList(2, LocalDate.now(), LocalTime.of(10,30),123456789123456789L)
        daoMock = TaskDaoMock()
        listOfTasks.forEach(daoMock::insert)
        repository = TaskRepository(NotificationWorkerMock(),daoMock)
        viewmodel = TodayTasksFragmentViewModel(repository)
    }


    @Test
    fun `check if return the correct list of unfinished task`(){
        val expectedValue = listOfTasks.filter { it.executionDate == LocalDate.now() && it.finished.not() }.sortedByDescending { it.executionHour }
        val result = viewmodel.getUnfinishedTasks().getOrAwaitValue()

        assertThat(result).isInOrder{
            task1,task2 -> when{
                (task1 as Task).executionHour < (task2 as Task).executionHour -> 1
                task1.executionHour > task2.executionHour -> -1
                else -> 0
            }
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