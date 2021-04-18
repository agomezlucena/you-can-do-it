package com.agomezlucena.youcandoit.data_managament.repos

import androidx.lifecycle.LiveData
import com.agomezlucena.youcandoit.data_managament.db.TaskDao
import com.agomezlucena.youcandoit.daysToDay
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.notifications.NotificationWorker
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.Task
import kotlinx.coroutines.Dispatchers
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class TaskRepository @Inject constructor(private val notificationWorker: NotificationWorker, private val dao:TaskDao) {

    fun getDateUnfinishedTask(date: LocalDate) = dao.getUnfinishedTaskFromADay(date)
    fun getTodayUnfinishedTask() = dao.getUnfinishedTaskFromADay(LocalDate.now())
    fun getUnfinishedTaskFromARange(startDate: LocalDate,endDate: LocalDate) = dao.getUnfinishedTaskFromARangeOfDates(startDate,endDate)

    fun getThisWeekUnfinishedTasks() : LiveData<List<Task>>{
        val today = LocalDate.now()
        val monday = today.plusDays(today.daysToDay(DayOfWeek.MONDAY))
        val sunday = today.plusDays(today.daysToDay(DayOfWeek.SUNDAY))
        return dao.getUnfinishedTaskFromARangeOfDates(monday,sunday)
    }

    fun getThisMonthUnfinishedTasks() : LiveData<List<Task>>{
        val today = LocalDate.now()
        val firstOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth())
        return dao.getUnfinishedTaskFromARangeOfDates(firstOfThisMonth,lastDayOfThisMonth)
    }

    fun getTodayAccomplishedScore() = dao.getAccomplishedScoreFromADay(LocalDate.now())
    fun getTodayTotalScore() = dao.getTotalScoreFromADay(LocalDate.now())
    
    fun getThisWeekAccomplishedScore() : LiveData<Long>{
        val today = LocalDate.now()
        val monday = today.plusDays(today.daysToDay(DayOfWeek.MONDAY))
        val sunday = today.plusDays(today.daysToDay(DayOfWeek.SUNDAY))
        return dao.getAccomplishedScoreFromARange(monday,sunday)
    }

    fun getThisWeekTotalScore() : LiveData<Long> {
        val today = LocalDate.now()
        val monday = today.plusDays(today.daysToDay(DayOfWeek.MONDAY))
        val sunday = today.plusDays(today.daysToDay(DayOfWeek.SUNDAY))
        return dao.getTotalScoreFromARange(monday,sunday)
    }

    fun getThisMonthAccomplishedScore() : LiveData<Long>{
        val today = LocalDate.now()
        val firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())
        return dao.getAccomplishedScoreFromARange(firstDayOfMonth,lastDayOfMonth)
    }

    fun getThisMonthTotalScore():LiveData<Long>{
        val today = LocalDate.now()
        val firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())
        return dao.getTotalScoreFromARange(firstDayOfMonth,lastDayOfMonth)
    }

    fun getThisYearAccomplishedScore () : LiveData<Long>{
        val firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        val lastDayOfYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear())
        return dao.getAccomplishedScoreFromARange(firstDayOfYear,lastDayOfYear)
    }

    fun getThisYearTotalScore():LiveData<Long>{
        val firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        val lastDayOfYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear())
        return dao.getTotalScoreFromARange(firstDayOfYear,lastDayOfYear)
    }

    fun getThisWeekScorePerDay():LiveData<List<DayScore>>{
        val today = LocalDate.now()
        val monday = today.plusDays(today.daysToDay(DayOfWeek.MONDAY))
        val sunday = today.plusDays(today.daysToDay(DayOfWeek.SUNDAY))
        return dao.getAccomplishedScorePerDaysInRange(monday,sunday)
    }

    fun getThisMonthScorePerDay():LiveData<List<DayScore>>{
        val today = LocalDate.now()
        val firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())
        return dao.getAccomplishedScorePerDaysInRange(firstDayOfMonth,lastDayOfMonth)
    }

    fun getThisYearScorePerDay():LiveData<List<DayScore>>{
        val firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        val lastDayOfYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear())
        return dao.getAccomplishedScorePerDaysInRange(firstDayOfYear,lastDayOfYear)
    }

    fun getThisWeekAccomplishedScoreNoLiveData() : Long{
        val today = LocalDate.now()
        val monday = today.plusDays(today.daysToDay(DayOfWeek.MONDAY))
        val sunday = today.plusDays(today.daysToDay(DayOfWeek.SUNDAY))
        return dao.getAccomplishedScoreFromARangeNoLiveData(monday,sunday)
    }

    fun getThisWeekTotalScoreNoLiveData() : Long {
        val today = LocalDate.now()
        val monday = today.plusDays(today.daysToDay(DayOfWeek.MONDAY))
        val sunday = today.plusDays(today.daysToDay(DayOfWeek.SUNDAY))
        return dao.getTotalScoreFromARangeNoLiveData(monday,sunday)
    }

    fun getThisMonthAccomplishedScoreNoLiveData() : Long{
        val today = LocalDate.now()
        val firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())
        return dao.getAccomplishedScoreFromARangeNoLiveData(firstDayOfMonth,lastDayOfMonth)
    }

    fun getThisMonthTotalScoreNoLiveData():Long{
        val today = LocalDate.now()
        val firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())
        return dao.getTotalScoreFromARangeNoLiveData(firstDayOfMonth,lastDayOfMonth)
    }

    fun getThisYearAccomplishedScoreNoLiveData() : Long{
        val firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        val lastDayOfYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear())
        return dao.getAccomplishedScoreFromARangeNoLiveData(firstDayOfYear,lastDayOfYear)
    }

    fun getThisYearTotalScoreNoLiveData():Long{
        val firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        val lastDayOfYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear())
        return dao.getTotalScoreFromARangeNoLiveData(firstDayOfYear,lastDayOfYear)
    }

    fun getDateTasks(date: LocalDate) = dao.getTasksFromADay(date)

    fun insert(task: Task){
       val id = dao.insert(task)
       launchCoroutine(Dispatchers.IO){
           task.id = id.toInt()
           val taskDuration = Duration.between(LocalDateTime.now(),LocalDateTime.of(task.executionDate,task.executionHour))
           notificationWorker.sendNotification(task,taskDuration)
       }
    }
    fun update(task: Task) {
        dao.insert(task)
        launchCoroutine(Dispatchers.IO){
            notificationWorker.cancelNotification(task.id!!.toString())
            val taskDuration = Duration.between(LocalDateTime.now(),LocalDateTime.of(task.executionDate,task.executionHour))
            notificationWorker.sendNotification(task,taskDuration)
        }
    }
    fun remove(task: Task) {
        dao.delete(task)
        launchCoroutine(Dispatchers.IO){
            notificationWorker.cancelNotification(task.id!!.toString())
        }
    }



}