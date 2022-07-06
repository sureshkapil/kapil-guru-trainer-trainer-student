package com.kapilguru.trainer.student.announcement.inbox.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.student.announcement.StudentAnnouncementRepository
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentInboxViewModel(private val studentAnnouncementRepository: StudentAnnouncementRepository) : ViewModel() {
    private val TAG = "InboxViewModel"

    var resultDat: MutableLiveData<ApiResource<StudentInboxResponse>> = MutableLiveData()

    // set initial count as 0
    var inboxItemCount: MutableLiveData<String> = MutableLiveData("0")

    fun getInBoxResponse(userId: String) {
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
}