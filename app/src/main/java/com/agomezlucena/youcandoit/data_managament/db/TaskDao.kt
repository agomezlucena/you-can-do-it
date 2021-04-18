package com.agomezlucena.youcandoit.data_managament.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.Task
import java.time.LocalDate

@Dao
interface TaskDao {
    /***
     * Returns a live data with a task with id passed by parameter
     */
    @Query("select * from Task where id = :id")
    fun findById(id: Int): LiveData<Task>

    /**
     * Returns all task from a day order by execution hour
     */
    @Query("select * from Task where executionDate = :executionDate order by executionHour")
    fun getTasksFromADay(executionDate: LocalDate) : LiveData<List<Task>>

    /***
     * Returns unfinished tasks from a day, order by descendant execution hour
     */
    @Query("select * from Task where executionDate = :executionDate and not finished order by executionHour desc")
    fun getUnfinishedTaskFromADay(executionDate: LocalDate): LiveData<List<Task>>

    /***
     * Return unfinished tasks from a range of dates, order by descendant execution date
     */
    @Query("select * from Task where not finished and executionDate between :from and :to order by executionDate desc")
    fun getUnfinishedTaskFromARangeOfDates(from: LocalDate,to: LocalDate) : LiveData<List<Task>>

    /***
     * Returns a list with the sum of the difficulty of all the tasks in a range of dates grouped by execution date
     */
    @Query("select executionDate,sum(difficulty) as 'score' from task where executionDate between :from and :to group by executionDate order by executionDate")
    fun getTotalScorePerDaysInRange(from: LocalDate, to: LocalDate):LiveData<List<DayScore>>

    /**
     * Returns a list with the sum of the difficulty of all the finished tasks in a range of dates grouped by execution date
     */
    @Query("select executionDate,sum(difficulty) as 'score' from task where finished and executionDate between :from and :to group by executionDate order by executionDate")
    fun getAccomplishedScorePerDaysInRange(from: LocalDate, to: LocalDate):LiveData<List<DayScore>>

    /***
     * Returns the sum of difficulty of all tasks from a date
     */
    @Query("select sum(difficulty) from task where executionDate = :dateToSearch")
    fun getTotalScoreFromADay(dateToSearch:LocalDate):LiveData<Long>

    /***
     * Returns the sum of difficulty of all finished tasks from a date
     */
    @Query("select sum(difficulty) from task where executionDate = :dateToSearch and finished")
    fun getAccomplishedScoreFromADay(dateToSearch: LocalDate):LiveData<Long>

    /**
     * Returns the sum of difficulty of all tasks from a range of dates
     */
    @Query("select sum(difficulty) from task where executionDate between :from and :to")
    fun getTotalScoreFromARange(from: LocalDate,to: LocalDate):LiveData<Long>
    /**
     * Returns the sum of difficulty of all finished tasks from a range of dates
     */
    @Query("select sum(difficulty) from task where finished and executionDate between :from and :to")
    fun getAccomplishedScoreFromARange(from: LocalDate,to: LocalDate):LiveData<Long>

    @Query("select sum(difficulty) from task where finished and executionDate between :from and :to")
    fun getAccomplishedScoreFromARangeNoLiveData(from: LocalDate,to: LocalDate): Long
    @Query("select sum(difficulty) from task where executionDate between :from and :to")
    fun getTotalScoreFromARangeNoLiveData(from: LocalDate,to: LocalDate):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task) : Long
    @Delete
    fun delete(task: Task)

}