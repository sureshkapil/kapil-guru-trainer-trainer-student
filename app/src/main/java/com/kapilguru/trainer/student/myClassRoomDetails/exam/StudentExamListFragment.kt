package com.kapilguru.trainer.student.myClassRoomDetails.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.trainer.databinding.FragmentStudentExamListBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.networkConnectionErrorDialog
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListItemResData
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListRequest
import com.kapilguru.trainer.student.myClassRoomDetails.viewModel.StudentMyClassDetailsViewModel

class StudentExamListFragment : Fragment(), StudentExamListAdapter.ExamItemClickListener {
    private val TAG = "ExamListFragment"
    private lateinit var binding: FragmentStudentExamListBinding
    private val viewModel: StudentMyClassDetailsViewModel by activityViewModels()
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var studentExamListAdapter: StudentExamListAdapter
    private var userId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStudentExamListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLateInitVariables()
        observeViewModelData()
        getQuestionPaperList()
    }

    private fun initializeLateInitVariables() {
        progressDialog = CustomProgressDialog(requireContext())
        studentExamListAdapter = StudentExamListAdapter(this)
        binding.rvExam.adapter = studentExamListAdapter
    }

    private fun observeViewModelData() {
        observeExamListApiRes()
    }

    private fun observeExamListApiRes() {
        viewModel.examListApiRes.observe(viewLifecycleOwner, Observer { questionPaperListApiRes ->
            when (questionPaperListApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    setAdapterData(questionPaperListApiRes.data?.questionPaperList)
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (questionPaperListApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(requireContext())
                        }
                    }
                }
            }
        })
    }

    private fun setAdapterData(examList: ArrayList<StudentQuestionPaperListItemResData>?) {
        examList?.let { examListNotNull ->
            if (examListNotNull.isEmpty()) {
                showOrHideEmptyExamsView(true)
            } else {
                studentExamListAdapter.setData(viewModel.batchCode, examList)
                showOrHideEmptyExamsView(false)
            }
        } ?: kotlin.run {
            showOrHideEmptyExamsView(true)
        }
    }

    private fun showOrHideEmptyExamsView(showExamsEmptyView: Boolean) {
        if (showExamsEmptyView) {
            binding.rvExam.visibility = View.GONE
            binding.actvEmptyView.visibility = View.VISIBLE
        } else {
            binding.rvExam.visibility = View.VISIBLE
            binding.actvEmptyView.visibility = View.GONE
        }
    }

    private fun getQuestionPaperList() {
        userId = StorePreferences(requireContext()).userId.toString()
        val studentQuestionPaperListRequest = StudentQuestionPaperListRequest(viewModel.batchId.value!!.toInt(), userId)
        viewModel.examListApiReq.value = studentQuestionPaperListRequest
        viewModel.getQuestionPaper()
    }

    override fun onStartExamClicked(questionPaper: StudentQuestionPaperListItemResData) {
//        QuestionPaperNewActivity.launchActivity(requireActivity(), questionPaper)  ---need_to_do
    }

    override fun onViewResultClicked(questionPaper: StudentQuestionPaperListItemResData) {
        launchViewResult(questionPaper)
    }

    private fun launchViewResult(questionPaper: StudentQuestionPaperListItemResData) { //---need_to_do
        /* val examReportRequest = StudentReportRequest(
             batchId = questionPaper.batchId,
             examId = questionPaper.examId,
             questionPaperId = questionPaper.questionPaperId,
             studentId = questionPaper.studentId,
             courseId = questionPaper.courseId,
             trainerId = questionPaper.trainerId
         )
         val questionRequest = QuestionsRequest(
             batchId = questionPaper.batchId,
             examId = questionPaper.examId,
             questionPaperId = questionPaper.questionPaperId,
             studentId = questionPaper.studentId,
         )
         startActivity(
             Intent(requireContext(), ViewExamResult::class.java).putExtra(PARAM_REPORTS_REQUEST, examReportRequest).putExtra(PARAM_QUESTIONS_REQUEST, questionRequest)
         )*/
    }
}