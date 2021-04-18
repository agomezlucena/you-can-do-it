package com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ListTaskViewPagerFragmentViewModel  : ViewModel(){
    private val position = MutableLiveData(Position(0,0))

    fun position():LiveData<Position> = position

    fun updatePosition(position:Int) {
        this.position.value =  Position(this.position.value!!.newPosition,position)
    }

    class Position (var oldPosition : Int, var newPosition: Int){
        fun fromLeft() = oldPosition <= newPosition
    }
}