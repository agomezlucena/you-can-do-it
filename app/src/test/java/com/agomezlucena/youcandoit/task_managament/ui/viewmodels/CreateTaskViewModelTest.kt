package com.agomezlucena.youcandoit.task_managament.ui.viewmodels

import com.agomezlucena.youcandoit.data_managament.db.TaskDao
import com.agomezlucena.youcandoit.data_managament.repos.NotificationWorkerMock
import com.agomezlucena.youcandoit.data_managament.repos.TaskDaoMock
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.task
import com.agomezlucena.youcandoit.task_managament.Difficulty
import com.agomezlucena.youcandoit.task_managament.Task
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.create_task.CreateTaskViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class CreateTaskViewModelTest {
    private lateinit var taskDao: TaskDao
    private lateinit var taskRepository: TaskRepository
    private lateinit var viewModel: CreateTaskViewModel

    @Before
    fun setUp() {
        taskDao = TaskDaoMock()
        taskRepository = TaskRepository(NotificationWorkerMock(),taskDao)
        viewModel = CreateTaskViewModel(taskRepository)
    }

    @Test
    fun `check if we validate a correct task must return true`() {
        val todayTaskToValidate = task {
            title = "prueba"
            description = "prueba"
        }

        val anotherDayInTheFutureTask = task {
            title = "prueba"
            description = "prueba"
            executionDate = executionDate.plusDays(2)
            executionHour = LocalTime.of(10,30)
        }

        assertThat(viewModel.validateTask(todayTaskToValidate)).isTrue()
        assertThat(viewModel.validateTask(anotherDayInTheFutureTask)).isTrue()
    }

    @Test
    fun `check if we validate a invalid task must return false`() {
        val todayTaskToValidate = Task(
                title = "prueba",
                description = "prueba",
                executionDate = LocalDate.now(),
                executionHour = LocalTime.now().minusMinutes(30),
                difficulty = Difficulty.EASY
        )

        val anotherDayInTheFutureTask = Task(
                title = "",
                description = "prueba",
                executionDate = LocalDate.now().plusDays(2),
                executionHour = LocalTime.of(10,30),
                difficulty = Difficulty.EASY
        )

        assertThat(viewModel.validateTask(todayTaskToValidate)).isFalse()
        assertThat(viewModel.validateTask(anotherDayInTheFutureTask)).isFalse()
    }
}