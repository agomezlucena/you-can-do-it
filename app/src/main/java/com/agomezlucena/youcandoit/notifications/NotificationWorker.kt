package com.agomezlucena.youcandoit.notifications

import com.agomezlucena.youcandoit.task_managament.Task
import java.time.Duration

interface NotificationWorker {
    fun sendNotification (task: Task, duration: Duration)
    fun cancelNotification (tag: String)
}