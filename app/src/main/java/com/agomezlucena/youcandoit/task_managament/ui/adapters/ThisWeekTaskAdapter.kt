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
import com.agomezlucena.youcandoit.task_managament.ui.viewmodels.check_task.ThisWeekTasksViewModel
import com.agomezlucena.youcandoit.translateDayOfWeek

class ThisWeekTaskAdapter(
        private val viewModel : ThisWeekTasksViewModel,
        private val context: Context,
        private var tasks: List<TasksPerDay>,
        private val navController: NavController
) : RecyclerView.Adapter<ThisWeekTaskAdapter.ThisWeekTaskViewHolder>() {

    inner class ThisWeekTaskViewHolder(
            private val binding: LayoutTaskPerDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(tasksPerDay: TasksPerDay) {
            val adapter = WeekTaskPreviewAdapter(viewModel,context, emptyList(),navController)
            binding.taskPerDayDay.text = translateDayOfWeek(context,tasksPerDay.date.dayOfWeek)
            binding.taskOfDay.layoutManager = LinearLayoutManager(context)
            binding.taskOfDay.adapter = adapter
            adapter.updateTasks(tasksPerDay.listOfTask)
            binding.taskOfDay.isNestedScrollingEnabled = false
            binding.tvAccomplishedScore.text = context.getString(R.string.some_day_score,tasksPerDay.listOfTask.map { it.difficulty.score }.sum())
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThisWeekTaskViewHolder {
        val binding = LayoutTaskPerDayBinding.inflate(LayoutInflater.from(context), parent, false)
        return ThisWeekTaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThisWeekTaskViewHolder, position: Int) {
        holder.bindData(tasks[position])
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
}