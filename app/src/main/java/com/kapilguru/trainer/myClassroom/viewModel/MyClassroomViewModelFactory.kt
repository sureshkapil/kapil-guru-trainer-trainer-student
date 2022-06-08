package com.kapilguru.trainer.myClassroom.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.myClassroom.MyClassroomRepository

class MyClassroomViewModelFactory(private val apiHelper: ApiHelper, val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyClassroomViewModel::class.java)) {
            return MyClassroomViewModel(
                MyClassroomRepository(
                    apiHelper
                ), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}