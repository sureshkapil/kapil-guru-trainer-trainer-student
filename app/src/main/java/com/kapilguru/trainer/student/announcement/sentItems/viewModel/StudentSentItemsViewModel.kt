package com.kapilguru.trainer.student.announcement.sentItems.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.student.announcement.StudentAnnouncementRepository
import com.kapilguru.trainer.student.announcement.sentItems.data.StudentSentItemsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentSentItemsViewModel(private val studentAnnouncementRepository: StudentAnnouncementRepository) : ViewModel() {
    private val TAG = "SentItemsViewModel"

    var studentSentItemsResponse: MutableLiveData<ApiResource<StudentSentItemsResponse>> = MutableLiveData()


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
            } catch (exception: IOException) {
                exception.printStackTrace()
                studentSentItemsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}