package com.kapilguru.trainer.exams.previousQuestionPapersList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityPreviousQuestionPaperListBinding
import com.kapilguru.trainer.exams.conductExams.QuestionPaperTitleDialogFragment
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.CreateQuestionPaperActivity
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.Question
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaper
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionsListData
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.CopyFromQuesPaperRequest
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.PreviousQuestionPapersListData
import com.kapilguru.trainer.exams.previousQuestionPapersList.viewModel.PreviousQuestionPapersListFactory
import com.kapilguru.trainer.exams.previousQuestionPapersList.viewModel.PreviousQuestionPapersListViewModel
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class PreviousQuestionPaperListActivity : BaseActivity(), PreviousQuestionPapersListAdapter.QuestionPaperClickListener, QuestionPaperTitleDialogFragment.QuestionPaperTitleListener {
    private val TAG = "PreviousQuestionPaperListActivity"
    lateinit var binding: ActivityPreviousQuestionPaperListBinding
    lateinit var viewModel: PreviousQuestionPapersListViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: PreviousQuestionPapersListAdapter
    lateinit var questionPaperTitleDialogFragment: QuestionPaperTitleDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previous_question_paper_list)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setCustomActionBarListener()
        viewModel = ViewModelProvider(this, PreviousQuestionPapersListFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(PreviousQuestionPapersListViewModel::class.java)
        binding.viewModel = viewModel
        getIntentData()
        setAdapter()
        observeViewModelData()
        fetchQuestionPapersList()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.exams_add_questions))
    }

    private fun getIntentData() {
        viewModel.course = intent.getParcelableExtra(KEY_COURSE)!!
    }

    private fun fetchQuestionPapersList() {
        viewModel.getQuestionPapersList()
    }

    private fun observeViewModelData() {
        observePreviousQuestionPapersListApiRes()
        observeQuestionsListResForSelectedPaper()
        observeCopyFromQuestionPaperApiRes()
    }

    private fun observePreviousQuestionPapersListApiRes() {
        viewModel.previousQuestionPapersListApiRes.observe(this, Observer { previousQuestionPapersListApiRes ->
            when (previousQuestionPapersListApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    previousQuestionPapersListApiRes.data?.let { previousQuestionPapersListResponse ->
                        previousQuestionPapersListResponse.previousQuestionPaperList?.let { previousQuestionPapersList ->
                            setAdapterData(previousQuestionPapersList as ArrayList<PreviousQuestionPapersListData>)
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeQuestionsListResForSelectedPaper() {
        viewModel.questionsListApiResponse.observe(this, Observer { questionsListApiRes ->
            when (questionsListApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    questionsListApiRes.data?.let { questionsListResponse ->
                        questionsListResponse.questionsListData.let { questionsListData ->
                            questionsListData?.let {
                                parseQuestionsApiResponse(it)
                                showQuestionPaperTitleDialog()
                            }
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeCopyFromQuestionPaperApiRes() {
        viewModel.questionsListApiResponse.observe(this, Observer { questionsListApiRes ->
            when (questionsListApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    questionsListApiRes.data?.let { questionsListResponse ->
                        questionsListResponse.questionsListData.let { questionsListData ->
                            questionsListData?.let {
                                parseQuestionsApiResponse(it)
                                launchCreateQuestionPaperActivity()
                            }
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun parseQuestionsApiResponse(questionsListData: QuestionsListData) {
        questionsListData.questionPaper?.let { questionPaper ->
            viewModel.questionPaper = QuestionPaper().parseQuestionPaper(questionPaper)
        }
        viewModel.questionsList.value = Question().parseQuestions(questionsListData.questions)
    }

    private fun showQuestionPaperTitleDialog() {
        questionPaperTitleDialogFragment = QuestionPaperTitleDialogFragment.newInstance(viewModel.course)
        questionPaperTitleDialogFragment.setQuestionPaperTitleListener(this)
        questionPaperTitleDialogFragment.show(supportFragmentManager, "")
    }

    private fun setAdapterData(previousQuestionPapersList: ArrayList<PreviousQuestionPapersListData>) {
        adapter.setData(previousQuestionPapersList)
    }

    private fun setAdapter() {
        adapter = PreviousQuestionPapersListAdapter(this)
        binding.rvPackageList.adapter = adapter
        binding.rvPackageList.layoutManager = GridLayoutManager(this, 2)
    }

    private fun launchCreateQuestionPaperActivity() {
        val course = viewModel.course
        val questionPaper = viewModel.questionPaper
        val questions = viewModel.questionsList.value!!
        closeQuestionPaperTitleDialog()
        CreateQuestionPaperActivity.launchActivityForCopyingQuestionPaper(course, questionPaper, questions, this)
    }

    private fun closeQuestionPaperTitleDialog(){
        questionPaperTitleDialogFragment.dismiss()
    }

    override fun onQuestionPaperClicked(previousQuestionPaper: PreviousQuestionPapersListData) {
        if (previousQuestionPaper.isDraft()) {
            CreateQuestionPaperActivity.launchActivityForViewAndAssign(viewModel.course, previousQuestionPaper, this)
        } else {
            viewModel.questionPaper = QuestionPaper().getQuestionPaper(previousQuestionPaper)
            viewModel.getQuestionsList()
        }
    }

    companion object {
        val KEY_COURSE = "key_course"

        fun launchActivity(course: CourseResponse, activity: Activity) {
            val intent = Intent(activity, PreviousQuestionPaperListActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(KEY_COURSE, course)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    override fun onSaveQuestionPaperTitleClicked(questionPaperTitle: String, course: CourseResponse) {
        val copyFromQuestionPaperApiReq = CopyFromQuesPaperRequest()
        copyFromQuestionPaperApiReq.courseId = course.courseId!!
        copyFromQuestionPaperApiReq.newQuestionPaperTitle = questionPaperTitle
        copyFromQuestionPaperApiReq.trainerId = course.trainerId.toString()
        copyFromQuestionPaperApiReq.existingQuestionpaperId = viewModel.questionPaper.questionPaperId.toString()
        viewModel.callCopyFromQuestionPaperApi(copyFromQuestionPaperApiReq)
    }
}