package com.agomezlucena.youcandoit.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.agomezlucena.youcandoit.task_managament.Task
import java.time.Duration
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.task
import com.agomezlucena.youcandoit.task_managament.Difficulty
import java.time.LocalDate
import java.time.LocalTime

/**
 * This class permits launch a notification using workmanager library, this allows send notifications
 * in the background without use
 */

class SendNotification(
        context : Context,
        parameters : WorkerParameters)
: Worker (context,parameters) {

    override fun doWork(): Result {
        val task = task {
            id = inputData.getInt("id",0)
            title = inputData.getString("title")?:""
            description = inputData.getString("description")?:""
            difficulty = Difficulty.valueOf(inputData.getString("difficulty")?:"EASY")
            finished = inputData.getBoolean("finished",false)
            executionDate = LocalDate.parse(inputData.getString("execution_date"))
            executionHour = LocalTime.parse(inputData.getString("execution_hour"))
        }

        launchNotification(task)

        return Result.success()
    }

    fun launchNotification(task : Task){
        val notificationChannelName = applicationContext.getString(R.string.notification_channel_id)
        val notificationChannel  = NotificationChannel(notificationChannelName,notificationChannelName,NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        val notification = NotificationCompat.Builder(applicationContext,notificationChannelName).apply {
            setSmallIcon(R.drawable.ic_icon)
            setContentTitle(applicationContext.getString(R.string.notification_title))
            setContentText(applicationContext.getString(R.string.notification_description,task.title))
        }.build()
        notificationManager.notify(task.id?:0,notification)
    }

    companion object {
        fun sendNotification(context : Context,duration:Duration, task: Task){
            if(task.finished.not()) {
                val data = generateData(task)
                val notification = OneTimeWorkRequest.Builder(SendNotification::class.java)
                    .setInitialDelay(duration)
                    .addTag(task.id!!.toString())
                    .setInputData(data)
                    .build()
                val instance = WorkManager.getInstance(context)
                instance.enqueue(notification)
            }
        }
        private fun generateData(task: Task) : Data{
            return Data.Builder()
                .putInt("id",task.id?:0)
                .putString("title",task.title)
                .putString("description",task.description)
                .putString("execution_date",task.executionDate.toString())
                .putString("execution_hour",task.executionHour.toString())
                .putString("difficulty",task.difficulty.name)
                .putBoolean("finished",task.finished)
                .build()
        }
    }
}