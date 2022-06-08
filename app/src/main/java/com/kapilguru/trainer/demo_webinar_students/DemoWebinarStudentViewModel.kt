package com.kapilguru.trainer.demo_webinar_students

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DemoWebinarStudentViewModel(private val addWebinarRepository: DemoWebinarStudentRepository,
                                  application: Application
): AndroidViewModel(application) {

    var demoWebinarStudents: MutableLiveData<ApiResource<DemoWebinarStudentsResponse>> = MutableLiveData()
     var totalWebinars: MutableLiveData<String> = MutableLiveData()


    fun fetchStudentListApi(webinarId: String) {
        demoWebinarStudents.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                demoWebinarStudents.postValue(
                    ApiResource.success(
                        addWebinarRepository.getDemoWebinarStudentList(webinarId)
                    )
                )
            } catch (exception: HttpException) {
                demoWebinarStudents.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                demoWebinarStudents.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun fetchDemoStudentListApi(webinarId: String) {
        demoWebinarStudents.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                demoWebinarStudents.postValue(
                    ApiResource.success(
                        addWebinarRepository.getDemoStudentListApi(webinarId)
                    )
                )
            } catch (exception: HttpException) {
                demoWebinarStudents.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                demoWebinarStudents.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}