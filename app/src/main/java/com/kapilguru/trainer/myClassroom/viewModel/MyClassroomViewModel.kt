package com.kapilguru.trainer.myClassroom.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageResponse
import com.kapilguru.trainer.myClassroom.MyClassroomRepository
import com.kapilguru.trainer.myClassroom.liveClasses.model.LiveUpComingClassData
import com.kapilguru.trainer.myClassroom.liveClasses.model.LiveUpComingClassResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyClassroomViewModel(
    private val repository: MyClassroomRepository,
    application: Application
) :
    AndroidViewModel(application) {
    private val TAG = "MyClassroomViewModel"
    val trainerId = StorePreferences(application).userId.toString()
    val liveUpComingClassResponse: MutableLiveData<ApiResource<LiveUpComingClassResponse>> = MutableLiveData()
    val activeBatchListResponse: MutableLiveData<ApiResource<NewMessageResponse>> = MutableLiveData()
    val liveUpComingClassDataList: MutableLiveData<List<LiveUpComingClassData>> = MutableLiveData()
    val liveClasses: MutableLiveData<List<LiveUpComingClassData>> = MutableLiveData()
    val upComingClasses: MutableLiveData<List<LiveUpComingClassData>> = MutableLiveData()
    val activeBatchesList: MutableLiveData<List<NewMessageData>> = MutableLiveData()

    fun getAllClasses() {
        viewModelScope.launch {
            liveUpComingClassResponse.postValue(ApiResource.loading(data = null))
            try {
                val liveUpComingClassesDeferred = async { repository.getLiveUpComingClasses(trainerId) }
                val activeBatchesDeferred = async { repository.getBatchList(trainerId) }
                val liveUpComingClassRes = liveUpComingClassesDeferred.await()
                val activeBatchesRes = activeBatchesDeferred.await()
                liveUpComingClassResponse.postValue(ApiResource.success(data = liveUpComingClassRes))
                activeBatchListResponse.postValue(ApiResource.success(data = activeBatchesRes))
            } catch (e: IOException) {
                liveUpComingClassResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            } catch (e: HttpException) {
                liveUpComingClassResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            } catch (e: Exception) {
                liveUpComingClassResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateLiveUpComingClassList(updatedList: ArrayList<LiveUpComingClassData>) {
        liveUpComingClassDataList.value = null
        liveUpComingClassDataList.value = updatedList
    }

    fun getLiveUpComingClasses(trainerId: String) {
        liveUpComingClassResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                liveUpComingClassResponse.postValue(ApiResource.success(data = repository.getLiveUpComingClasses(trainerId)))
            } catch (exception: HttpException) {
                liveUpComingClassResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                liveUpComingClassResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}