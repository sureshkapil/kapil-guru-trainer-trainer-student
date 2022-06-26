package com.kapilguru.trainer.student.exam.questionPaper

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityStudentQuestionPaperNewBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.exam.model.StudentQuestionsAndOptions
import com.kapilguru.trainer.student.exam.model.StudentQuestionsRequest
import com.kapilguru.trainer.student.exam.model.StudentResponses
import com.kapilguru.trainer.student.exam.model.StudentSubmitQuestionRequest
import com.kapilguru.trainer.student.exam.questionPaper.viewModel.StudentQuestionPaperViewModel
import com.kapilguru.trainer.student.exam.questionPaper.viewModel.StudentQuestionPaperViewModelFactory
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListItemResData
import java.lang.reflect.Type

class StudentQuestionPaperNewActivity : AppCompatActivity() {
    private val TAG = "QuestionPaperNewActivity"
    private lateinit var binding: ActivityStudentQuestionPaperNewBinding
    private lateinit var viewModel: StudentQuestionPaperViewModel
    private lateinit var progressDialog: CustomProgressDialog
    var timer: CountDownTimer? = null
    lateinit var adapter: StudentQuestionsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        getIntentData()
        setActivityName()
        getQuestions()
        observeViewModelData()
        setTimerText()
        setClickListeners()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_question_paper_new)
        viewModel = ViewModelProvider(this, StudentQuestionPaperViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudentQuestionPaperViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(this)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = StudentQuestionsListAdapter(viewModel)
        val linearLayoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvQuestion.layoutManager = linearLayoutManager
        binding.rvQuestion.adapter = adapter
    }

    private fun getIntentData() {
        val questionPaperInfo = intent.getParcelableExtra<StudentQuestionPaperListItemResData>(StudentQuestionPaperActivity.KEY_QUESTION_PAPER_INFO)
        viewModel.questionPaperInfo = questionPaperInfo ?: StudentQuestionPaperListItemResData()
        binding.questionPaperInfo = questionPaperInfo
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.question_paper)
    }

    private fun getQuestions() {
        val questionPaper = viewModel.questionPaperInfo
        val questionRequest = StudentQuestionsRequest(questionPaper.batchId, questionPaper.examId, questionPaper.questionPaperId, questionPaper.studentId)
        viewModel.getQuestionsRequest(questionRequest)
    }

    private fun observeViewModelData() {
        observeCurrentQuestionIndex()
        observeQuestionsApiResponse()
        observeQuestionSubmitResponse()
        observeAllQuestionsSubmitResponse()
    }

    private fun observeCurrentQuestionIndex() {
        viewModel.currentQuestionIndex.observe(this, Observer { questionPaperIndex ->
            setCurrentQuestion(questionPaperIndex)
            setActionButtonsVisibility(questionPaperIndex)
        })
    }

    private fun setCurrentQuestion(questionPaperIndex: Int) {
        viewModel.currentQuestionAndOption.value = viewModel.studentQuestionsAndOptions.value!![questionPaperIndex]
    }

    private fun setActionButtonsVisibility(questionPaperIndex: Int) {
        val lastQuestionIndex = viewModel.studentQuestionsAndOptions.value!!.size - 1
        if (questionPaperIndex == 0) {
            setBackBtnProperties(false, false)
            setNextBtnProperties(true, true)
            setSubmitBtnProperties(false, false)
        } else {
            if (questionPaperIndex == lastQuestionIndex) {
                setBackBtnProperties(true, true)
                setNextBtnProperties(false, false)
                setSubmitBtnProperties(true, true)
            } else {
                setBackBtnProperties(true, true)
                setNextBtnProperties(true, true)
                setSubmitBtnProperties(false, false)
            }
        }
    }

    private fun setBackBtnProperties(shouldShow: Boolean, shouldBeEnabled: Boolean) {
        if (shouldShow) {
            binding.btnBack.visibility = View.VISIBLE
        } else {
            binding.btnBack.visibility = View.GONE
        }
        binding.btnBack.isEnabled = shouldBeEnabled
    }

    private fun setNextBtnProperties(shouldShow: Boolean, shouldBeEnabled: Boolean) {
        if (shouldShow) {
            binding.btnNext.visibility = View.VISIBLE
        } else {
            binding.btnNext.visibility = View.GONE
        }
        binding.btnNext.isEnabled = shouldBeEnabled
    }

    private fun setSubmitBtnProperties(shouldShow: Boolean, shouldBeEnabled: Boolean) {
        if (shouldShow) {
            binding.btnSubmit.visibility = View.VISIBLE
        } else {
            binding.btnSubmit.visibility = View.GONE
        }
        binding.btnSubmit.isEnabled = shouldBeEnabled
    }

    private fun observeQuestionsApiResponse() {
        viewModel.questionsResponse.observe(this, Observer { questionsApiResponse ->
            when (questionsApiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    val questions = questionsApiResponse.data?.studentQuestionsApi?.questions
                    val responses = questionsApiResponse.data?.studentQuestionsApi?.responses
                    var studentResponsesList = ArrayList<StudentResponses>()
                    val gson = Gson()
                    responses?.let { responsesNotNull ->
                        val responseType: Type = object : TypeToken<List<StudentResponses>?>() {}.type
                        studentResponsesList = gson.fromJson(responsesNotNull, responseType) as ArrayList<StudentResponses>
                    }
                    questions?.let { questionsNotNull ->
                        val studentQuestionsAndOptionsType: Type = object : TypeToken<List<StudentQuestionsAndOptions>?>() {}.type
                        val studentQuestionsAndOptions: ArrayList<StudentQuestionsAndOptions> = gson.fromJson(questionsNotNull, studentQuestionsAndOptionsType)
                        val questionsAndOptionsWithPreviouslySelectedOptions = addPreviouslySelectedOptions(studentQuestionsAndOptions, studentResponsesList)
                        viewModel.studentQuestionsAndOptions.value = questionsAndOptionsWithPreviouslySelectedOptions
                        viewModel.currentQuestionIndex.value = 0
                        createSubmitQuestionsList(questionsAndOptionsWithPreviouslySelectedOptions)
                        adapter.setData(questionsAndOptionsWithPreviouslySelectedOptions)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun addPreviouslySelectedOptions(
        studentQuestionsAndOptions: ArrayList<StudentQuestionsAndOptions>, studentResponsesList: ArrayList<StudentResponses>
    ): ArrayList<StudentQuestionsAndOptions> {
        val list = ArrayList<StudentQuestionsAndOptions>()
        for (i in 0 until studentQuestionsAndOptions.size step 1) {
            val questionAndOption = studentQuestionsAndOptions[i]
            val response = studentResponsesList.filter { responseItem ->
                questionAndOption.questionId == responseItem.questionId
            }
            var selectedOption = ""
            if (response.isNotEmpty()) {
                selectedOption = response[0].selectedOpt
            }
            questionAndOption.selectedOpt = selectedOption
            list.add(i, questionAndOption)
        }
        return list
    }

    private fun createSubmitQuestionsList(studentQuestionsAndOptions: List<StudentQuestionsAndOptions>) {
        viewModel.submitAllQuestions.value = ArrayList()
        for (questionAndOption in studentQuestionsAndOptions) {
            val question = createSubmitQuestion(questionAndOption)
            viewModel.submitAllQuestions.value!!.add(question)
        }
    }

    private fun createSubmitQuestion(questionAndOption: StudentQuestionsAndOptions): StudentSubmitQuestionRequest {
        return StudentSubmitQuestionRequest(
            courseId = questionAndOption.courseId!!,
            questionPaperId = viewModel.questionPaperInfo.questionPaperId,
            batchId = viewModel.questionPaperInfo.batchId,
            isAttempted = 1,
            studentId = viewModel.questionPaperInfo.studentId.toString(),
            questionId = questionAndOption.questionId!!,
            selectedOpt = questionAndOption.selectedOpt ?: "",
            trainerId = questionAndOption.trainerId!!,
            examId = viewModel.questionPaperInfo.examId
        )
    }

    private fun observeQuestionSubmitResponse() {
        viewModel.submitQuestionApiResponse.observe(this, Observer { commonResponse ->
            when (commonResponse.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    if (viewModel.isSubmitClicked) {
                        viewModel.submitAllQuestionsResponse()
                    } else {
                        updateCurrentQuestion()
                    }
                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun updateCurrentQuestion() {
        val shouldIncrement = viewModel.isNextQuestionClicked
        val currentIndex = viewModel.currentQuestionIndex.value
        if (shouldIncrement) {
            checkAndIncrementQuestionIndex(currentIndex)
        } else {
            checkAndDecrementQuestionIndex(currentIndex)
        }
    }

    private fun checkAndIncrementQuestionIndex(currentIndex: Int?) {
        currentIndex?.let { currentIndexNotNull ->
            if (currentIndexNotNull != viewModel.studentQuestionsAndOptions.value!!.size - 1) {
                viewModel.currentQuestionIndex.value = currentIndexNotNull + 1
                binding.rvQuestion.scrollToPosition(currentIndexNotNull + 1)
                adapter.updateUI(currentIndexNotNull + 1)
            }
        }
    }

    private fun checkAndDecrementQuestionIndex(currentIndex: Int?) {
        currentIndex?.let { currentIndexNotNull ->
            if (currentIndexNotNull != 0) {
                viewModel.currentQuestionIndex.value = currentIndexNotNull - 1
                binding.rvQuestion.scrollToPosition(currentIndexNotNull - 1)
                adapter.updateUI(currentIndexNotNull + 1)
            }
        }
    }

    private fun observeAllQuestionsSubmitResponse() {
        viewModel.submitAllQuestionsApiResponse.observe(this, Observer { commonResponse ->
            when (commonResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    showAppToast(this, "Uploaded Successfully")
                    finish()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    showAppToast(this, "Error in Upload")
                    when (commonResponse.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun setTimerText() {
        if (timer != null) timer!!.cancel()

        val totalDiff: Long = (viewModel.questionPaperInfo.duration * 60 * 1000).toLong() // duration is given in minutes
        timer = object : CountDownTimer((totalDiff) + 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val hours = seconds / (60 * 60)
                val tempMint = seconds - hours * 60 * 60
                val minutes = tempMint / 60
                seconds = tempMint - minutes * 60
                binding.actvDurationValue.text = String.format(
                    "%02d", hours
                ) + ":" + kotlin.String.format("%02d", minutes) + ":" + kotlin.String.format(
                    "%02d", seconds
                )
            }

            override fun onFinish() {
                submitFinalResponse()
            }
        }.start()
    }

    private fun setClickListeners() {
        setActivityBackClickListener()
        setQuestionBackClickListener()
        setNextClickListener()
        setSubmitClickListener()
    }

    private fun setActivityBackClickListener() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }

    private fun setQuestionBackClickListener() {
        binding.btnBack.setOnClickListener {
            submitResponse(false, false)
        }
    }

    private fun setNextClickListener() {
        binding.btnNext.setOnClickListener {
            submitResponse(true, false)
        }
    }

    private fun submitResponse(isNextButtonClicked: Boolean, isSubmitButtonClicked: Boolean) {
        updateResponseInQuestionsList()
        updateResponseInSubmitQuestionsList()
        viewModel.isNextQuestionClicked = isNextButtonClicked
        viewModel.isSubmitClicked = isSubmitButtonClicked
        val selectedOption = viewModel.currentQuestionAndOption.value?.selectedOpt
        viewModel.submitQuestionResponse(selectedOption)
    }

    private fun updateResponseInQuestionsList() {
        val question = viewModel.currentQuestionAndOption.value
        viewModel.studentQuestionsAndOptions.value?.set(viewModel.currentQuestionIndex.value!!, question!!)
    }

    private fun updateResponseInSubmitQuestionsList() {
        val question = viewModel.currentQuestionAndOption.value
        val submitQuestionRequest = createSubmitQuestion(question!!)
        viewModel.submitAllQuestions.value?.set(viewModel.currentQuestionIndex.value!!, submitQuestionRequest)
    }

    private fun setSubmitClickListener() {
        binding.btnSubmit.setOnClickListener {
            confirmUserBeforeSubmission()
        }
    }

    private fun confirmUserBeforeSubmission() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setTitle("You are about to submit the exam")
        alertDialogBuilder.setMessage("Are you sure you want to submit the exam ?")
        alertDialogBuilder.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
            submitFinalResponse()
        })
        alertDialogBuilder.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        alertDialogBuilder.show()
    }

    private fun submitFinalResponse() {
        submitResponse(true, true)
    }

    companion object {
        const val KEY_QUESTION_PAPER_INFO = "QUESTION_PAPER_INFO"

        fun launchActivity(activity: Activity, questionPaperInfo: StudentQuestionPaperListItemResData) {
            val intent = Intent(activity, StudentQuestionPaperNewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(KEY_QUESTION_PAPER_INFO, questionPaperInfo)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}