package com.kapilguru.trainer.trainerGallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityTrainerAllGalleryPicksBinding
import com.kapilguru.trainer.network.RetrofitNetwork

class TrainerAllGalleryPicksActivity : AppCompatActivity() {

    lateinit var binding: ActivityTrainerAllGalleryPicksBinding
    lateinit var viewModel: TrainerAllGalleyPicksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_trainer_all_gallery_picks)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, TrainerAllGalleryPicksViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(TrainerAllGalleyPicksViewModel::class.java)
//        setSignUpDetailsFragment()
    }

}