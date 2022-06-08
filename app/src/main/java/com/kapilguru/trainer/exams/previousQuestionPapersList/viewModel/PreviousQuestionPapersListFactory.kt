package com.kapilguru.trainer.exams.previousQuestionPapersList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.previousQuestionPapersList.PreviousQuestionPapersListRepository

class PreviousQuestionPapersListFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviousQuestionPapersListViewModel::class.java)) {
            return PreviousQuestionPapersListViewModel(PreviousQuestionPapersListRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}