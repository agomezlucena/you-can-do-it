package com.agomezlucena.youcandoit.task_managament.ui.check_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agomezlucena.youcandoit.databinding.FragmentTodayTasksBinding
import com.agomezlucena.youcandoit.task_managament.ui.adapters.TodayTaskAdapter
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.TodayTasksFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayTasksFragment : Fragment() {
    private val viewModel by viewModels<TodayTasksFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTodayTasksBinding.inflate(inflater,container,false)
        binding.todayTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.todayTasks.adapter = TodayTaskAdapter(viewModel,requireContext(), emptyList(),findNavController())
        viewModel.getUnfinishedTasks().observe(viewLifecycleOwner){
            val adapter = binding.todayTasks.adapter as TodayTaskAdapter
            adapter.updateTasks(it)
        }
        return binding.root
    }


}