package com.kapilguru.trainer.ui.courses.addcourse.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.addcourse.AddCourseRepository

class AddCourseViewModelFactory(
    private val apiHelper: ApiHelper,
    private val appliaction: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCourseViewModel::class.java)) {
            return AddCourseViewModel(
                    AddCourseRepository(apiHelper),
                appliaction
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}