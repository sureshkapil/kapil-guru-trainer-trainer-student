package com.kapilguru.trainer.exams.previousQuestionsList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityPreviousQuestionsListBinding
import com.kapilguru.trainer.exams.previousQuestionsList.model.AddExistingQuesApiRequest
import com.kapilguru.trainer.exams.previousQuestionsList.model.PreviousQuestionData
import com.kapilguru.trainer.exams.previousQuestionsList.viewModel.PreviousQuestionsListViewModel
import com.kapilguru.trainer.exams.previousQuestionsList.viewModel.PreviousQuestionsListViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class PreviousQuestionsListActivity : BaseActivity() {
    private val TAG = "PreviousQuestionsListActivity"
    lateinit var binding: ActivityPreviousQuestionsListBinding
    lateinit var viewModel: PreviousQuestionsListViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: PreviousQuestionsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previous_questions_list)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, PreviousQuestionsListViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(PreviousQuestionsListViewModel::class.java)
        getIntentData()
        setCustomActionBarListener()
        binding.viewModel = viewModel
        setAdapter()
        setClickListener()
        observeViewModelData()
        viewModel.getPreviousQuestionsList()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.exams_add_from_prev_ques))
    }

    private fun getIntentData() {
        viewModel.course = intent.getParcelableExtra(KEY_COURSE)!!
        viewModel.questionPaperId = intent.getIntExtra(KEY_QUESTION_PAPER_ID,-1)
    }

    private fun setAdapter() {
        adapter = PreviousQuestionsListAdapter()
        binding.rvPreviousQuestionsList.adapter = adapter
    }

    private fun setClickListener() {
        binding.actvBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        binding.actvSubmit.setOnClickListener {
            setAddExistingQuesApiRequestData()
            viewModel.addExistingQuestions()
        }
    }

    private fun setAddExistingQuesApiRequestData() {
        val addExistingQuesApiRequest = AddExistingQuesApiRequest()
        addExistingQuesApiRequest.courseId = viewModel.course.courseId!!
        addExistingQuesApiRequest.trainerId = viewModel.course.trainerId.toString()
        addExistingQuesApiRequest.questions_json_b64 = getSelectedQuestionBase64()
        addExistingQuesApiRequest.questionPaperId = viewModel.questionPaperId
        viewModel.addExistingQuesApiReq.value = addExistingQuesApiRequest
    }

    /*question id is sent in the addExistingQuestion Api Call
    * ex : [9,10]*/
    private fun getSelectedQuestionBase64(): String {
        val selectedQuestions: ArrayList<PreviousQuestionData> = adapter.getSelectedQuestions()
        val selectedQuesIds = ArrayList<Int>()
        for (selectedQues in selectedQuestions) {
            selectedQuesIds.add(selectedQues.id)
        }
        Log.d(TAG,"selectedQuesIds.toString().toBase64() : "+selectedQuesIds.toString().toBase64())
        return selectedQuesIds.toString().toBase64()
    }

    private fun observeViewModelData() {
        observePreviousQuestionsListApiRes()
        observeAddExistingQuesListApiRes()
    }

    private fun observePreviousQuestionsListApiRes() {
        viewModel.previousQuestionsList.observe(this, Observer { previousQuestionsListApiRes ->
            when (previousQuestionsListApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    previousQuestionsListApiRes.data?.let { previousQuestionsListResponse ->
                        previousQuestionsListResponse.previousQuestionsList?.let { previousQuestionsList ->
                            setAdapterData(previousQuestionsList as ArrayList<PreviousQuestionData>)
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

    private fun observeAddExistingQuesListApiRes() {
        viewModel.addExistingQuestionsApiRes.observe(this, Observer { addExistingQuesApiRes ->
            when (addExistingQuesApiRes.status) {
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

    private fun setAdapterData(previousQuestionsList: ArrayList<PreviousQuestionData>) {
        adapter.setData(previousQuestionsList)
    }

    companion object {
        val KEY_COURSE = "KEY_COURSE"
        val KEY_QUESTION_PAPER_ID = "KEY_QUESTION_PAPER_ID"

        fun getIntentToLaunchActivity(course: CourseResponse, questionPaperId: Int,actvity : Activity): Intent {
            return Intent(actvity,PreviousQuestionsListActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(KEY_COURSE, course)
                    putInt(KEY_QUESTION_PAPER_ID, questionPaperId)
                }
                putExtras(bundle)
            }
        }
    }
}