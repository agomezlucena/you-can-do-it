package com.agomezlucena.youcandoit.task_managament.ui.viewmodels.create_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.task_managament.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
    private val _taskExecutionDate = MutableLiveData(LocalDateTime.now().withSecond(0))
    val taskExecutionDate: LiveData<LocalDateTime> = _taskExecutionDate

    fun validateTask(task: Task): Boolean {
        val today = LocalDate.now()
        val actualHour = LocalTime.now().withSecond(0).withNano(0)
        return (task.executionDate > today || (task.executionDate == today && task.executionHour >= actualHour)) &&
                task.title.isNotBlank() && task.description.isNotBlank() && task.finished.not()
    }

    fun insert(task: Task) {
       launchCoroutine(Dispatchers.IO) {
            taskRepository.insert(task)
        }

    }

    fun updateExecutionDate(newDateTime: LocalDateTime) {
        _taskExecutionDate.value = newDateTime.withSecond(0).withNano(0)
        Timber.d(newDateTime.toString())
    }

}