package com.kapilguru.trainer.exams.createQuestion.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.createQuestion.CreateQuestionRepository

class CreateQuestionViewModelFactory(private val apiHelper: ApiHelper, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateQuestionViewModel::class.java)) {
            return CreateQuestionViewModel(CreateQuestionRepository(apiHelper), application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}