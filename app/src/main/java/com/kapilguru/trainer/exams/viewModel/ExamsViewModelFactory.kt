package com.kapilguru.trainer.exams.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.ExamsRepository

class ExamsViewModelFactory(private val apiHelper: ApiHelper, var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamsViewModel::class.java)) {
            return ExamsViewModel(
                ExamsRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}