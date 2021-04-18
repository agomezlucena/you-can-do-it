package com.agomezlucena.youcandoit.task_managament.ui.create_task

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agomezlucena.youcandoit.*
import com.agomezlucena.youcandoit.databinding.FragmentCreateTaskBinding
import com.agomezlucena.youcandoit.task_managament.Difficulty
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.create_task.CreateTaskViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {
    private val viewModel by viewModels<CreateTaskViewModel>()
    private lateinit var binding: FragmentCreateTaskBinding

    private var taskDifficulty by Delegates.observable(Difficulty.EASY) { _, _, newValue ->
        binding.tvScore.text = newValue.score.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        binding.btnEasyCreateTask.setOnClickListener(this::onClickOnEasy)
        binding.btnMediumCreateTask.setOnClickListener(this::onClickOnMedium)
        binding.btnHardCreateTask.setOnClickListener(this::onClickOnHard)
        binding.etExecutionDateTime.inputType = InputType.TYPE_NULL
        binding.etExecutionDateTime.setOnClickListener(this::launchDatePicker)

        binding.tvScore.text = taskDifficulty.score.toString()

        viewModel.taskExecutionDate.observe(viewLifecycleOwner) {
            binding.etExecutionDateTime.setText(it.format(
                    DateTimeFormatter.ofPattern(getString(R.string.date_time_format))
                )
            )
        }

        binding.btnCreateTask.setOnClickListener(this::insertTask)
        return binding.root
    }

    private fun onClickOnEasy(v: View) {
        taskDifficulty = Difficulty.EASY
    }

    private fun onClickOnMedium(v: View) {
        taskDifficulty = Difficulty.MEDIUM
    }

    private fun onClickOnHard(v: View) {
        taskDifficulty = Difficulty.HARD
    }

    private fun launchDatePicker(v: View) {
        val today = LocalDate.now().toCalendar()
        val inTwoMonth = LocalDate.now().plusMonths(2).toCalendar()
        val constraints = CalendarConstraints.Builder()
                .setStart(today.timeInMillis)
                .setEnd(inTwoMonth.timeInMillis)
                .setOpenAt(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date)
                .setCalendarConstraints(constraints)
                .build()

        datePicker.addOnPositiveButtonClickListener {
            lauchTimePicker(it)

        }


        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun lauchTimePicker(dayInMillis: Long) {
        val actualHour = LocalTime.now()

        val timePicker = MaterialTimePicker.Builder()
                .setTitleText(R.string.select_time)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(actualHour.hour)
                .setMinute(actualHour.minute)
                .build()


        timePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()

            calendar.timeInMillis = dayInMillis
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)

            val result = calendar.toLocalDateTime()
            viewModel.updateExecutionDate(result)
        }
        timePicker.show(parentFragmentManager, "timePicker")

    }

    private fun insertTask(v: View) {
        val task = task {
            title = binding.etTitle.text.toString()
            description = binding.etDescription.text.toString()
            executionDate = LocalDate.from(viewModel.taskExecutionDate.value)
            executionHour = LocalTime.from(viewModel.taskExecutionDate.value?.withSecond(0))
            difficulty = taskDifficulty
        }

        if (viewModel.validateTask(task)) {
            viewModel.insert(task)
            findNavController().navigate(R.id.action_createTaskFragment_to_listTasksFragment)
        } else {
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.creation_error_title)
                    .setMessage(R.string.creation_error_text)
                    .show()
        }
    }
}