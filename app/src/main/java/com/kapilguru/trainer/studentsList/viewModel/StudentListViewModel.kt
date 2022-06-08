package com.kapilguru.trainer.studentsList.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.studentsList.StudentListRepository
import com.kapilguru.trainer.studentsList.model.AllStudentsListPerTrainerApi
import com.kapilguru.trainer.studentsList.model.RequestRaiseComplaint
import com.kapilguru.trainer.studentsList.model.StudentDetails
import com.kapilguru.trainer.ui.profile.data.SaveProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentListViewModel(private val studentListRepository: StudentListRepository) : ViewModel() {

    var resultDat: MutableLiveData<ApiResource<AllStudentsListPerTrainerApi>> = MutableLiveData()


    var raiseComplaintResult: MutableLiveData<ApiResource<SaveProfileResponse>> = MutableLiveData()

    var studentDetails: MutableLiveData<StudentDetails> = MutableLiveData()

    // set initial count as 0
    var studentCount: MutableLiveData<String> = MutableLiveData("0")


    var raiseComplaintText: MutableLiveData<String> = MutableLiveData()


    fun fetchStudentListApi(trainerId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(
                    ApiResource.success(
                        studentListRepository.getTrainerStudentList(
                            trainerId
                        )
                    )
                )
            } catch (exception: HttpException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }

        }
    }

    fun fetchStudentListForCourseApi(courseId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(
                    ApiResource.success(
                        studentListRepository.getTrainerCourseStudentList(courseId)
                    )
                )
            } catch (exception: HttpException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun fetchStudentListForBatchApi(batchId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(
                    ApiResource.success(
                        studentListRepository.fetchStudentListForBatchApi(batchId)
                    )
                )
            } catch (exception: HttpException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun fetchStudentsListForWebinar(webinarId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(
                    ApiResource.success(
                        studentListRepository.getTrainerCourseStudentList(webinarId)
                    )
                )
            } catch (exception: HttpException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun raiseComplaintRequest(request: RequestRaiseComplaint) {
        raiseComplaintResult.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                raiseComplaintResult.postValue(ApiResource.success(studentListRepository.postComplaint(request)))
            } catch (exception: HttpException) {
                raiseComplaintResult.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                raiseComplaintResult.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

}