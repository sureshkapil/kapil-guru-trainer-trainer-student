package com.kapilguru.trainer.ui.courses.view_course.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.view_course.CourseDetailsRepository

class CourseDetailsModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseDetailsViewModel::class.java)) {
            return CourseDetailsViewModel(
                CourseDetailsRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}