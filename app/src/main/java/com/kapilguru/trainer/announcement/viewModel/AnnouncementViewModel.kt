package com.kapilguru.trainer.announcement.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.announcement.AnnouncementRepository
import com.kapilguru.trainer.announcement.inbox.data.InboxResponse
import com.kapilguru.trainer.announcement.newMessage.data.*
import com.kapilguru.trainer.announcement.sentItems.data.LastMessageRequest
import com.kapilguru.trainer.announcement.sentItems.data.SentItemsResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponse
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AnnouncementViewModel(private val announcementRepository: AnnouncementRepository,var contxt: Application) : AndroidViewModel(contxt) {
    private val TAG = "SentItemsViewModel"
    var sentItemsResponse: MutableLiveData<ApiResource<SentItemsResponse>> = MutableLiveData()
    var batchListMutLiveData: MutableLiveData<ApiResource<NewMessageResponse>> = MutableLiveData()
    var adminListMutLiveData: MutableLiveData<ApiResource<GetAdminListRes>> = MutableLiveData()
    var sendNewMessageResponce: MutableLiveData<ApiResource<SendNewMessageResponce>> = MutableLiveData()
    var subjectMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var messageMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var selectedBatchId: MutableLiveData<String> = MutableLiveData("")
    var selectedAdminId: MutableLiveData<String> = MutableLiveData("")
    var isAdminChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    var resultDat: MutableLiveData<ApiResource<InboxResponse>> = MutableLiveData()
    var commonResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()

    // set initial count as 0
    var inboxItemCount: MutableLiveData<String> = MutableLiveData("0")

    fun getInboxResponce(userId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(ApiResource.success(announcementRepository.getInboxResponce(userId)))
            } catch (exception: HttpException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getBatchList(userId: String) {
        batchListMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchListMutLiveData.postValue(ApiResource.success(announcementRepository.getBatchList(userId)))
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
                adminListMutLiveData.postValue(ApiResource.success(announcementRepository.getAdminList()))
            } catch (exception: HttpException) {
                adminListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                adminListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun sendBatchRequest(request: SendBatchMessageRequest) {
        sendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendNewMessageResponce.postValue(ApiResource.success(announcementRepository.sendBatchMessageRequest(request)))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun sendAdminRequest(request: SendAdminMessageRequest) {
        sendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendNewMessageResponce.postValue(
                    ApiResource.success(
                        announcementRepository.sendAdminMessageRequest(request)
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun getSentItemsResponse(userId: String) {
        sentItemsResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sentItemsResponse.postValue(
                    ApiResource.success(
                        announcementRepository.getSentItemsResponce(
                            userId
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                sentItemsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateLastMessageId(messageId: Int) {
        commonResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                commonResponse.postValue(
                    ApiResource.success(
                        announcementRepository.updateLastMessageId(
                           StorePreferences(contxt).trainerId.toString(), LastMessageRequest().apply { lastAnnouncementId = messageId }
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                commonResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}