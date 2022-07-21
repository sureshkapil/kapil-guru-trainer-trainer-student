package com.kapilguru.trainer.student.allExamsList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityStudentAllExamsListBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.StudentBaseActivity
import com.kapilguru.trainer.student.exam.StudentViewExamResult
import com.kapilguru.trainer.student.exam.model.StudentQuestionsRequest
import com.kapilguru.trainer.student.exam.questionPaper.StudentQuestionPaperNewActivity
import com.kapilguru.trainer.student.myClassRoomDetails.exam.StudentExamListAdapter
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListItemResData
import com.kapilguru.trainer.studentExamBatchResult.StudentReportRequest

class StudentAllExamsListActivity : StudentBaseActivity(), StudentExamListAdapter.ExamItemClickListener {

    lateinit var binding: ActivityStudentAllExamsListBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: StudentAllExamsViewModel
    lateinit var adapter: StudentExamListAdapter
    var studentId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBar()
        observeViewModelData()
    }

    private fun observeViewModelData() {
        viewModel.getCompletionRequests(studentId.toString())

        viewModel.examsListResponseAPi.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    it.data?.questionPaperList?.let { questionPaperList ->
                        if (questionPaperList.isNotEmpty()) {
                            showNoDataVisibility(false)
                            setDataToAdapter(questionPaperList)
                        } else {
                            showNoDataVisibility(true)
                        }
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun showNoDataVisibility(boolean: Boolean) {
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_exams)
        binding.noDataAvailable.tvNoData.visibility = if (boolean) View.VISIBLE else View.GONE
    }

    private fun setDataToAdapter(questionPaperList: ArrayList<StudentQuestionPaperListItemResData>) {
        adapter.setData(null, questionPaperList)
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_all_exams_list)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, StudentAllExamsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudentAllExamsViewModel::class.java)
        binding.lifecycleOwner = this
        adapter = StudentExamListAdapter(this)
        binding.examListRecycler.adapter = adapter
        studentId = StorePreferences(this).userId
    }

    private fun setCustomActionBar() {
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.all_exams))
    }


    override fun onStartExamClicked(questionPaper: StudentQuestionPaperListItemResData) {
        StudentQuestionPaperNewActivity.launchActivity(this, questionPaper)
    }


    override fun onViewResultClicked(questionPaper: StudentQuestionPaperListItemResData) {
        launchViewResult(questionPaper)
    }

    private fun launchViewResult(questionPaper: StudentQuestionPaperListItemResData) {
        val examReportRequest = StudentReportRequest(
            batchId = questionPaper.batchId,
            examId = questionPaper.examId,
            questionPaperId = questionPaper.questionPaperId,
            studentId = questionPaper.studentId,
            courseId = questionPaper.courseId,
            trainerId = questionPaper.trainerId
        )
        val questionRequest = StudentQuestionsRequest(
            batchId = questionPaper.batchId,
            examId = questionPaper.examId,
            questionPaperId = questionPaper.questionPaperId,
            studentId = questionPaper.studentId,
        )
        startActivity(
            Intent(this, StudentViewExamResult::class.java).putExtra(PARAM_REPORTS_REQUEST, examReportRequest).putExtra(PARAM_QUESTIONS_REQUEST, questionRequest)
        )
    }

}