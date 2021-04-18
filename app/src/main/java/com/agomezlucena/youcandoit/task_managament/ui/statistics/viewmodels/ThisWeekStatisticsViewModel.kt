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
class ThisWeekStatisticsViewModel @Inject constructor(private val repository: TaskRepository):ViewModel() {
    fun getWeekScoreGroupByDay():LiveData<List<DayScore>> {
        return repository.getThisWeekScorePerDay()
    }

    fun thisWeekScore() : ScoreData{
        val accomplishedScore = repository.getThisWeekAccomplishedScoreNoLiveData()
        val totalScore = repository.getThisWeekTotalScoreNoLiveData()
        return ScoreData(totalScore, accomplishedScore)
    }
}