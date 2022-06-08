package com.kapilguru.trainer.studentsList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.studentsList.StudentListRepository

class StudentListViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentListViewModel::class.java)) {
            return StudentListViewModel(
                StudentListRepository(
                    apiHelper
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}