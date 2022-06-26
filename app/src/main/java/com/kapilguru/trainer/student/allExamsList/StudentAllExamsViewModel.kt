package com.kapilguru.trainer.student.allExamsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentAllExamsViewModel(private val repository: AllStudentExamsListRepository) : ViewModel() {
    private val TAG = "CompletionRequestViewModel"
    val examsListResponseAPi: MutableLiveData<ApiResource<StudentQuestionPaperListResponse>> = MutableLiveData()


    fun getCompletionRequests(studentId: String) {
        examsListResponseAPi.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                examsListResponseAPi.postValue(ApiResource.success(data = repository.getAllStudentExamsList(studentId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                examsListResponseAPi.postValue(ApiResource.error(data = null, exception.message, exception.code))
            } catch (exception: HttpException) {
                examsListResponseAPi.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            } catch (exception: IOException) {
                examsListResponseAPi.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!"))
            }
        }
    }


}