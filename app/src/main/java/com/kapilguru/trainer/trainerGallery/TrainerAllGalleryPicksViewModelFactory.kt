package com.kapilguru.trainer.trainerGallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class TrainerAllGalleryPicksViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainerAllGalleyPicksViewModel::class.java)) {
            return TrainerAllGalleyPicksViewModel(TrainerAllGalleryPicksRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}