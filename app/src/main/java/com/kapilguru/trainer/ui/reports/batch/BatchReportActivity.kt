package com.kapilguru.trainer.ui.reports.batch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.COURSE_INFO_PARAM
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityBatchRepotBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse
import com.kapilguru.trainer.ui.reports.batch.viewModel.BatchReportViewModel
import com.kapilguru.trainer.ui.reports.batch.viewModel.BatchReportViewModelFactory
import com.kapilguru.trainer.ui.reports.viewModel.ReportsViewModel

class BatchReportActivity : AppCompatActivity() {
    lateinit var binding : ActivityBatchRepotBinding
    lateinit var viewModel : BatchReportViewModel
    lateinit var adapter : BatchReportAdapter
    lateinit var progressDialog : CustomProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_batch_repot)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this,BatchReportViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(BatchReportViewModel::class.java)
        getIntentData()
        setSupportActionBar()
        setAdapter()
        observeViewModelData()
        getBatchList()
    }

    private fun getIntentData(){
        viewModel.courseData = intent.getParcelableExtra(COURSE_INFO_PARAM)
    }

    private fun setSupportActionBar(){
        supportActionBar?.title = viewModel.courseData?.courseTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun setAdapter(){
        adapter = BatchReportAdapter()
        binding.rvBatch.adapter = adapter
    }

    private fun observeViewModelData(){
        viewModel.batchListResponse.observe(this, Observer {batchListApiRes->
            when(batchListApiRes.status){
                Status.LOADING->{
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS->{
                    batchListApiRes?.data?.let{ batchListApi ->
                        adapter.setData(batchListApi.batchListResponse as ArrayList<BatchListResponse>)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR->{
                    progressDialog.dismissLoadingDialog()
                }
            }

        })
    }

    fun getBatchList(){
        val courseId = viewModel.courseData?.courseId!!
        val trainerId =viewModel.courseData?.trainerId!!
        val batchListReq = BatchListApiRequest(trainerId,courseId.toString())
        viewModel.getBatchListResponse(batchListReq)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                true
            }else -> return super.onOptionsItemSelected(item)
        }
    }

}