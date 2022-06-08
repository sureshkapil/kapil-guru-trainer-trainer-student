package com.kapilguru.trainer.studentExamBatchResult

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception


class StudentExamBatchResultViewModel(private val repository: StudentExamBatchResultRepository, application: Application) : AndroidViewModel(application) {

    var batchExamStudentResultApi: MutableLiveData<ApiResource<BatchExamStudentResultApi>> = MutableLiveData()
    var studentResult: MutableLiveData<StudentResult> = MutableLiveData()
    var studentAnswerSheetApi: MutableLiveData<ApiResource<StudentAnswerSheetApi>> = MutableLiveData()
    var questionPaperResponse: MutableLiveData<QuestionPaperResponse> = MutableLiveData()
    var questionPaperResponseList: MutableLiveData<List<QuestionPaperResponse>> = MutableLiveData()


    fun getStudentsReports(request: StudentReportRequest) {
        batchExamStudentResultApi.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchExamStudentResultApi.postValue(ApiResource.success(data = repository.getStudentReport(request))
                )
            } catch (http: HttpException) {
                batchExamStudentResultApi.postValue(ApiResource.error(data = null, message = http.message()))
                batchExamStudentResultApi.value
            } catch (exception: IOException) {
                batchExamStudentResultApi.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    fun getExamResult(request: StudentExamPaperRequest) {
        studentAnswerSheetApi.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentAnswerSheetApi.postValue(ApiResource.success(data = repository.getAnswerSheet(request))
                )
            } catch (http: HttpException) {
                studentAnswerSheetApi.postValue(ApiResource.error(data = null, message = http.message()))
                batchExamStudentResultApi.value
            } catch (exception: IOException) {
                studentAnswerSheetApi.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }


}