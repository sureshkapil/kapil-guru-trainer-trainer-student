package com.kapilguru.trainer.exams.scheduledExams

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.batchExamReports.BatchReportsRequestModel
import com.kapilguru.trainer.batchExamReports.SelectedBatchExamReportsActivity
import com.kapilguru.trainer.databinding.FragmentScheduledExamsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class ScheduledExamsActivity : BaseActivity(), ScheduleExamsAdapter.ScheduleExamsSelectedBatchListener {
    private val TAG = "ConductExamsFragment"
    lateinit var viewModel: ScheduleExamsViewModel
    lateinit var adapter: ScheduleExamsAdapter
    lateinit var progressDialog: CustomProgressDialog
    var selectedCourse = CourseResponse()
    lateinit var binding: FragmentScheduledExamsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_scheduled_exams)
        viewModel = ViewModelProvider(this, ScheduleExamsModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(ScheduleExamsViewModel::class.java)
        binding.lifecycleOwner = this
        setActionbarBackListener(this, binding.actionbar, getString(R.string.exam_results))
        progressDialog = CustomProgressDialog(this)
        setRecyclerAdapters()
        viewModelObervers()
    }

    private fun setRecyclerAdapters() {
        adapter = ScheduleExamsAdapter(this)
        binding.schedueleExamsItem.adapter = adapter
        viewModel.getScheduleExamsList()
    }



    private fun viewModelObervers() {
        viewModel.scheduleExamsAPi.observe(this, Observer { scheduleExamsAPi ->
            when (scheduleExamsAPi.status) {

                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    scheduleExamsAPi.data?.scheduleExamsResponse?.let { scheduleExamsResponseList ->
                        adapter.scheduleExamsResponseList = scheduleExamsResponseList as ArrayList<ScheduleExamsResponse>
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }
        })
    }

    override fun onBatchSelected(scheduleExamsResponse: ScheduleExamsResponse) {
      val batchReportsRequestModel = BatchReportsRequestModel().apply {
            trainerId = scheduleExamsResponse.trainerId
            courseId = scheduleExamsResponse.courseId
            batchId = scheduleExamsResponse.batchId
            examId = scheduleExamsResponse.examId
            questionPaperId = scheduleExamsResponse.questionPaperId
        }
        val intent = Intent(this, SelectedBatchExamReportsActivity::class.java)
        intent.putExtra(Batch_Reports_Request_PARAM,batchReportsRequestModel)
        intent.putExtra(COURSE_TITLE_PARAM,scheduleExamsResponse.courseTitle)
        intent.putExtra(PARAM_BATCH_CODE,scheduleExamsResponse.batchCode)
        startActivity(intent)
    }

}