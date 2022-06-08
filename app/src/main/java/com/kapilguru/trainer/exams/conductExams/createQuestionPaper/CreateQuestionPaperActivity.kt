package com.kapilguru.trainer.exams.conductExams.createQuestionPaper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityCreateQuestionPaperBinding
import com.kapilguru.trainer.exams.assignExamToBatch.AssignExamToBatchActivity
import com.kapilguru.trainer.exams.conductExams.QuestionsListAdapter
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.Question
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaper
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionsListData
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.viewModel.CreateQuestionPaperViewModel
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.viewModel.CreateQuestionPaperViewModelFactory
import com.kapilguru.trainer.exams.createQuestion.CreateQuestionActivity
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.PreviousQuestionPapersListData
import com.kapilguru.trainer.exams.previousQuestionsList.PreviousQuestionsListActivity
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class CreateQuestionPaperActivity : BaseActivity(), QuestionsListAdapter.QuestionClickListener {
    private val TAG = "CreateQuestionPaperActivity"
    lateinit var binding: ActivityCreateQuestionPaperBinding
    lateinit var viewModel: CreateQuestionPaperViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var questionsListAdapter: QuestionsListAdapter
    lateinit var createQuestionResultLauncher: ActivityResultLauncher<Intent>
    lateinit var selectedPrevQuestionsResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, CreateQuestionPaperViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(CreateQuestionPaperViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_question_paper)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setCustomActionBarListener()
        setAdapter()
        getIntentData()
        binding.viewModel = viewModel
        registerActivityResults()
        setClickListeners()
        observeViewModelData()
    }

    override fun onResume() {
        super.onResume()
        setVisibilityOfSaveButton()
    }

    private fun setVisibilityOfSaveButton() {
        if (viewModel.questionsList.value?.size!! > 0) {
            binding.btnSaveAndAssign.visibility = View.VISIBLE
        } else {
            binding.btnSaveAndAssign.visibility = View.GONE
        }
    }

    private fun setAdapter() {
        questionsListAdapter = QuestionsListAdapter(this)
        binding.rvQuestions.adapter = questionsListAdapter
    }

    private fun registerActivityResults() {
        registerForCreateQuestionActivityResult()
        registerForSelectPrevQuestionsActivityResult()
    }

    private fun registerForCreateQuestionActivityResult() {
        createQuestionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            when (activityResult.resultCode) {
                Activity.RESULT_OK -> {
                    viewModel.getQuestionsList()
                }
            }
        }
    }

    private fun registerForSelectPrevQuestionsActivityResult() {
        selectedPrevQuestionsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            when (activityResult.resultCode) {
                Activity.RESULT_OK -> {
                    viewModel.getQuestionsList()
                }
            }
        }
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.exams_view_questions))
    }

    private fun getIntentData() {
        if (intent.action.equals(ACTION_ADD_NEW_QUESTIONS)) {
            viewModel.course = intent.getParcelableExtra(KEY_COURSE)!!
            val questionPaperTitle = intent.getStringExtra(KEY_QUESTION_PAPER_TITLE)!!
            val questionPaperId = intent.getIntExtra(KEY_QUESTION_PAPER_ID, -1)
            viewModel.questionPaper = QuestionPaper().createQuestionPaper(questionPaperId, questionPaperTitle)
            viewModel.isLaunchedForAddingNewQuestions = true
        }
        if (intent.action.equals(ACTION_COPY_QUESTION_PAPER)) {
            viewModel.course = intent.getParcelableExtra(KEY_COURSE)!!
            viewModel.questionPaper = intent.getParcelableExtra(KEY_QUESTION_PAPER)!!
            viewModel.questionsList.value = intent.getParcelableArrayListExtra(KEY_QUESTIONS_LIST)
            viewModel.isLaunchedForCopyQuestionPaper = true
            setQuestionsListAdapterData()
        }
        if (intent.action.equals(ACTION_VIEW_AND_ASSIGN)) {
            viewModel.isLaunchedForViewAndAssign = true
            viewModel.course = intent.getParcelableExtra(KEY_COURSE)!!
            viewModel.questionPaper = QuestionPaper().getQuestionPaper(intent.getParcelableExtra(KEY_PREVIOUS_QUESTION_PAPER)!!)
            viewModel.getQuestionsList()
        }
    }

    private fun setClickListeners() {
        binding.rlAddQuesFomPrev.setOnClickListener {
            launchPrevQuesListActivityForResult()
        }

        binding.rlAddNewQues.setOnClickListener {
            launchCreateOrEditQuestionActivityForResult(null)
        }
        binding.btnSaveAndAssign.setOnClickListener {
            AssignExamToBatchActivity.launchActivity(viewModel.course, viewModel.questionPaper, this)
        }
    }

    private fun launchPrevQuesListActivityForResult() {
        val intent = PreviousQuestionsListActivity.getIntentToLaunchActivity(viewModel.course, viewModel.questionPaper.questionPaperId, this)
        selectedPrevQuestionsResultLauncher.launch(intent)
    }

    private fun launchCreateOrEditQuestionActivityForResult(question: Question?) {
        val questionPaperId = viewModel.questionPaper.questionPaperId
        val questionPaperTitle = viewModel.questionPaper.title
        val intent = CreateQuestionActivity.getIntentToLaunchActivity(viewModel.course, question, questionPaperId, questionPaperTitle.toString(), this)
        createQuestionResultLauncher.launch(intent)
    }

    private fun observeViewModelData() {
        observeQuestionsListData()
    }

    private fun observeQuestionsListData() {
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
                                setQuestionsListAdapterData()
                                setVisibilityOfSaveButton()
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
            viewModel.questionPaper = QuestionPaper().parseQuestionPaper(questionsListData.questionPaper)
        }
        viewModel.questionsList.value = Question().parseQuestions(questionsListData.questions)
    }

    private fun setQuestionsListAdapterData() {
        questionsListAdapter.setData(viewModel.questionsList.value)
    }

    override fun onEditClicked(question: Question) {
        launchCreateOrEditQuestionActivityForResult(question)
    }

    override fun onDeleteClicked(question: Question) {
        Log.d(TAG, "onDeleteClicked: ")
    }

    companion object {
        val KEY_COURSE = "COURSE_KEY"
        val KEY_QUESTION_PAPER_ID = "QUESTION_PAPER_ID"
        val KEY_QUESTION_PAPER_TITLE = "QUESTION_PAPER_TITLE"
        val KEY_PREVIOUS_QUESTION_PAPER = "PREVIOUS_QUESTION_PAPER"
        val KEY_QUESTIONS_LIST = "QUESTIONS_LIST"
        val KEY_QUESTION_PAPER = "QUESTION_PAPER"
        val ACTION_ADD_NEW_QUESTIONS = "ADD_NEW_QUESTIONS"
        val ACTION_COPY_QUESTION_PAPER = "copy_question_paper"
        val ACTION_VIEW_AND_ASSIGN = "view_and_assign"

        fun launchActivityToAddNewQuestions(questionPaperId: Int, questionPaperTitle: String, course: CourseResponse, activity: Activity) {
            val intent = Intent(activity, CreateQuestionPaperActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(KEY_COURSE, course)
                    putString(KEY_QUESTION_PAPER_TITLE, questionPaperTitle)
                    putInt(KEY_QUESTION_PAPER_ID, questionPaperId)
                }
                putExtras(bundle)
                action = ACTION_ADD_NEW_QUESTIONS
            }
            activity.startActivity(intent)
        }

        /*This mode of activity is launched from previous question paper List activity, when the selected question paper status is Published*/
        fun launchActivityForCopyingQuestionPaper(course: CourseResponse, questionPaper: QuestionPaper, questionsList: ArrayList<Question>, activity: Activity) {
            val intent = Intent(activity, CreateQuestionPaperActivity::class.java)
            intent.action = ACTION_COPY_QUESTION_PAPER
            val bundle = Bundle()
            bundle.putParcelable(KEY_COURSE, course)
            bundle.putParcelable(KEY_QUESTION_PAPER, questionPaper)
            bundle.putParcelableArrayList(KEY_QUESTIONS_LIST, questionsList)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }

        /*This mode is used when this activity is launched from previous Question Papers List and
        the selected question paper status is draft*/
        fun launchActivityForViewAndAssign(course: CourseResponse, previousQuestionPaper: PreviousQuestionPapersListData, activity: Activity) {
            val intent = Intent(activity, CreateQuestionPaperActivity::class.java)
            intent.action = ACTION_VIEW_AND_ASSIGN
            val bundle = Bundle()
            bundle.putParcelable(KEY_COURSE, course)
            bundle.putParcelable(KEY_PREVIOUS_QUESTION_PAPER, previousQuestionPaper)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}