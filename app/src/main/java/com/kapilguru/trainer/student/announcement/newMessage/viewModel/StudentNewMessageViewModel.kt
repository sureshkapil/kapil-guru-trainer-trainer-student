package com.kapilguru.trainer.student.announcement.newMessage.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.student.announcement.StudentAnnouncementRepository
import com.kapilguru.trainer.student.announcement.newMessage.data.StudentNewMessageResponse
import com.kapilguru.trainer.student.announcement.newMessage.data.StudentSendNewMessageRequest
import com.kapilguru.trainer.student.announcement.newMessage.data.StudentSendNewMessageResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentNewMessageViewModel(private val studentAnnouncementRepository: StudentAnnouncementRepository) : ViewModel() {
    val TAG = "NewMessageViewModel"
    var batchListMutLiveData: MutableLiveData<ApiResource<StudentNewMessageResponse>> = MutableLiveData()
    var studentSendNewMessageResponce: MutableLiveData<ApiResource<StudentSendNewMessageResponce>> = MutableLiveData()
    var subjectMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var messageMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var selectedBatchId: MutableLiveData<String> = MutableLiveData("")


    fun getBatchList(userId: String) {
        batchListMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchListMutLiveData.postValue(
                    ApiResource.success(
                        studentAnnouncementRepository.getBatchList(
                            userId
                        )
                    )
                )
            } catch (exception: HttpException) {
                batchListMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                batchListMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun sendRequest(request: StudentSendNewMessageRequest) {
        studentSendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentSendNewMessageResponce.postValue(
                    ApiResource.success(
                        studentAnnouncementRepository.sendNewMessageRequest(
                            request
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                studentSendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

}