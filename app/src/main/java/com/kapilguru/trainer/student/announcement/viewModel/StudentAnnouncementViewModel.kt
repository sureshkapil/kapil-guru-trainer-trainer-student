package com.kapilguru.trainer.student.announcement.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponse
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.announcement.StudentAnnouncementRepository
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxResponse
import com.kapilguru.trainer.student.announcement.inbox.data.StudentLastMessageRequest
import com.kapilguru.trainer.student.announcement.newMessage.data.*
import com.kapilguru.trainer.student.announcement.sentItems.data.StudentSentItemsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentAnnouncementViewModel(private val studentAnnouncementRepository: StudentAnnouncementRepository, var contxt: Application) : AndroidViewModel(contxt) {
    private val TAG = "SentItemsViewModel"
    var studentSentItemsResponse: MutableLiveData<ApiResource<StudentSentItemsResponse>> = MutableLiveData()
    var batchListMutLiveData: MutableLiveData<ApiResource<StudentNewMessageResponse>> = MutableLiveData()
    var adminListMutLiveData: MutableLiveData<ApiResource<StudentAdminMessageResponse>> = MutableLiveData()
    var studentSendNewMessageResponce: MutableLiveData<ApiResource<StudentSendNewMessageResponce>> = MutableLiveData()
    var subjectMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var messageMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var selectedBatchReceiverId: MutableLiveData<String> = MutableLiveData("")
    var selectedAdminUserId: MutableLiveData<String> = MutableLiveData("")
    var isAdminChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    var resultDat: MutableLiveData<ApiResource<StudentInboxResponse>> = MutableLiveData()
    var commonResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()

    // set initial count as 0
    var inboxItemCount: MutableLiveData<String> = MutableLiveData("0")

    fun getInboxResponce(userId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(ApiResource.success(studentAnnouncementRepository.getInBoxResponse(userId)))
            } catch (exception: HttpException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun getBatchList(userId: String) {
        batchListMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchListMutLiveData.postValue(ApiResource.success(studentAnnouncementRepository.getBatchList(userId)))
            } catch (exception: HttpException) {
                batchListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                batchListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getAdminList() {
        adminListMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                adminListMutLiveData.postValue(ApiResource.success(studentAnnouncementRepository.getAdminList()))
            } catch (exception: HttpException) {
                adminListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                adminListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun sendBatchRequest(request: StudentSendNewMessageRequest) {
        studentSendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentSendNewMessageResponce.postValue(ApiResource.success(studentAnnouncementRepository.sendNewMessageRequest(request)))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                studentSendNewMessageResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                studentSendNewMessageResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun sendAdminRequest(request: StudentSendAdminMessageRequest) {
        studentSendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentSendNewMessageResponce.postValue(
                    ApiResource.success(
                        studentAnnouncementRepository.sendAdminMessageRequest(request)
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                studentSendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message, code = exception.code)
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                studentSendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                exception.printStackTrace()
                studentSendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun getSentItemsResponse(userId: String) {
        studentSentItemsResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentSentItemsResponse.postValue(
                    ApiResource.success(
                        studentAnnouncementRepository.getSentItemsResponse(
                            userId
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                studentSentItemsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun updateLastMessageId(messageId: Int) {
        commonResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                commonResponse.postValue(
                    ApiResource.success(
                        studentAnnouncementRepository.updateLastMessageId(StorePreferences(contxt).userId.toString(), StudentLastMessageRequest().apply { lastAnnouncementId = messageId })
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                commonResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}