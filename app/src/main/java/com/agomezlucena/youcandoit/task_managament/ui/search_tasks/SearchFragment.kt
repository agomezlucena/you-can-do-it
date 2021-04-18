package com.agomezlucena.youcandoit.task_managament.ui.search_tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.FragmentSeachBinding
import com.agomezlucena.youcandoit.task_managament.Task
import com.agomezlucena.youcandoit.task_managament.ui.adapters.SearchTaskAdapter
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.search_tasks.SearchFragmentViewModel
import com.agomezlucena.youcandoit.toLocalDateTime
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel by viewModels<SearchFragmentViewModel>()
    private lateinit var binding: FragmentSeachBinding
    private lateinit var taskLiveData: LiveData<List<Task>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSeachBinding.inflate(inflater, container, false)
        binding.tvDateSearch.setOnClickListener {
            launchDatePicker()
        }
        binding.taskFromSearchDate.layoutManager = LinearLayoutManager(requireContext())
        binding.taskFromSearchDate.adapter = SearchTaskAdapter(viewModel, requireContext(), emptyList(),findNavController())
        viewModel.searchDateLiveData.observe(viewLifecycleOwner) {
            binding.tvDateSearch.text = it.format(DateTimeFormatter.ofPattern(getString(R.string.date_format)))
            setNewAdapter(it)
        }
        return binding.root
    }

    private fun setNewAdapter(date: LocalDate) {
        if (this::taskLiveData.isInitialized) {
            taskLiveData.removeObservers(viewLifecycleOwner)
        }
        taskLiveData = viewModel.getDayLiveData(date)
        taskLiveData.observe(viewLifecycleOwner) {
            Timber.d(it.toString())
            binding.taskFromSearchDate.adapter = SearchTaskAdapter(viewModel,requireContext(),it,findNavController())
            if (it.isEmpty()) {
                binding.taskFromSearchDate.visibility = View.INVISIBLE
                binding.noItemsTv.visibility = View.VISIBLE
            } else {
                binding.taskFromSearchDate.visibility = View.VISIBLE
                binding.noItemsTv.visibility = View.GONE
                binding.taskFromSearchDate.invalidate()
            }
        }
    }

    private fun launchDatePicker() {
        val constraints = CalendarConstraints.Builder()
                .setOpenAt(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date)
                .setCalendarConstraints(constraints)
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            viewModel.updateDate(LocalDate.from(calendar.toLocalDateTime()))
        }
        datePicker.show(parentFragmentManager, "datepicker")
    }
}