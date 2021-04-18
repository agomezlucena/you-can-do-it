package com.agomezlucena.youcandoit.task_managament.ui.check_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.DialogTaskDetailBinding
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.DetailTaskDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTaskDialog : BottomSheetDialogFragment() {
    private lateinit var binding:DialogTaskDetailBinding
    private val args by navArgs<DetailTaskDialogArgs>()
    private val viewModel by viewModels<DetailTaskDialogViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val task = args.task
        binding = DialogTaskDetailBinding.inflate(inflater,container,false)
        binding.tvTitle.text = task.title
        binding.tvDescription.text = task.description
        binding.finishButton.text = if(task.finished) getString(R.string.retake_task) else getString(R.string.finish_task)

        binding.finishButton.setOnClickListener {
            task.finished = !task.finished
            viewModel.updateTask(task)
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.delete){ _, _: Int ->
                    viewModel.deleteTask(task)
                } .setNegativeButton(R.string.cancel){ dialogInterface,_ ->
                    dialogInterface.dismiss()
                }
                .show()
        }

        binding.close.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}