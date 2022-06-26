package com.kapilguru.trainer.student.myClassroom.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.myClassroom.StudentMyClassroomRepository
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentActiveBatchData
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentAllClassesResponse
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentLiveUpComingClassData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentMyClassroomViewModel(private val repository: StudentMyClassroomRepository, application: Application) : AndroidViewModel(application) {
    private val TAG = "MyClassroomViewModel"
    val userId = StorePreferences(application).userId.toString()
    val studentAllClassesResponse: MutableLiveData<ApiResource<StudentAllClassesResponse>> = MutableLiveData()
    val studentLiveUpComingClassDataList: MutableLiveData<List<StudentLiveUpComingClassData>> = MutableLiveData()
    val liveClasses: MutableLiveData<List<StudentLiveUpComingClassData>> = MutableLiveData()
    val upComingClasses: MutableLiveData<List<StudentLiveUpComingClassData>> = MutableLiveData()
    val activeBatchesList: MutableLiveData<List<NewMessageData>> = MutableLiveData()
    val activeBatchesList1: ArrayList<StudentActiveBatchData> = ArrayList()

    fun getAllClasses() {
        studentAllClassesResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentAllClassesResponse.postValue(ApiResource.success(data = repository.getAllStudentClasses(userId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                studentAllClassesResponse.postValue(ApiResource.error(data = null, message = exception.message, code = exception.code))
            } catch (exception: IOException) {
                studentAllClassesResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                studentAllClassesResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            }
        }
    }

    fun updateLiveUpComingClassList(updatedList: ArrayList<StudentLiveUpComingClassData>) {
        studentLiveUpComingClassDataList.value = null
        studentLiveUpComingClassDataList.value = updatedList
        Log.d(TAG, "updateLiveUpComingClassList: " + liveClasses.value.toString())
    }

    fun updateActiveClasses() {

    }
}