package com.agomezlucena.youcandoit.task_managament.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.agomezlucena.youcandoit.task_managament.ui.check_task.ThisMonthTasksFragment
import com.agomezlucena.youcandoit.task_managament.ui.check_task.ThisWeekTasksFragment
import com.agomezlucena.youcandoit.task_managament.ui.check_task.TodayTasksFragment
import com.agomezlucena.youcandoit.task_managament.ui.statistics.ThisMonthStatistics
import com.agomezlucena.youcandoit.task_managament.ui.statistics.ThisWeekStatistics
import com.agomezlucena.youcandoit.task_managament.ui.statistics.ThisYearStatistics

class ListStatisticsAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val listOfFragments = listOf(ThisWeekStatistics(), ThisMonthStatistics(),ThisYearStatistics())

    override fun getItemCount(): Int = listOfFragments.size

    override fun createFragment(position: Int): Fragment {
        return listOfFragments[position]
    }

}