package com.kapilguru.trainer.student.courseDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.courseDetails.StudentCourseDetailsRepository

class StudentCourseDetailsModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentCourseDetailsViewModel::class.java)) {
            return StudentCourseDetailsViewModel(
                StudentCourseDetailsRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}