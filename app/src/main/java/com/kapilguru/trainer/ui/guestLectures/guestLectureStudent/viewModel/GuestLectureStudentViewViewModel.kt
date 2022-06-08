package com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.GuestLectureStudentViewRepository
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.model.GuestLectureStudentViewRes
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.model.GuestLectureStudentViewResData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GuestLectureStudentViewViewModel(private val repository: GuestLectureStudentViewRepository) : ViewModel() {
    private val TAG = "DemoLectureDetailsViewModel"
    var demoLectureIntent: Int? = -1 // demo lecture data from intent
    var demoLectureDetailsApi: MutableLiveData<GuestLectureStudentViewResData> = MutableLiveData(GuestLectureStudentViewResData()) // demo lecture data from API
    val demoLectureDetailsApiRes: MutableLiveData<ApiResource<GuestLectureStudentViewRes>> = MutableLiveData()

    fun getDemoLectureDetails() {
        demoLectureDetailsApiRes.value = ApiResource.loading(data = null)
        if (demoLectureIntent != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    demoLectureDetailsApiRes.postValue(ApiResource.success(data = repository.getGuestLectureStudentViewDetails(demoLectureIntent.toString())))
                } catch (exception: HttpException) {
                    demoLectureDetailsApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                } catch (e: IOException) {
                    demoLectureDetailsApiRes.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
                }
            }
        }
    }
}