package com.agomezlucena.youcandoit.task_managament.ui.check_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.agomezlucena.youcandoit.R

import com.agomezlucena.youcandoit.task_managament.ui.adapters.ListTasksAdapter
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ListTaskViewPagerFragmentViewModel
import timber.log.Timber

class ListTasksViewPagerFragment : Fragment() {
    private val viewModel by activityViewModels<ListTaskViewPagerFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list_tasks_view_pager,container,false)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewpagerBaseViewPagerFragment)
        viewPager2.adapter = ListTasksAdapter(requireActivity())
        viewPager2.registerOnPageChangeCallback(PageChangeCallBack())
        return view
    }

    private inner class PageChangeCallBack() : ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Timber.d("cambiando a fragment $position")
            viewModel.updatePosition(position)
        }
    }
}