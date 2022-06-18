package com.kapilguru.trainer.testimonials

import android.content.Intent
import com.kapilguru.trainer.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.ActivityTestimonialsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class TestimonialsActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestimonialsBinding
    lateinit var viewModel: TrainerTestimonialViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var adapgter: TestimonialReyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_testimonials)

        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, TrainerTestimonialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(TrainerTestimonialViewModel::class.java)
        dialog = CustomProgressDialog(this)
        setclickListeners()
        setRecycler()
        observeViewModel()
    }

    private fun setclickListeners() {
        binding.buttonTestimonial.setOnClickListener {
            launchAddTestimoialsAcitivty()
        }
    }


    private fun setRecycler() {
        adapgter = TestimonialReyclerAdapter()
        binding.galleryAllImagesRecy.adapter = adapgter
    }


    private fun observeViewModel() {
        viewModel.getTestimonials(StorePreferences(this).tenantId)

        viewModel.fetchTestimonialsResponse.observe(this, Observer { allTestimonialsData ->
            when (allTestimonialsData.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    allTestimonialsData.data?.fetchTestimoialsResponseApi?.let {
                        if (it.isNotEmpty()) {
                            setDataToAdapter(it)
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setDataToAdapter(list: List<FetchTestimonialsResponseApi>) {
        adapgter.setData(list as ArrayList<FetchTestimonialsResponseApi>)
    }

    private fun launchAddTestimoialsAcitivty() {
        startActivity(Intent(this, AddTrainerTestimonial::class.java))
    }
}