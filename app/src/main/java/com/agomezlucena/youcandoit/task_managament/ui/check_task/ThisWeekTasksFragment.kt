package com.agomezlucena.youcandoit.task_managament.ui.check_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agomezlucena.youcandoit.databinding.FragmentThisWeekTasksBinding
import com.agomezlucena.youcandoit.task_managament.ui.adapters.ThisWeekTaskAdapter
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ThisWeekTasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThisWeekTasksFragment():Fragment() {
    private val viewModel by viewModels<ThisWeekTasksViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = FragmentThisWeekTasksBinding.inflate(layoutInflater,container,false)
        binding.thisWeekTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.thisWeekTasks.adapter = ThisWeekTaskAdapter(viewModel,requireContext(), emptyList(),findNavController())
        viewModel.getThisWeekUnfinishedTask().observe(viewLifecycleOwner){
            val adapter = binding.thisWeekTasks.adapter as ThisWeekTaskAdapter
            adapter.updateTasks(it)
        }
        return binding.root
    }
}