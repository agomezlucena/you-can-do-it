package com.agomezlucena.youcandoit.notifications

import android.content.Context
import com.agomezlucena.youcandoit.task_managament.Task
import java.time.Duration


/**
 * This class takes care about notification Managament
 * allows launch or cancel a notification
 * */

class NotificationWorkerImpl (private val context: Context) : NotificationWorker {
    override fun sendNotification (task:Task, duration: Duration){
        SendNotification.sendNotification(context,duration,task)
    }
    override fun cancelNotification (tag:String){
        CancelNotification.cancelNotification(context,tag)
    }
}