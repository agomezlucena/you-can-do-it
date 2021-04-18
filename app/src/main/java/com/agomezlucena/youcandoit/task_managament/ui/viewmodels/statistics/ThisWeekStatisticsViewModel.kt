package com.agomezlucena.youcandoit.task_managament.ui.viewmodels.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.task_managament.DayScore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThisWeekStatisticsViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    fun getDataScorePerDay():LiveData<List<DayScore>>{
        return repository.getThisWeekScorePerDay()
    }
}