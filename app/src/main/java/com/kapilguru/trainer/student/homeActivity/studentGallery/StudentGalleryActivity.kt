package com.kapilguru.trainer.student.homeActivity.studentGallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityStudentGalleryBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.showAppToast
import com.kapilguru.trainer.student.StudentBaseActivity
import com.kapilguru.trainer.student.homeActivity.models.PopularAndTrendingApi
import com.kapilguru.trainer.student.homeActivity.studentGallery.viewModel.StudentGalleryViewModel
import com.kapilguru.trainer.student.homeActivity.studentGallery.viewModel.StudentGalleryViewModelFactory

class StudentGalleryActivity : StudentBaseActivity(), StudentGalleryAdapter.CardItemClickListener {
    private val TAG = "StudentGalleryActivity"
    private lateinit var binding: ActivityStudentGalleryBinding
    private lateinit var viewModel: StudentGalleryViewModel
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var adapter: StudentGalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBar()
        observeViewModelData()
        getImages()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_gallery)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, StudentGalleryViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudentGalleryViewModel::class.java)
        setAdapter()
    }

    private fun setAdapter() {
        adapter = StudentGalleryAdapter(this,true)
        binding.rcView.adapter = adapter
    }

    private fun setCustomActionBar() {
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.gallery))
    }

    private fun observeViewModelData() {
        observeInformUser()
        observeShowLoadingIndicator()
        observeImagesList()
    }

    private fun observeInformUser() {
        viewModel.informUser.observe(this, Observer { message ->
            showAppToast(this, message)
        })
    }

    private fun observeShowLoadingIndicator() {
        viewModel.showLoadingIndicator.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                progressDialog.showLoadingDialog()
            } else {
                progressDialog.dismissLoadingDialog()
            }
        })
    }

    private fun observeImagesList() {
        viewModel.imagesList.observe(this, Observer { imagesList ->
            if (imagesList.isNotEmpty()) {
                adapter.imageList = imagesList
            }
        })
    }

    private fun getImages() {
        viewModel.getImages()
    }

    override fun onCardClicked(popularAndTrendingApi: PopularAndTrendingApi) {

    }

    companion object {
        fun launchActivity(activity: Activity) {
            activity.startActivity(Intent(activity, StudentGalleryActivity::class.java))
        }
    }
}