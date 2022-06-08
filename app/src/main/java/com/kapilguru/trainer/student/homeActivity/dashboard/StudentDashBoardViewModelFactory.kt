package com.kapilguru.trainer.student.homeActivity.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class StudentDashBoardViewModelFactory(private val apiHelper: ApiHelper,
                                       private val appliaction: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentDashBoardViewModel::class.java)) {
            return StudentDashBoardViewModel(
                StudentHomeScreenRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}