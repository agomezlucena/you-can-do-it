package com.agomezlucena.youcandoit.task_managament.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ListTaskViewPagerFragmentViewModel.Position
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _position = MutableLiveData(Position(0, 0))

    val position : LiveData<Position> = _position

    fun updatePosition(position: Position){
        _position.value = position
    }

    fun getScore(position: Position) = when(position.newPosition){
        0 -> todayScore()
        1 -> thisWeekScore()
        else -> thisMonthScore()
    }

    private fun todayScore() : LiveData<ScoreData>{
        return MediatorLiveData<ScoreData>().apply {
            addSource(repository.getTodayTotalScore()){
                this.value = ScoreData(it?:0,this.value?.accomplishedScore?:0)
            }
            addSource(repository.getTodayAccomplishedScore()){
                this.value = ScoreData(this.value?.totalScore?:0,it?:0)
            }
        }
    }

    private fun thisWeekScore() : LiveData<ScoreData>{
        return MediatorLiveData<ScoreData>().apply {
            addSource(repository.getThisWeekTotalScore()){
                this.value = ScoreData(it?:0,this.value?.accomplishedScore?:0)
            }
            addSource(repository.getThisWeekAccomplishedScore()) {
                this.value = ScoreData(this.value?.totalScore?:0,it?:0)
            }
        }
    }

    private fun thisMonthScore() : LiveData<ScoreData>{
        return MediatorLiveData<ScoreData>().apply {
            addSource(repository.getThisMonthTotalScore()){
                this.value = ScoreData(it?:0,this.value?.accomplishedScore?:0)
            }
            addSource(repository.getThisMonthAccomplishedScore()) {
                this.value = ScoreData(this.value?.totalScore?:0,it?:0)
            }
        }
    }
}