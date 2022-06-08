package com.kapilguru.trainer.exams.conductExams.createQuestionPaper.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.CreateQuestionPaperRepository

class CreateQuestionPaperViewModelFactory(private val apiHelper: ApiHelper, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateQuestionPaperViewModel::class.java)) {
            return CreateQuestionPaperViewModel(
                CreateQuestionPaperRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}