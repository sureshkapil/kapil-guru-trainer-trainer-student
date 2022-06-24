package com.kapilguru.trainer.student.myClassRoomDetails.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.myClassRoomDetails.StudentMyClassRoomDetailsRepository

class StudentMyClassDetailsFactory(private val apiHelper: ApiHelper, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentMyClassDetailsViewModel::class.java)) {
            return StudentMyClassDetailsViewModel(
                StudentMyClassRoomDetailsRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}