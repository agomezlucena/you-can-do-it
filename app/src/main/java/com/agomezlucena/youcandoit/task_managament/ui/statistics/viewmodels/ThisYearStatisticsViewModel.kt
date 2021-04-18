package com.agomezlucena.youcandoit.task_managament.ui.statistics.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.ScoreData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThisYearStatisticsViewModel @Inject constructor(
    private val repository: TaskRepository
    ) : ViewModel() {

    fun getYearScoreGroupByDay(): LiveData<List<DayScore>> {
        return repository.getThisYearScorePerDay()
    }

    fun thisYearScore(): ScoreData {
        val accomplishedScore = repository.getThisYearAccomplishedScoreNoLiveData()
        val totalScore = repository.getThisYearTotalScoreNoLiveData()
        return ScoreData(totalScore, accomplishedScore)
    }
}