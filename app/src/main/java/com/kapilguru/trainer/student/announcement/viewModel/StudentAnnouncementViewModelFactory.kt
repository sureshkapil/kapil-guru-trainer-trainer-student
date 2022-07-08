package com.kapilguru.trainer.student.announcement.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.announcement.StudentAnnouncementRepository

class StudentAnnouncementViewModelFactory(private val apiHelper: ApiHelper, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentAnnouncementViewModel::class.java)) {
            return StudentAnnouncementViewModel(
                StudentAnnouncementRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}