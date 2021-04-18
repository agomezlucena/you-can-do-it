package com.agomezlucena.youcandoit.task_managament.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.agomezlucena.youcandoit.task_managament.ui.check_task.ThisMonthTasksFragment
import com.agomezlucena.youcandoit.task_managament.ui.check_task.ThisWeekTasksFragment
import com.agomezlucena.youcandoit.task_managament.ui.check_task.TodayTasksFragment

class ListTasksAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val listOfFragments = listOf(TodayTasksFragment(), ThisWeekTasksFragment(), ThisMonthTasksFragment())

    override fun getItemCount(): Int = listOfFragments.size

    override fun createFragment(position: Int): Fragment {
        return listOfFragments[position]
    }

}