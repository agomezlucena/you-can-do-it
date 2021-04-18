package com.agomezlucena.youcandoit.di

import com.agomezlucena.youcandoit.notifications.NotificationWorker
import com.agomezlucena.youcandoit.notifications.NotificationWorkerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class TaskBindingsModule {
    @Binds
    abstract fun bindNotificationWorker(notificationWorker: NotificationWorkerImpl):NotificationWorker
}