package com.agomezlucena.youcandoit.task_managament.ui.statistics.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.github.mikephil.charting.charts.LineChart
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThisMonthStatisticsViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    fun getMonthScoreGroupByDay(): LiveData<List<DayScore>> {
        return repository.getThisMonthScorePerDay()
    }

    fun thisMonthScore() : ScoreData {
        val accomplishedScore = repository.getThisMonthAccomplishedScoreNoLiveData()
        val totalScore = repository.getThisMonthTotalScoreNoLiveData()
        return ScoreData(totalScore, accomplishedScore)
    }
}