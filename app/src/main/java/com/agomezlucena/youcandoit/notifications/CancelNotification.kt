package com.agomezlucena.youcandoit.notifications

import android.content.Context
import androidx.work.WorkManager

class CancelNotification {
    companion object {
        fun cancelNotification(context: Context,tag:String){
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
        }
    }
}