package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.AddGuestLectureRepository

class AddGuestLectureViewModelFactory(private val apiHelper: ApiHelper, private val appliaction: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGuestLectureViewModel::class.java)) {
            return AddGuestLectureViewModel(AddGuestLectureRepository(apiHelper), appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}