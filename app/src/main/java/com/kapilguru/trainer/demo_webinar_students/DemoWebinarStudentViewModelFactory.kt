package com.kapilguru.trainer.demo_webinar_students

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class DemoWebinarStudentViewModelFactory (private val apiHelper: ApiHelper,
                                          private val appliaction: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DemoWebinarStudentViewModel::class.java)) {
            return DemoWebinarStudentViewModel(
                DemoWebinarStudentRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}