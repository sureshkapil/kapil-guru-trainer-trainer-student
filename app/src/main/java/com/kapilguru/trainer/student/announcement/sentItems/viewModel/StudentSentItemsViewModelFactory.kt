package com.kapilguru.trainer.student.announcement.sentItems.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.student.announcement.StudentAnnouncementRepository

class StudentSentItemsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentSentItemsViewModel::class.java)) {
            return StudentSentItemsViewModel(
                StudentAnnouncementRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}