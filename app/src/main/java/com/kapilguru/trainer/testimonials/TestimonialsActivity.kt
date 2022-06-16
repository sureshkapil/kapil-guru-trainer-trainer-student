package com.kapilguru.trainer.testimonials

import android.content.Intent
import com.kapilguru.trainer.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.databinding.ActivityTestimonialsBinding
import com.kapilguru.trainer.network.RetrofitNetwork

class TestimonialsActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestimonialsBinding
    lateinit var viewModel: TrainerTestimonialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_testimonials)

        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, TrainerTestimonialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(TrainerTestimonialViewModel::class.java)

        setclickListeners()
    }

    private fun setclickListeners() {
        binding.buttonTestimonial.setOnClickListener {
            launchAddTestimoialsAcitivty()
        }
    }

    private fun launchAddTestimoialsAcitivty() {
        startActivity(Intent(this, AddTrainerTestimonial::class.java))
    }
}