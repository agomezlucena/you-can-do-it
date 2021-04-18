package com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.Task
import com.agomezlucena.youcandoit.task_managament.TasksPerDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThisMonthTasksViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    fun getThisMonthTask() :LiveData<List<TasksPerDay>>{
       return repository.getThisMonthUnfinishedTasks().map {
           it.groupBy { task ->
               task.executionDate
           }.map { map ->
               TasksPerDay(map.key,map.value)
           }
       }
    }

    fun remove(task: Task){
        launchCoroutine (Dispatchers.IO) {
            repository.remove(task)
        }
    }
    fun update(task: Task){
        launchCoroutine (Dispatchers.IO) {
            repository.update(task)
        }
    }
}