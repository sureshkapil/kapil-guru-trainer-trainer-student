package com.kapilguru.trainer.testimonials

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityTestimonialsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class TestimonialsActivity : BaseActivity(), TestimonialReyclerAdapter.ItemClickListener,
    TwoButtonDialogInteractor{

    private var isFromStatusApproval: Boolean= false
    private var testimonialId: Int?=null
    lateinit var binding: ActivityTestimonialsBinding
    lateinit var viewModel: TrainerTestimonialViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var adapgter: TestimonialReyclerAdapter
    private  val TAG = "TestimonialsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_testimonials)

        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, TrainerTestimonialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(TrainerTestimonialViewModel::class.java)
        dialog = CustomProgressDialog(this)
        setCustomActionBar()
        setclickListeners()
        setRecycler()
        observeViewModel()
    }


    fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.customActionBar, getString(R.string.testimonials))
    }

    private fun setclickListeners() {
        binding.buttonTestimonial.setOnClickListener {
            launchAddTestimoialsAcitivty()
        }
    }


    private fun setRecycler() {
        adapgter = TestimonialReyclerAdapter(this)
        binding.galleryAllImagesRecy.adapter = adapgter
    }


    private fun observeViewModel() {
        fetchTestimonials()
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

        viewModel.updateTestimonialStatusResponse.observe(this, Observer { updateTestimonialStatusResponse ->
            when (updateTestimonialStatusResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    updateTestimonialStatusResponse.data?.status?.let { status ->
                        if (status == 200) {
                            showToast()
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun showToast() {
        showAppToast(this,"Testimonial status updated")
        fetchTestimonials()
    }

    private fun fetchTestimonials() {
        viewModel.getTestimonials(StorePreferences(this).tenantId)
    }

    private fun setDataToAdapter(list: List<FetchTestimonialsResponseApi>) {
        adapgter.setData(list as ArrayList<FetchTestimonialsResponseApi>)
    }

    private fun launchAddTestimoialsAcitivty() {
        startActivity(Intent(this, AddTrainerTestimonial::class.java))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fetchTestimonials()
    }

    override fun onApproveClickListener(fetchTestimonialsResponseApi: FetchTestimonialsResponseApi) {
        testimonialId = fetchTestimonialsResponseApi.id
        isFromStatusApproval = true
        showTwoButtonDialog(getString(R.string.you_want_to_approve_testimonial))
    }

    override fun onDeleteClickListener(fetchTestimonialsResponseApi: FetchTestimonialsResponseApi) {
        testimonialId = fetchTestimonialsResponseApi.id
        isFromStatusApproval = false
        showTwoButtonDialog(getString(R.string.you_want_to_delete_testimonial))
    }

    private fun showTwoButtonDialog(text: String) {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: TwoButtonDialog = TwoButtonDialog.newInstance(
            String.format(text),this)
        editNameDialogFragment.show(fm, "two_button_dialog")
    }

    override fun onNegativeClick() {
    // shut the fuck up
    }

    override fun onPositiveClick() {
        if (isFromStatusApproval) {
            testimonialId?.let { id ->
                viewModel.updateStatus(id.toString(), TestimonialApproveRequest())
            }
        } else {
            testimonialId?.let { id ->
                viewModel.deletTestimonial(id.toString())
            }
        }
    }
}