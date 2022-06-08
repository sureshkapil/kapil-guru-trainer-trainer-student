package com.kapilguru.trainer.student.homeActivity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class StudentHomeViewModelFactory(private val apiHelper: ApiHelper, private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentHomeViewModel::class.java)) {
            return StudentHomeViewModel(StudentHomeRepository(apiHelper), app) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}