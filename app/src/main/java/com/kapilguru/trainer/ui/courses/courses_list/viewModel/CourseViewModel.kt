package com.kapilguru.trainer.ui.courses.courses_list.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.courses_list.CourseRepository
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CourseViewModel(private val courseRepository: CourseRepository, application: Application) : AndroidViewModel(application) {

    private val TAG = "CourseViewModel"
    var totalCourses: MutableLiveData<String> = MutableLiveData("0")
    var trainerId: Int
    var deleteResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()
    var deleteCourseId: String?=null
    var courseList: MutableLiveData<ApiResource<CourseApi>> = MutableLiveData()

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
    }

    fun getCourseList() {
        viewModelScope.launch(Dispatchers.IO) {
            courseList.postValue(ApiResource.loading(data = null))
            try {
                courseList.postValue(ApiResource.success(data = courseRepository.getCousesList(trainerId.toString())))
            } catch (exception: HttpException) {
                courseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                courseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun deleteCourse(courseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteResponse.postValue(ApiResource.loading(data = null))
            try {
                deleteResponse.postValue(ApiResource.success(data = courseRepository.deleteCourse(courseId)))
            } catch (exception: HttpException) {
                deleteResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                deleteResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


}