package com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task

import androidx.lifecycle.ViewModel
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.task_managament.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DetailTaskDialogViewModel @Inject constructor(private val repository: TaskRepository):ViewModel(){
    fun updateTask(task: Task){
        launchCoroutine(Dispatchers.IO){
            repository.update(task)
        }
    }

    fun deleteTask(task: Task){
        launchCoroutine(Dispatchers.IO){
            repository.remove(task)
        }
    }
}