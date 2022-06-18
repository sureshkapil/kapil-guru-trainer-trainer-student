package com.kapilguru.trainer.testimonials

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper

class TrainerTestimonialViewModelFactory(private val apiHelper: ApiHelper, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainerTestimonialViewModel::class.java)) {
            return TrainerTestimonialViewModel(TrainerTestimonialRepository(apiHelper),application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}