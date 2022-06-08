package com.kapilguru.trainer.exams.conductExams

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentCourseListExamBinding
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.CreateQuestionPaperActivity
import com.kapilguru.trainer.exams.previousQuestionPapersList.PreviousQuestionPaperListActivity
import com.kapilguru.trainer.exams.scheduledExams.ScheduledExamsActivity
import com.kapilguru.trainer.exams.viewModel.ExamsViewModel
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class CourseListExamFragment : Fragment(), QuestionPaperTitleDialogFragment.QuestionPaperTitleListener {
    private val TAG = "ConductExamsFragment"
    lateinit var binding: FragmentCourseListExamBinding
    val viewModel: ExamsViewModel by activityViewModels()
    lateinit var adapter: ConductExamsAdapter
    lateinit var progressDialog: CustomProgressDialog
    lateinit var selectedCourse: CourseResponse
    lateinit var questionPaperTitleDialogFragment: QuestionPaperTitleDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCourseListExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        setClickListeners()
        observeViewModelData()
        viewModel.getCourseList()
    }

    private fun setAdapter() {
        adapter = ConductExamsAdapter()
        binding.rvExams.adapter = adapter
    }

    private fun setClickListeners() {
        binding.actvExamReports.setOnClickListener {
            startActivity(Intent(requireContext(), ScheduledExamsActivity::class.java))
        }
        binding.actvNewQuestionPaper.setOnClickListener {
            showQuestionPaperTitleDialog()
        }
        binding.actvPreviousQuestionPaper.setOnClickListener {
            selectedCourse = adapter.getSelectedCourse()
            PreviousQuestionPaperListActivity.launchActivity(selectedCourse, requireActivity())
        }
    }

    private fun showQuestionPaperTitleDialog() {
        selectedCourse = adapter.getSelectedCourse()
        questionPaperTitleDialogFragment = QuestionPaperTitleDialogFragment.newInstance(selectedCourse)
        questionPaperTitleDialogFragment.setQuestionPaperTitleListener(this)
        questionPaperTitleDialogFragment.show(parentFragmentManager, "")
    }

    private fun closeQuestionPaperTitleDialog(){
        questionPaperTitleDialogFragment?.dismiss()
    }

    private fun observeViewModelData() {
        observeCourseListApiRes()
        observeCreateQuestionPaperTitleApiRes()
    }

    private fun observeCourseListApiRes() {
        viewModel.courseApiRes.observe(requireActivity(), Observer { courseApiRes ->
            when (courseApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    courseApiRes.data?.let { courseRes ->
                        viewModel.verifiedCoursesList = courseRes.courseResponse.filter {
                            it.isVerified == 1
                        } as ArrayList<CourseResponse>
                        setCourseListAdapterData(viewModel.verifiedCoursesList)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setCourseListAdapterData(courseList : ArrayList<CourseResponse>){
        if(courseList.isEmpty()){
            binding.actvNewQuestionPaper.isEnabled = false
            binding.actvPreviousQuestionPaper.isEnabled = false
            showNoData()
        }else{
            if(viewModel.courseIdFromClassRoom == viewModel.emptyCourseId){
                adapter.setData(courseList)
            }else{
                val selectedCourse = courseList.filter {
                    it.courseId == viewModel.courseIdFromClassRoom
                }
                adapter.setData(selectedCourse as ArrayList<CourseResponse>)
            }
        }
    }

    private fun showNoData(){
        binding.actvNoCourses.visibility = View.VISIBLE
        binding.actvSelectCourse.visibility = View.GONE
        binding.rvExams.visibility = View.GONE
    }

    private fun observeCreateQuestionPaperTitleApiRes() {
        viewModel.questionPaperTitleApiResponse.observe(viewLifecycleOwner, Observer { questionPaperTitleAPiRes ->
            when (questionPaperTitleAPiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    questionPaperTitleAPiRes.data?.let { addBatchApiResponse ->
                        val questionPaperId = addBatchApiResponse.data.insertId
                        progressDialog.dismissLoadingDialog()
                        closeQuestionPaperTitleDialog()
                        launchCreateQuestionPaperActivity(questionPaperId)
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun launchCreateQuestionPaperActivity(questionPaperId: Int) {
        val questionPaperTitle = viewModel.questionPaperTitleApiReq.value?.title!!
        CreateQuestionPaperActivity.launchActivityToAddNewQuestions(questionPaperId,questionPaperTitle, selectedCourse, requireActivity())
    }

    override fun onSaveQuestionPaperTitleClicked(questionPaperTitle: String, course: CourseResponse) {
        selectedCourse = course
        viewModel.callCreateQuestionPaperTitleApi(questionPaperTitle, course)
    }
}