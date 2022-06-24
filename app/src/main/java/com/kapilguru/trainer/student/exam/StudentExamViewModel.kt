package com.kapilguru.trainer.student.exam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.exam.model.*
import com.kapilguru.trainer.studentExamBatchResult.StudentReportRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentExamViewModel(val repository: StudentExamRepository, val app: Application) : AndroidViewModel(app) {

    var studentReportResponseApi: MutableLiveData<ApiResource<StudentReportResponse>> = MutableLiveData()
    var studentResult: MutableLiveData<StudentResult> = MutableLiveData()

    // Question api response
    var studentQuestionsReponse: MutableLiveData<ApiResource<StudentQuestionsReponse>> = MutableLiveData()

    // Question api response is stored here
    var studentQuestionsAndOptions: MutableLiveData<ArrayList<StudentQuestionsAndOptions>> = MutableLiveData()

    // Question api response is stored
    var studentSelectedOption: MutableLiveData<ArrayList<StudentSelectedOption>> = MutableLiveData()

    // user selceted options are stored
    var selectedQuestionAndOption: MutableLiveData<StudentQuestionsAndOptions> = MutableLiveData()


    fun getStudentReport(request: StudentReportRequest) {
        studentReportResponseApi.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentReportResponseApi.postValue(
                    ApiResource.success(data = repository.getStudentReport(request))
                )
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                studentReportResponseApi.postValue(ApiResource.error(data = null, message = http.message, code = http.code))
            } catch (http: HttpException) {
                studentReportResponseApi.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                studentReportResponseApi.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    fun getQuestionsRequest(request: StudentQuestionsRequest) {
        studentQuestionsReponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentQuestionsReponse.postValue(
                    ApiResource.success(data = repository.getQuestions(request))
                )
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                studentQuestionsReponse.postValue(ApiResource.error(data = null, message = http.message, http.code))
            } catch (http: HttpException) {
                studentQuestionsReponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                studentQuestionsReponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

}