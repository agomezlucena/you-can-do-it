package com.agomezlucena.youcandoit.task_managament.ui.check_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agomezlucena.youcandoit.databinding.FragmentThisMonthTasksBinding
import com.agomezlucena.youcandoit.task_managament.ui.adapters.ThisMonthTaskAdapter
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ThisMonthTasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThisMonthTasksFragment : Fragment(){
    private val viewModel by viewModels<ThisMonthTasksViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = FragmentThisMonthTasksBinding.inflate(layoutInflater,container,false)
        binding.thisWeekTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.thisWeekTasks.adapter = ThisMonthTaskAdapter(viewModel,requireContext(), emptyList(),findNavController())

        viewModel.getThisMonthTask().observe(viewLifecycleOwner){
            val adapter = binding.thisWeekTasks.adapter as ThisMonthTaskAdapter
            adapter.updateTasks(it)
        }
        return binding.root
    }
}