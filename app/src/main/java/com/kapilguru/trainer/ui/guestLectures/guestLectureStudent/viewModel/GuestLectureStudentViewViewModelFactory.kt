package com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.GuestLectureStudentViewRepository

class GuestLectureStudentViewViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuestLectureStudentViewViewModel::class.java)) {
            return GuestLectureStudentViewViewModel(GuestLectureStudentViewRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}