package com.kapilguru.trainer.student.allExamsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class StudentAllExamsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentAllExamsViewModel::class.java)) {
            return StudentAllExamsViewModel(AllStudentExamsListRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}