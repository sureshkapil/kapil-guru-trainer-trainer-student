package com.kapilguru.trainer.studentExamBatchResult

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentViewAnswerSheetBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import kotlinx.android.synthetic.main.fragment_view_answer_sheet.*
import java.lang.reflect.Type

/**
 * A simple [Fragment] subclass.
 * Use the [ViewAnswerSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewAnswerSheetActivity : BaseActivity(),QuestionClickListener {
    lateinit var binding: FragmentViewAnswerSheetBinding
    lateinit var viewModel: StudentExamBatchResultViewModel
    lateinit var progressDialog: CustomProgressDialog
     var studentReportRequest:StudentReportRequest?= null
    lateinit var studentQuestionRecycler: StudentQuestionRecycler
    private val TAG = "ViewAnswerSheetFragment"
    private var counter = 0
    private var maxSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_view_answer_sheet)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, StudentExamBatchResultViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(StudentExamBatchResultViewModel::class.java)
        progressDialog = CustomProgressDialog(this)
        setSupportActionBar()
        fetchIntentSharedData()
        setUpRecycler()
        observeViewModel()
    }

    private fun setSupportActionBar() {
        setActionbarBackListener(this,binding.actionbar,getString(R.string.exam_results))
    }

    private fun fetchIntentSharedData() {
        studentReportRequest = intent?.getParcelableExtra<StudentReportRequest>(Batch_STUDENT_Reports_Request_PARAM)
        binding.studentName = intent?.getStringExtra(STUDENT_NAME_PARAM)
        binding.studentCode = intent?.getStringExtra(STUDENT_CODE_PARAM)
    }

    private fun setUpRecycler() {
        studentQuestionRecycler = StudentQuestionRecycler(this)
        questionsRecycler.adapter = studentQuestionRecycler
    }

    private fun observeViewModel() {
        studentReportRequest?.let {
            val studentExamPaperRequest = StudentExamPaperRequest().apply {
                this.batchId = it.batchId
                this.courseId = it.courseId
                this.questionPaperId = it.questionPaperId
                this.studentId = it.studentId
            }
            viewModel.getExamResult(studentExamPaperRequest)
        }

        viewModel.studentAnswerSheetApi.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    val studentsInfo = response.data?.studentAnswerSheetResponse?.questionsResponses
                    val gson = Gson()
                    studentsInfo?.let { it ->
                        val studentResultType: Type =
                            object : TypeToken<List<QuestionPaperResponse?>?>() {}.type
                        val studentResult: List<QuestionPaperResponse> = gson.fromJson(studentsInfo.toString(), studentResultType)
                        studentResult.let { studentResult ->
                            viewModel.questionPaperResponseList.value = studentResult
                            maxSize = studentResult.size
                            /*val questionPaperResponse =  arrayListOf<QuestionPaperResponse>()
                            studentResult.forEachIndexed { index, ele ->
                                if (index == 0) {
                                    ele.apply {
                                        this.correctOpt = "opt_2"
                                        this.selectedOpt = "opt_2"
                                    }
                                } else if (index == 1) {
                                    ele.apply {
                                        this.correctOpt = "opt_1"
                                        this.selectedOpt = "opt_2"
                                    }
                                } else if (index == 2) {
                                    ele.apply {
                                        this.correctOpt = "opt_3"
                                        this.selectedOpt = null
                                    }
                                } else if (index == 3) {
                                    ele.apply {
                                        this.correctOpt = "opt_4"
                                        this.selectedOpt = "opt_4"
                                    }
                                }*/
//                                questionPaperResponse.add(ele)
//                            }
//                            Log.d(TAG, "observeViewModel: ${questionPaperResponse}")
                            studentQuestionRecycler.questionPaperResponse = studentResult as ArrayList<QuestionPaperResponse>
//                            studentQuestionRecycler.questionPaperResponse = questionPaperResponse
                        }
                    }
//                    var studentInfoType = studentsInfo
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    if (response.data?.status != 200) {

                    } else {

                    }
                }
            }
        })
    }

    override fun onQuestionClicked(questionPaperResponse: QuestionPaperResponse,position: Int) {
        binding.selectedPosition = position
        binding.viewModel = questionPaperResponse
    }

}