package com.kapilguru.trainer.testimonials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddTrainerTestimonialBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import kotlinx.android.synthetic.main.trainer_testimonial_recycler_item_item.*

class AddTrainerTestimonial : BaseActivity() {

    lateinit var binding: ActivityAddTrainerTestimonialBinding
    lateinit var viewModel: TrainerTestimonialViewModel
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_trainer_testimonial)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, TrainerTestimonialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application))
            .get(TrainerTestimonialViewModel::class.java)
        dialog = CustomProgressDialog(this)
        binding.model = viewModel
        binding.lifecycleOwner = this
        setCustomActionBar()
        setclickListeners()
        observeVideModel()
    }

    fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.add_testimonial))
    }


    private fun setclickListeners() {
        binding.sumbit.setOnClickListener {
            viewModel.postTestimonial.value?.comments?.let { comments ->
                if (comments.trim().isNotEmpty()) viewModel.addTestimonials()
            }
        }
    }

    private fun observeVideModel() {
        viewModel.postTestimonialsResponse.observe(this, Observer {postTestimonialsResponse->
            when (postTestimonialsResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    if(postTestimonialsResponse.data?.status == 200) {
                        showToast()
                    }

                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun showToast() {
        showAppToast(this,"Testimonial  Added Successfully")
        navigateTotestimonial()
    }

    private fun navigateTotestimonial() {
        startActivity(Intent(this,TestimonialsActivity::class.java))
    }


}