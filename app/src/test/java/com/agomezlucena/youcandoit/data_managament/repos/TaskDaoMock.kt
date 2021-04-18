package com.agomezlucena.youcandoit.data_managament.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agomezlucena.youcandoit.data_managament.db.TaskDao
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.Task
import java.time.LocalDate

class TaskDaoMock : TaskDao {
    private val tasks = ArrayList<Task>()

    override fun findById(id: Int): LiveData<Task> {
        return MutableLiveData<Task>().apply {
            value = tasks.find { it.id == id }
        }
    }

    override fun getTasksFromADay(executionDate: LocalDate): LiveData<List<Task>> {
        return MutableLiveData<List<Task>>().apply {
            value = tasks.filter { it.executionDate == executionDate }
        }
    }

    override fun getUnfinishedTaskFromADay(executionDate: LocalDate): LiveData<List<Task>> {
        return MutableLiveData<List<Task>>().apply {
            value = tasks
                .filter { !it.finished && it.executionDate == executionDate }
                .sortedByDescending { it.executionHour }
        }
    }

    override fun getUnfinishedTaskFromARangeOfDates(
        from: LocalDate,
        to: LocalDate
    ): LiveData<List<Task>> {
        return MutableLiveData<List<Task>>().apply {
            value = tasks.filter {
                (it.executionDate.isBefore(to) || it.executionDate.isEqual(to)) &&
                        (it.executionDate.isAfter(from) || it.executionDate.isEqual(from)) &&
                        it.finished.not()
            }.sortedByDescending { it.executionDate }
        }
    }

    override fun getTotalScorePerDaysInRange(
        from: LocalDate,
        to: LocalDate
    ): LiveData<List<DayScore>> {
        return MutableLiveData<List<DayScore>>().apply {
            value = tasks
                .filter {
                    (it.executionDate.isBefore(to) || it.executionDate.isEqual(to)) &&
                            (it.executionDate.isAfter(from) || it.executionDate.isEqual(from))
                }
                .sortedBy { it.executionDate }
                .groupBy { it.executionDate }
                .map {
                    DayScore(it.key, it.value.map { task -> task.difficulty.score }.sum().toLong())
                }
        }
    }

    override fun getAccomplishedScorePerDaysInRange(
        from: LocalDate,
        to: LocalDate
    ): LiveData<List<DayScore>> {
        return MutableLiveData<List<DayScore>>().apply {
            value = tasks
                .filter {
                    it.finished &&
                            (it.executionDate.isBefore(to) || it.executionDate.isEqual(to)) &&
                            (it.executionDate.isAfter(from) || it.executionDate.isEqual(from))
                }
                .sortedBy { it.executionDate }
                .groupBy { it.executionDate }
                .map {
                    DayScore(it.key, it.value.map { task -> task.difficulty.score }.sum().toLong())
                }
        }
    }

    override fun getTotalScoreFromADay(dateToSearch: LocalDate): LiveData<Long> {
        return MutableLiveData<Long>().apply {
            value = tasks.filter { it.executionDate == dateToSearch }
                .map { it.difficulty.score }
                .sum().toLong()
        }
    }

    override fun getAccomplishedScoreFromADay(dateToSearch: LocalDate): LiveData<Long> {
        return MutableLiveData<Long>().apply {
            value = tasks.filter { it.executionDate == dateToSearch && it.finished }
                .map { it.difficulty.score }
                .sum().toLong()
        }
    }

    override fun getTotalScoreFromARange(from: LocalDate, to: LocalDate): LiveData<Long> {
        return MutableLiveData<Long>().apply {
            value = tasks.filter {
                (it.executionDate.isBefore(to) || it.executionDate.isEqual(to)) &&
                        (it.executionDate.isAfter(from) || it.executionDate.isEqual(from))
            }.map {
                it.difficulty.score
            }.sum().toLong()
        }
    }

    override fun getAccomplishedScoreFromARange(from: LocalDate, to: LocalDate): LiveData<Long> {
        return MutableLiveData<Long>().apply {
            value = tasks.filter {
                (it.executionDate.isBefore(to) || it.executionDate.isEqual(to)) &&
                        (it.executionDate.isAfter(from) || it.executionDate.isEqual(from)) &&
                        it.finished
            }.map {
                it.difficulty.score
            }.sum().toLong()
        }
    }

    override fun insert(task: Task): Long {
        val indexOfTask = tasks.find { it.id == task.id }?.id
        if (indexOfTask == null) {
            task.id = tasks.size
            tasks.add(task)
        } else {
            tasks.add(indexOfTask, task)
        }
        return task.id?.toLong()?:-1L
    }

    override fun delete(task: Task) {
        tasks.remove(task)
    }
}