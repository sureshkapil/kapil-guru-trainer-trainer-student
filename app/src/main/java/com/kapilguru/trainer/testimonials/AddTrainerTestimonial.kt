package com.kapilguru.trainer.testimonials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityAddTrainerTestimonialBinding
import com.kapilguru.trainer.databinding.ActivityTrainerAllGalleryPicksBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.trainerGallery.TrainerAllGalleryPicksViewModelFactory
import com.kapilguru.trainer.trainerGallery.TrainerAllGalleyPicksViewModel

class AddTrainerTestimonial : AppCompatActivity() {

    lateinit var binding: ActivityAddTrainerTestimonialBinding
    lateinit var viewModel: TrainerTestimonialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_trainer_testimonial)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, TrainerTestimonialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(TrainerTestimonialViewModel::class.java)
        setclickListeners()
    }

    private fun setclickListeners() {
        binding.sumbit.setOnClickListener {
         // make view model checks and api call
        }
    }
}