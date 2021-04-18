package com.agomezlucena.youcandoit.task_managament.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agomezlucena.youcandoit.databinding.FragmentListTasksViewPagerBinding
import com.agomezlucena.youcandoit.task_managament.ui.adapters.ListStatisticsAdapter

class ListStatistics : Fragment() {
    private lateinit var binding : FragmentListTasksViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListTasksViewPagerBinding.inflate(inflater,container,false)
        binding.viewpagerBaseViewPagerFragment.adapter = ListStatisticsAdapter(requireActivity())
        return binding.root
    }
}