package com.agomezlucena.youcandoit.data_managament.repos

import com.agomezlucena.youcandoit.notifications.NotificationWorker
import com.agomezlucena.youcandoit.task_managament.Task
import java.time.Duration

class NotificationWorkerMock : NotificationWorker {
    override fun sendNotification(task: Task, duration: Duration) {

    }

    override fun cancelNotification(tag: String) {

    }
}