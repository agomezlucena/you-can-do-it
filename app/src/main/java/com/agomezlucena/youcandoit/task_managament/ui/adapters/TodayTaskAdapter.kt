package com.agomezlucena.youcandoit.task_managament.ui.adapters

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.LayoutTaskPreviewBinding
import com.agomezlucena.youcandoit.task_managament.Task
import com.agomezlucena.youcandoit.task_managament.ui.check_task.ListTasksViewPagerFragmentDirections
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.TodayTasksFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.format.DateTimeFormatter

class TodayTaskAdapter(
    private val viewModel: TodayTasksFragmentViewModel,
    private val context : Context,
    private var tasks:List<Task>,
    private val navController: NavController) : RecyclerView.Adapter<TodayTaskAdapter.TodayTaskViewHolder>() {

    inner class TodayTaskViewHolder (private val binding: LayoutTaskPreviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(task: Task){
            binding.root.setOnClickListener {
                navController.navigate(ListTasksViewPagerFragmentDirections.actionListTasksFragmentToDetailTaskDialog(task))
            }
            binding.taskPreviewTitle.text = task.title
            binding.taskPreviewDescription.text = task.description
            binding.taskPreviewDate.text = task.executionHour.format(DateTimeFormatter.ofPattern("HH:mm"))
            binding.taskPreviewScore.text = task.difficulty.score.toString()
            binding.finishedStatus.isChecked = task.finished

            binding.finishedStatus.setOnCheckedChangeListener{_,isChecked ->
                if (isChecked) viewModel.finishTask(task)
            }
            binding.taskPreviewCloseIv.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                        .setTitle(R.string.delete_dialog_title)
                        .setMessage(R.string.delete_dialog_message)
                        .setPositiveButton(R.string.delete){_,_ ->
                            viewModel.deleteTask(task)
                        } .setNegativeButton(R.string.cancel){ dialogInterface,_ ->
                            dialogInterface.dismiss()
                        }
                        .show()
            }
        }
    }

    inner class TaskItemDifficultCallBack(private val oldList : List<Task>,private val newList : List<Task>) : DiffUtil.Callback(){
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayTaskViewHolder {
        val binding = LayoutTaskPreviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return TodayTaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayTaskViewHolder, position: Int) {
        holder.bindData(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateTasks (tasks: List<Task>){
        val oldList = this.tasks
        val diffResult = DiffUtil.calculateDiff(TaskItemDifficultCallBack(oldList,tasks))
        this.tasks = tasks
        diffResult.dispatchUpdatesTo(this)
    }
}