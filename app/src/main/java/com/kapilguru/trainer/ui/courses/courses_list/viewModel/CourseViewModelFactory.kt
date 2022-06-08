package com.kapilguru.trainer.ui.courses.courses_list.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.courses_list.CourseRepository

class CourseViewModelFactory(private val apiHelper: ApiHelper,
  private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            return CourseViewModel(
                CourseRepository(apiHelper),application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}