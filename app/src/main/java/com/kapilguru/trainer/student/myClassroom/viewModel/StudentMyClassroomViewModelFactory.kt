package com.kapilguru.trainer.student.myClassroom.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.myClassroom.StudentMyClassroomRepository

class StudentMyClassroomViewModelFactory(private val apiHelper: ApiHelper, val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentMyClassroomViewModel::class.java)) {
            return StudentMyClassroomViewModel(
                StudentMyClassroomRepository(
                    apiHelper
                ), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}