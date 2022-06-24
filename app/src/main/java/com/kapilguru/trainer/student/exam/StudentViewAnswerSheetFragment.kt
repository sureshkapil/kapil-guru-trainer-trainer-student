package com.kapilguru.trainer.student.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.trainer.PARAM_QUESTIONS_REQUEST
import com.kapilguru.trainer.databinding.FragmentStudentViewAnswerSheetBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.networkConnectionErrorDialog
import com.kapilguru.trainer.student.exam.model.StudentQuestionsAndOptions
import com.kapilguru.trainer.student.exam.model.StudentQuestionsRequest
import com.kapilguru.trainer.student.exam.model.StudentSelectedOption
import java.lang.reflect.Type

class StudentViewAnswerSheetFragment : Fragment(), QuestionClickListener {

    lateinit var binding: FragmentStudentViewAnswerSheetBinding
    val viewModel: StudentExamViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    private val TAG = "ViewAnswerSheetFragment"
    lateinit var studentQuestionRecycler: StudentQuestionRecycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentViewAnswerSheetBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(this.requireActivity())
        setUpRecycler()
        apiCall()
        observeViewModel()
    }

    private fun setUpRecycler() {
        studentQuestionRecycler = StudentQuestionRecycler(this)
        binding.questionsRecycler.adapter = studentQuestionRecycler
    }

    private fun apiCall() {
        val studentReportRequest = arguments?.getParcelable<StudentQuestionsRequest>(PARAM_QUESTIONS_REQUEST)
        studentReportRequest?.let {
            viewModel.getQuestionsRequest(it)
        }
    }


    private fun observeViewModel() {
        viewModel.studentQuestionsReponse.observe(this.viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    val questions = response.data?.studentQuestionsApi?.questions
                    val responses = response.data?.studentQuestionsApi?.responses
                    var gson = Gson()
                    questions?.let { it ->
                        val studentQuestionsAndOptionsType: Type = object : TypeToken<List<StudentQuestionsAndOptions>?>() {}.type
                        val studentQuestionsAndOptions: List<StudentQuestionsAndOptions> = gson.fromJson(questions.toString(), studentQuestionsAndOptionsType)
                        studentQuestionsAndOptions.let { questionsAndOptions ->
                            viewModel.studentQuestionsAndOptions.value = questionsAndOptions as ArrayList<StudentQuestionsAndOptions>
                        }
                    }

                    gson = Gson()
                    responses?.let { it ->
                        val studentSelectedOptionType: Type = object : TypeToken<List<StudentSelectedOption>?>() {}.type
                        val studentSelectedOption: List<StudentSelectedOption> = gson.fromJson(responses.toString(), studentSelectedOptionType)
                        studentSelectedOption.let { selectedOption ->
                            viewModel.studentSelectedOption.value = selectedOption as ArrayList<StudentSelectedOption>
                        }
                    }
                    // merge questions and selceted ans
                    updateQuestionAndOptionsWithSelectedOptions()
                    updateRecyclerView()
                    showDefaultData()
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (response.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                    }
//                    if (response.data?.status != 200) {
//
//                    } else {
//
//                    }
                }
            }
        })
    }

    private fun showDefaultData() {
        viewModel.studentQuestionsAndOptions.value?.let {
            if (it.size > 0) onQuestionClicked(it[0], 1)
        }
    }

    private fun updateRecyclerView() {
        studentQuestionRecycler.questionPaperResponse = viewModel.studentQuestionsAndOptions.value as ArrayList<StudentQuestionsAndOptions>
    }

    private fun updateQuestionAndOptionsWithSelectedOptions() {
        viewModel.studentQuestionsAndOptions.value?.forEachIndexed { questionIndex, questionsAndOptions ->
            viewModel.studentSelectedOption.value?.forEachIndexed { selectedOptionIndex, selectedOption ->
                if (questionsAndOptions.questionId == selectedOption.questionId) {
                    selectedOption.selectedOpt?.let {
                        if (it.contains("\"")) {
                            viewModel.studentQuestionsAndOptions.value!![questionIndex].selectedOpt = selectedOption.selectedOpt.toString().replace("\"", "").trim()
                        } else {
                            viewModel.studentQuestionsAndOptions.value!![questionIndex].selectedOpt = selectedOption.selectedOpt?.trim()
                        }
                    }
                    viewModel.studentQuestionsAndOptions.value!![questionIndex].correctOpt = selectedOption.correctOpt.toString()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(studentQuestionsRequest: StudentQuestionsRequest?) = StudentViewAnswerSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PARAM_QUESTIONS_REQUEST, studentQuestionsRequest)
            }
        }
    }

    override fun onQuestionClicked(studentQuestionsAndOptions: StudentQuestionsAndOptions, position: Int) {
        binding.selectedPosition = position
        viewModel.selectedQuestionAndOption.value = studentQuestionsAndOptions
    }
}