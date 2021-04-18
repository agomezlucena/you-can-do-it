package com.agomezlucena.youcandoit.di

import android.content.Context
import androidx.room.Room
import com.agomezlucena.youcandoit.data_managament.db.TaskDao
import com.agomezlucena.youcandoit.data_managament.db.YouCanDoITDatabase
import com.agomezlucena.youcandoit.data_managament.repos.TaskRepository
import com.agomezlucena.youcandoit.notifications.NotificationWorker
import com.agomezlucena.youcandoit.notifications.NotificationWorkerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TaskModule {
    private const val TASK_DB = "youcandoit.db"

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) = Room
            .databaseBuilder(context,YouCanDoITDatabase::class.java,TASK_DB)
            .build()

    @Singleton
    @Provides
    fun providesTaskDao(database:YouCanDoITDatabase) = database.taskDao()

    @Singleton
    @Provides
    fun providesNotificationWorker(@ApplicationContext context: Context) = NotificationWorkerImpl(context)

    @Singleton
    @Provides
    fun providesRepository(notificationWorker: NotificationWorker, dao:TaskDao) = TaskRepository(notificationWorker,dao)

}