package com.kapilguru.trainer.batchExamReports

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivitySelectedBatchExamReportsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.studentExamBatchResult.StudentReportRequest
import com.kapilguru.trainer.studentExamBatchResult.ViewStudentReport
import java.lang.reflect.Type

class SelectedBatchExamReportsActivity : BaseActivity(), ExamBatchStudentsAdapter.ExamBatchStudentsAdapterCard {
    private val TAG = "SelectedBatchExamReport"
    lateinit var viewModel: SelectedBatchExamReportsViewModel
    lateinit var viewBinding: ActivitySelectedBatchExamReportsBinding
    lateinit var dialog: CustomProgressDialog
    lateinit var adapter: ExamBatchStudentsAdapter
    var batchReportsRequestModel:  BatchReportsRequestModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_selected_batch_exam_reports)
        viewModel = ViewModelProvider(
            this, SelectedBatchExamReportsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(
            SelectedBatchExamReportsViewModel::class.java
        )
        setActionbarBackListener(this, viewBinding.actionbar, getString(R.string.exam_results))
        viewBinding.viewModel = viewModel
        viewBinding.lifecycleOwner = this
        getIntentData()
        setRecyclerAdapter()
        dialog = CustomProgressDialog(this)
        observeViewModelData()
    }

    private fun setRecyclerAdapter() {
        adapter = ExamBatchStudentsAdapter(this)
        viewBinding.recyclerview.adapter = adapter
    }

    private fun observeViewModelData() {
        batchReportsRequestModel?.let {
            viewModel.getBatchReports(it)
        }

        viewModel.batchStudentReportApi.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val examResults = response.data?.batchStudentReportResponse?.examResults
                    val gson = Gson()
                    examResults?.let { it ->
                        val examResultType: Type =
                            object : TypeToken<BatchExamResultModel?>() {}.type
                        val gsonClassConverterExamResults: BatchExamResultModel = gson.fromJson(it, examResultType)
                        gsonClassConverterExamResults.let {
                            viewModel.batchExamReportModel.value = it
                        }
                    }
                    val batchExamStudents = response.data?.batchStudentReportResponse?.examStudents
                    batchExamStudents?.let {
                        val examStudentType: Type =
                            object : TypeToken<List<BatchStudentsItem>?>() {}.type
                        val gsonClassConverterExamStudents: List<BatchStudentsItem> = gson.fromJson(it, examStudentType)
                        gsonClassConverterExamStudents.let {
                           viewModel.batchExamStudents.value = it
                            upDateAdapter()
                        }
                    }
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }

        })
    }

    private fun getIntentData() {
        batchReportsRequestModel = intent.getParcelableExtra<BatchReportsRequestModel>(Batch_Reports_Request_PARAM)
        viewBinding.courseTitle = intent.getStringExtra(COURSE_TITLE_PARAM)
        viewBinding.batchCode = intent.getStringExtra(PARAM_BATCH_CODE)
    }

    private fun upDateAdapter() {
        viewModel.batchExamStudents.value?.let {
            adapter.batchExamStudentsList = it
        }
    }

    override fun onCardClick(batchStudentsItem: BatchStudentsItem) {
        val studentReportRequest =
         batchReportsRequestModel?.let {
            StudentReportRequest().apply {
                this.examId = it.examId
                this.batchId = it.batchId
                this.courseId = it.courseId
                this.questionPaperId = it.questionPaperId
                this.trainerId = it.trainerId
                this.studentId = batchStudentsItem.studentId
            }
        }
        val intent = Intent(this, ViewStudentReport::class.java)
        intent.putExtra(Batch_STUDENT_Reports_Request_PARAM,studentReportRequest)
        startActivity(intent)
    }

}