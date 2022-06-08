package com.kapilguru.trainer.exams.createQuestion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityCreateQuestionBinding
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.Question
import com.kapilguru.trainer.exams.createQuestion.model.AddQuestionRequest
import com.kapilguru.trainer.exams.createQuestion.viewModel.CreateQuestionViewModel
import com.kapilguru.trainer.exams.createQuestion.viewModel.CreateQuestionViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class CreateQuestionActivity : BaseActivity() {
    private val TAG = "CreateQuestionActivity"
    lateinit var binding: ActivityCreateQuestionBinding
    lateinit var viewModel: CreateQuestionViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var question: TextFormat
    lateinit var option1: TextFormat
    lateinit var option2: TextFormat
    lateinit var option3: TextFormat
    lateinit var option4: TextFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_question)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setCustomActionBarListener()
        viewModel = ViewModelProvider(this, CreateQuestionViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(CreateQuestionViewModel::class.java)
        binding.viewModel = viewModel
        getIntentData()
        addTextFormatters()
        setClickListeners()
        observeViewModelData()
        checkAndPopulateViews()
    }

    private fun getIntentData() {
        viewModel.course = intent.getParcelableExtra(KEY_COURSE)!!
        viewModel.questionPaperId = intent.getIntExtra(KEY_QUESTION_PAPER_ID, 0)
        viewModel.questionPaperTitle = intent.getStringExtra(KEY_QUESTION_PAPER_TITLE)!!
        viewModel.question = intent.getParcelableExtra(KEY_QUESTION)
        if (viewModel.question != null) {
            viewModel.isLaunchedForEdit = true
        }
    }

    private fun checkAndPopulateViews() {
        if (viewModel.isLaunchedForEdit) {
            viewModel.question?.let {
                populateQuestionAndOptions(it)
                setCorrectOptionAndFillMarks(it)
                setUpdateText()
            }
        }
    }

    private fun populateQuestionAndOptions(editQuestion: Question) {
        question.binding.richEditor.html = editQuestion.question?.fromBase64ToString()
        option1.binding.richEditor.html = editQuestion.opt1?.fromBase64ToString()
        option2.binding.richEditor.html = editQuestion.opt2?.fromBase64ToString()
        option3.binding.richEditor.html = editQuestion.opt3?.fromBase64ToString()
        option4.binding.richEditor.html = editQuestion.opt4?.fromBase64ToString()
    }

    private fun setCorrectOptionAndFillMarks(editQuestion: Question) {
        when (editQuestion.correctOpt) {
            getString(R.string.option1) -> {
                binding.rbA.isChecked = true
            }
            getString(R.string.option2) -> {
                binding.rbB.isChecked = true
            }
            getString(R.string.option3) -> {
                binding.rbC.isChecked = true
            }
            getString(R.string.option4) -> {
                binding.rbD.isChecked = true
            }
        }
        binding.tietMarks.setText(editQuestion.marks.toString())
    }

    private fun setUpdateText(){
        binding.actvAdd.text = "Update"
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.exams_add_questions))
    }

    private fun setClickListeners() {
        setCancelClickListener()
        setAddClickListener()
    }

    private fun setCancelClickListener() {
        binding.actvCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setAddClickListener() {
        binding.actvAdd.setOnClickListener {
            checkAndAddQuestion()
        }
    }

    private fun checkAndAddQuestion() {
        val questionString = question.getText()
        val option1String = option1.getText()
        val option2String = option2.getText()
        val option3String = option3.getText()
        val option4String = option4.getText()
        if (TextUtils.isEmpty(questionString)) {
            showAppToast(this, "Please enter Question")
            return
        }
        if (TextUtils.isEmpty(option1String)) {
            showAppToast(this, "Please enter Option A")
            return
        }
        if (TextUtils.isEmpty(option2String)) {
            showAppToast(this, "Please enter Option B")
            return
        }
        if (TextUtils.isEmpty(option3String)) {
            showAppToast(this, "Please enter Option C")
            return
        }
        if (TextUtils.isEmpty(option4String)) {
            showAppToast(this, "Please enter Option D")
            return
        }
        if (!isAnswerSelected()) {
            showAppToast(this, "Please select correct Answer")
            return
        }
        if (TextUtils.isEmpty(binding.tietMarks.text)) {
            showAppToast(this, "Please enter Marks for the question")
            return
        }
        setAddQuestionRequestData()
        viewModel.addOrUpdateQuestion()
    }

    private fun setAddQuestionRequestData() {
        val addQuestionRequest = AddQuestionRequest()
        addQuestionRequest.correctOpt = getCorrectOption()
        addQuestionRequest.courseId = viewModel.course.courseId!!
        addQuestionRequest.marks = binding.tietMarks.text.toString()
        addQuestionRequest.opt1 = option1.getText().toBase64()
        addQuestionRequest.opt2 = option2.getText().toBase64()
        addQuestionRequest.opt3 = option3.getText().toBase64()
        addQuestionRequest.opt4 = option4.getText().toBase64()
        addQuestionRequest.question = question.getText().toBase64()
        addQuestionRequest.questionPaperId = viewModel.questionPaperId
        addQuestionRequest.trainerId = viewModel.course.trainerId.toString()
        viewModel.addQuestionRequest.value = addQuestionRequest
    }

    private fun getCorrectOption(): String {
        return when {
            binding.rbA.isChecked -> "opt_1"
            binding.rbB.isChecked -> "opt_2"
            binding.rbC.isChecked -> "opt_3"
            else -> "option_4"
        }
    }

    private fun isAnswerSelected(): Boolean {
        return (binding.rbA.isChecked || binding.rbB.isChecked || binding.rbC.isChecked || binding.rbD.isChecked)
    }

    private fun observeViewModelData() {
        viewModel.addQuestionResponse.observe(this, Observer { addBathApiRes ->
            when (addBathApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    setResult(RESULT_OK)
                    finish()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }

        })
    }

    private fun addTextFormatters() {
        question = TextFormat(this, binding.llTextFormatter)
        question.setHeader("Question", resources.getColor(R.color.purple))

        option1 = TextFormat(this, binding.llTextFormatter)
        option1.setHeader("Option A", resources.getColor(R.color.purple))

        option2 = TextFormat(this, binding.llTextFormatter)
        option2.setHeader("Option B", resources.getColor(R.color.purple))

        option3 = TextFormat(this, binding.llTextFormatter)
        option3.setHeader("Option C", resources.getColor(R.color.purple))

        option4 = TextFormat(this, binding.llTextFormatter)
        option4.setHeader("Option D", resources.getColor(R.color.purple))

    }

    companion object {
        val KEY_COURSE = "key_course"
        val KEY_QUESTION_PAPER_ID = "key_question_paper_id"
        val KEY_QUESTION_PAPER_TITLE = "key_question_paper_title"
        val KEY_QUESTION = "key_question_edit"

        fun getIntentToLaunchActivity(course: CourseResponse, question: Question?, questionPaperId: Int, questionPaperTitle: String, activity: Activity): Intent {
            val intent = Intent(activity, CreateQuestionActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(KEY_COURSE, course)
            bundle.putInt(KEY_QUESTION_PAPER_ID, questionPaperId)
            bundle.putString(KEY_QUESTION_PAPER_TITLE, questionPaperTitle)
            bundle.putParcelable(KEY_QUESTION, question)
            intent.putExtras(bundle)
            return intent
        }
    }
}