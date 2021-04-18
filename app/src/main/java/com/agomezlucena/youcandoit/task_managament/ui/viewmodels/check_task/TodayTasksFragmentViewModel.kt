package com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task

import androidx.lifecycle.*
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayTasksFragmentViewModel  @Inject constructor(private val repository: TaskRepository): ViewModel() {
    fun getUnfinishedTasks () = repository.getTodayUnfinishedTask()


    fun finishTask(task: Task){
        task.finished = true
        launchCoroutine (Dispatchers.IO) {
            repository.update(task)
        }
    }

    fun deleteTask(task:Task){
        launchCoroutine (Dispatchers.IO) {
            repository.remove(task)
        }
    }
}