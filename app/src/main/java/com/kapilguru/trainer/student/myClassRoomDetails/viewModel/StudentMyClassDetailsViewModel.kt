package com.kapilguru.trainer.student.myClassRoomDetails.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.myClassRoomDetails.StudentMyClassRoomDetailsRepository
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListRequest
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListResponse
import com.kapilguru.trainer.student.myClassRoomDetails.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentMyClassDetailsViewModel(private val repository: StudentMyClassRoomDetailsRepository, application: Application) : AndroidViewModel(application) {
    private val TAG = "BatchCompletionReqViewModel"

    var batchApiResponse: MutableLiveData<ApiResource<StudentBatchDetailsResponse>> = MutableLiveData()
    var reviewStudentRequest: MutableLiveData<ReviewStudentRequest> = MutableLiveData(ReviewStudentRequest(0f, "", 0, ""))
    var reviewStudentResponse: MutableLiveData<ApiResource<ReviewStudentResponse>> = MutableLiveData()
    var batchId: MutableLiveData<String?> = MutableLiveData("")
    val isComplaint: MutableLiveData<Boolean> = MutableLiveData(false)

    var raiseComplaintByStudentResponse: MutableLiveData<ApiResource<RaiseComplaintByStudentResponse>> = MutableLiveData()
    var refundStudentResponse: MutableLiveData<ApiResource<RefundStudentResponse>> = MutableLiveData()

    var examListApiRes: MutableLiveData<ApiResource<StudentQuestionPaperListResponse>> = MutableLiveData()
    var examListApiReq: MutableLiveData<StudentQuestionPaperListRequest> = MutableLiveData()
    var batchCode: String = ""

    var studentStudyMaterialResponse: MutableLiveData<ApiResource<StudentStudyMaterialResponse>> = MutableLiveData()
    var studentStudyMaterialResponseApi: MutableLiveData<List<StudentStudyMaterialResponseApi>> = MutableLiveData()

    fun getBatchDetails() {
        batchApiResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchApiResponse.postValue(ApiResource.success(repository.getBatchDetails(batchId.value!!)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                batchApiResponse.postValue(ApiResource.error(data = null, message = exception.message, exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                batchApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                batchApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun raiseComplaint(raiseComplaintByStudentRequest: RaiseComplaintByStudentRequest) {
        raiseComplaintByStudentResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                raiseComplaintByStudentResponse.postValue(ApiResource.success(repository.raiseComplaintByStudent(raiseComplaintByStudentRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                raiseComplaintByStudentResponse.postValue(ApiResource.error(data = null, message = exception.message, exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                raiseComplaintByStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                raiseComplaintByStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateReview(reviewStudentRequest: ReviewStudentRequest) {
        reviewStudentResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                reviewStudentResponse.postValue(ApiResource.success(repository.updateReviewByStudent(reviewStudentRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                reviewStudentResponse.postValue(ApiResource.error(data = null, message = exception.message, exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                reviewStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                reviewStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun refundRequest(refundStudentRequest: RefundStudentRequest) {
        refundStudentResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refundStudentResponse.postValue(ApiResource.success(repository.requestRefundByStudent(refundStudentRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                refundStudentResponse.postValue(ApiResource.error(data = null, message = exception.message, exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                refundStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                refundStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getQuestionPaper() {
        examListApiRes.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                examListApiRes.postValue(ApiResource.success(repository.getStudentExamList(examListApiReq.value!!)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                examListApiRes.postValue(ApiResource.error(data = null, message = exception.message, exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                examListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                examListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun getStudyMaterial(batchId: String) {
        studentStudyMaterialResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentStudyMaterialResponse.postValue(ApiResource.success(repository.getStudentStudyMaterialList(batchId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                studentStudyMaterialResponse.postValue(ApiResource.error(data = null, message = exception.message, exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                studentStudyMaterialResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                studentStudyMaterialResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

}