package com.kapilguru.trainer.studyMaterial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class StudyMaterialViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudyMaterialViewModel::class.java)) {
            return StudyMaterialViewModel(StudyMaterialRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}