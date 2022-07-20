package com.kapilguru.trainer.student.homeActivity.studentGallery.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.homeActivity.studentGallery.StudentGalleryRepository

class StudentGalleryViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentGalleryViewModel::class.java)) {
            return StudentGalleryViewModel(
                StudentGalleryRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}