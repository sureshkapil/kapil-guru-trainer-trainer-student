package com.kapilguru.trainer.exams.scheduledExams

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class ScheduleExamsModelFactory(private val apiHelper: ApiHelper, var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleExamsViewModel::class.java)) {
            return ScheduleExamsViewModel(
                ScheduleExamsRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}