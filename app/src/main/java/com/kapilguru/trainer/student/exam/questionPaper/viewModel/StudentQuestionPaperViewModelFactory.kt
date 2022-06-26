package com.kapilguru.trainer.student.exam.questionPaper.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.exam.questionPaper.StudentQuestionPaperRepository

class StudentQuestionPaperViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentQuestionPaperViewModel::class.java)) {
            return StudentQuestionPaperViewModel(
                StudentQuestionPaperRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}