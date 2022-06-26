package com.kapilguru.trainer.student.exam.questionPaper.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponse
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.exam.model.*
import com.kapilguru.trainer.student.exam.questionPaper.StudentQuestionPaperRepository
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListItemResData
import com.kapilguru.trainer.toBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.HttpException
import java.io.IOException

class StudentQuestionPaperViewModel(private val repository: StudentQuestionPaperRepository) : ViewModel() {
    //Object from intent
    lateinit var questionPaperInfo: StudentQuestionPaperListItemResData

    // Question api response
    var questionsResponse: MutableLiveData<ApiResource<StudentQuestionsReponse>> = MutableLiveData()

    // Question api response is stored here
    var studentQuestionsAndOptions: MutableLiveData<ArrayList<StudentQuestionsAndOptions>> = MutableLiveData(ArrayList())

    // submit request - questionsList
    var submitAllQuestions: MutableLiveData<ArrayList<StudentSubmitQuestionRequest>> = MutableLiveData(ArrayList())

    //current question Index
    var currentQuestionIndex: MutableLiveData<Int> = MutableLiveData()

    // Current question
    var currentQuestionAndOption: MutableLiveData<StudentQuestionsAndOptions> = MutableLiveData()
    var isNextQuestionClicked = false
    var submitQuestionApiResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    var submitAllQuestionsApiResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    var isSubmitClicked = false

    fun getQuestionsRequest(request: StudentQuestionsRequest) {
        questionsResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionsResponse.postValue(ApiResource.success(data = repository.getQuestions(request)))
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                questionsResponse.postValue(ApiResource.error(data = null, message = http.message, http.code))
            } catch (http: HttpException) {
                questionsResponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                questionsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    fun submitQuestionResponse(selectedOption: String?) {
        val request = getSubmitQuestionRequest(selectedOption)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                submitQuestionApiResponse.postValue(ApiResource.success(data = repository.submitQuestion(request)))
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                submitQuestionApiResponse.postValue(ApiResource.error(data = null, message = http.message, http.code))
            } catch (http: HttpException) {
                submitQuestionApiResponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                submitQuestionApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    private fun getSubmitQuestionRequest(optionSelected: String?): StudentSubmitQuestionRequest {
        return StudentSubmitQuestionRequest(
            courseId = questionPaperInfo.courseId,
            questionPaperId = questionPaperInfo.questionPaperId,
            batchId = questionPaperInfo.batchId,
            isAttempted = 1,
            studentId = questionPaperInfo.studentId.toString(),
            questionId = currentQuestionAndOption.value?.questionId!!,
            selectedOpt = optionSelected,
            trainerId = questionPaperInfo.trainerId,
            examId = questionPaperInfo.examId
        )
    }

    fun submitAllQuestionsResponse() {
        submitAllQuestionsApiResponse.value = ApiResource.loading(data = null)
        val request = getSubmitAllQuestionsRequest()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                submitAllQuestionsApiResponse.postValue(ApiResource.success(data = repository.submitAllQuestions(request)))
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                submitAllQuestionsApiResponse.postValue(ApiResource.error(data = null, message = http.message, http.code))
            } catch (http: HttpException) {
                submitAllQuestionsApiResponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                submitAllQuestionsApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    private fun getSubmitAllQuestionsRequest(): StudentSubmitAllQuestionsRequest {
        val submitAllQuestionsList: ArrayList<StudentSubmitQuestionRequest>? = submitAllQuestions.value
        val jsonArray1 = JSONArray()
        submitAllQuestionsList?.let { listNotNull ->
            for (question in listNotNull) {
                val jsonObject = question.getJsonObject()
                jsonArray1.put(jsonObject)
            }
        }
        val encodedString = jsonArray1.toString().toBase64()
        return StudentSubmitAllQuestionsRequest(encodedString)
    }
}