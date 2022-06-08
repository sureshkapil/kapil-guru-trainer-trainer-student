package com.kapilguru.trainer.ui.guestLectures.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.guestLectures.GuestLectureRepository

class GuestLectureViewModelFactory(private val apiHelper: ApiHelper,
                                   private val appliaction: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuestLectureViewModel::class.java)) {
            return GuestLectureViewModel(
                GuestLectureRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}