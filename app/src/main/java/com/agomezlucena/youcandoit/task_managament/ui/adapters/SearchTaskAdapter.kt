package com.agomezlucena.youcandoit.task_managament.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.LayoutTaskPreviewBinding
import com.agomezlucena.youcandoit.task_managament.Task
import com.agomezlucena.youcandoit.task_managament.ui.search_tasks.SearchFragmentDirections
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.search_tasks.SearchFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.format.DateTimeFormatter

class SearchTaskAdapter(
        private val viewModel: SearchFragmentViewModel,
        private val context: Context,
        private var tasks: List<Task>,
        private val navController: NavController
) : RecyclerView.Adapter<SearchTaskAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutTaskPreviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindData(tasks[position])
    }

    override fun getItemCount() = tasks.size


    inner class ViewHolder(
            private val binding: LayoutTaskPreviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(task: Task) {
            binding.root.setOnClickListener {
                navController.navigate(SearchFragmentDirections.actionSearchFragmentToDetailTaskDialog(task))
            }
            binding.taskPreviewTitle.text = task.title
            binding.taskPreviewDescription.text = task.description
            binding.taskPreviewDate.text = task.executionHour.format(DateTimeFormatter.ofPattern("HH:mm"))
            binding.taskPreviewScore.text = task.difficulty.score.toString()
            binding.finishedStatus.isChecked = task.finished

            binding.finishedStatus.setOnCheckedChangeListener { _, isChecked ->
                task.finished = isChecked
                viewModel.updateTask(task)
            }
            binding.taskPreviewCloseIv.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                        .setTitle(R.string.delete_dialog_title)
                        .setMessage(R.string.delete_dialog_message)
                        .setPositiveButton(R.string.delete) { _,_ ->
                            viewModel.deleteTask(task)
                        }.setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
            }

        }

    }


}