package com.kapilguru.trainer.ui.guestLectures.guestLectureDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GuestLectureDetailsViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuestLectureDetailsViewModel::class.java)) {
            return GuestLectureDetailsViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}