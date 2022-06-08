package com.kapilguru.trainer.exams.previousQuestionsList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.exams.previousQuestionsList.PreviousQuestionsListRepository

class PreviousQuestionsListViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviousQuestionsListViewModel::class.java)) {
            return PreviousQuestionsListViewModel(
                PreviousQuestionsListRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}