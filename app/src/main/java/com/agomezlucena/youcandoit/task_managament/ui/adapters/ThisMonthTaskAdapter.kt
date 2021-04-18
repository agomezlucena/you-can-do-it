package com.agomezlucena.youcandoit.task_managament.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.LayoutTaskPerDayBinding
import com.agomezlucena.youcandoit.task_managament.TasksPerDay
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ThisMonthTasksViewModel
import com.agomezlucena.youcandoit.translateDayOfWeek
import java.time.format.DateTimeFormatter

class ThisMonthTaskAdapter(
    private val viewModel: ThisMonthTasksViewModel,
    private val context: Context,
    private var tasks: List<TasksPerDay>,
    private val navController: NavController
) :
    RecyclerView.Adapter<ThisMonthTaskAdapter.ThisMonthTasksViewHolder>() {

    inner class ThisMonthTasksViewHolder(
        private val binding: LayoutTaskPerDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(tasksPerDay: TasksPerDay) {
            val adapter = MothTasksPreviewAdapter(viewModel, context, emptyList(),navController)
            binding.taskPerDayDay.text =
                tasksPerDay.date.format(DateTimeFormatter.ofPattern(context.getString(R.string.date_format)))
            binding.taskOfDay.layoutManager = LinearLayoutManager(context)
            binding.taskOfDay.adapter = adapter
            adapter.updateTasks(tasksPerDay.listOfTask)
            binding.taskOfDay.isNestedScrollingEnabled = false
            binding.tvAccomplishedScore.text = context.getString(
                R.string.some_day_score,
                tasksPerDay.listOfTask.map { it.difficulty.score }.sum()
            )
        }

    }

    inner class TaskItemDifficultCallBack(
        private val oldList: List<TasksPerDay>,
        private val newList: List<TasksPerDay>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].date == newList[newItemPosition].date

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThisMonthTasksViewHolder {
        val binding = LayoutTaskPerDayBinding.inflate(LayoutInflater.from(context), parent, false)
        return ThisMonthTasksViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateTasks(tasks: List<TasksPerDay>) {
        val oldList = this.tasks
        val diffResult = DiffUtil.calculateDiff(TaskItemDifficultCallBack(oldList, tasks))
        this.tasks = tasks
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ThisMonthTasksViewHolder, position: Int) {
        holder.bindData(tasks[position])
    }
}