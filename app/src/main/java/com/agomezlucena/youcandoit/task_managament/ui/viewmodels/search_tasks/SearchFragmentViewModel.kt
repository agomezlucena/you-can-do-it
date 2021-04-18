package com.agomezlucena.youcandoit.task_managament.ui.viewmodels.search_tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.task_managament.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(private val repository: TaskRepository) :ViewModel() {
    private val _searchDateLiveData = MutableLiveData<LocalDate>()
    val searchDateLiveData : LiveData<LocalDate> = _searchDateLiveData

    fun updateDate(newDate: LocalDate){
        _searchDateLiveData.value = newDate
    }

    fun getDayLiveData(date: LocalDate) = repository.getDateTasks(date)
    fun updateTask (task: Task){
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