package com.kapilguru.trainer.ui.webiner.addWebinar.viewModel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.MP4
import com.kapilguru.trainer.PNG
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.toBase64
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourseResponse
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadVideoResponse
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.webiner.addWebinar.AddWebinarRepository
import com.kapilguru.trainer.ui.webiner.addWebinar.model.AddWebinarDetailsResData
import com.kapilguru.trainer.ui.webiner.addWebinar.model.AddWebinarDetailsResponse
import com.kapilguru.trainer.ui.webiner.addWebinar.model.AddWebinarRequest
import com.kapilguru.trainer.ui.webiner.model.WebinarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.util.*

class AddWebinarViewModel(private val repository: AddWebinarRepository,val context: Application) : AndroidViewModel(context) {
    val TAG = "AddWebinarViewModel"
    var currentIndex: MutableLiveData<Int> = MutableLiveData(0)
    var isLaunchedForEdit: Boolean = false
    var webinarResponse: MutableLiveData<ApiResource<WebinarResponse>> = MutableLiveData()
    var addWebinarRequest: MutableLiveData<AddWebinarRequest> = MutableLiveData(AddWebinarRequest())
    var addWebinarDetailsApiResponse: MutableLiveData<ApiResource<AddWebinarDetailsResponse>> = MutableLiveData()
    var addWebinarDetailsResData = AddWebinarDetailsResData()
    var addWebinarImageRequest = UploadImageCourse()
    var addWebinarImageApiResponse: MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()
    //    var addWebinarVideoRequest
    var addWebinarVideoApiResponse: MutableLiveData<ApiResource<UploadVideoResponse>> = MutableLiveData()
    var updateWebinarApiResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()
    var updateEditWebinarApi: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()
    var languagesForWebinar: MutableLiveData<String> = MutableLiveData()
    var webinarInsertId: MutableLiveData<Int> = MutableLiveData(0)
    var webinarEditId: MutableLiveData<Int> = MutableLiveData(0)
    var selectedLanguages: ArrayList<LanguageData> = ArrayList<LanguageData>()
    var selectedLanguagesString: MutableLiveData<String> = MutableLiveData()
    var selectedLanguagesIds: MutableLiveData<String> = MutableLiveData()
    var isNewImageSelected = false

    var webinarLanguages = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = repository.getWebinarLanguages()))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun setIsLaunchedForEdit(value: Boolean) {
        isLaunchedForEdit = value
    }

    fun getIsLaunchedForEdit(): Boolean {
        return isLaunchedForEdit
    }

    fun setIsNewImageSelected(isSelected: Boolean) {
        isNewImageSelected = isSelected
    }

    fun getIsNewImageSelected(): Boolean = isNewImageSelected

    fun addValuesToWebinarReqData() {
        addWebinarRequest.value?.trainerID = StorePreferences(context).userId.toString()
        addWebinarRequest.value?.languages = selectedLanguagesIds.value?.toBase64()
    }

    fun fetchWebinarDetails() {
        webinarResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                webinarResponse.postValue(ApiResource.success(repository.getWebinarDetails(webinarEditId.value.toString())))
            } catch (e: HttpException) {
                webinarResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                webinarResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
            }
        }
    }

    /*for the first time we are adding a new webinar i.e, only details are sent to server*/
    fun addWebinarDetails() {
        addWebinarDetailsApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                addWebinarDetailsApiResponse.postValue(ApiResource.success(repository.addWebinarDetails(addWebinarRequest.value!!)))
            } catch (e: HttpException) {
                addWebinarDetailsApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                addWebinarDetailsApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
            }
        }
    }

    /*isForFirstTime - image request source id is addGuestLectureRes insertId,
  * if not for first time i.e, if for edit - then source id is addGuestLecture Id*/
    private fun setWebinarImageRequest(imageBase64: String, isForEdit: Boolean) {
        addWebinarImageRequest.code = addWebinarDetailsResData.code!!
        addWebinarImageRequest.sourceId = if (isForEdit) addWebinarDetailsResData.id else addWebinarDetailsResData.insertId
        addWebinarImageRequest.sourceType = "Webinar"
        addWebinarImageRequest.baseImage = imageBase64
    }

    fun addWebinarImage(imageBase64: String) {
        setWebinarImageRequest(imageBase64, getIsLaunchedForEdit())
        addWebinarImageApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                addWebinarImageApiResponse.postValue(ApiResource.success(repository.addWebinarImage(addWebinarImageRequest)))
            } catch (e: HttpException) {
                addWebinarImageApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                addWebinarImageApiResponse.postValue(ApiResource.error(data = null, message = e.message?: "Error Occurred !!"))
            }
        }
    }

    fun uploadVideo(file: File, trainerId: String, sourceId: String, code: String, videoType: String) {
        addWebinarVideoApiResponse.postValue(ApiResource.loading(null))
        Log.d(TAG, "uploadVideo: $trainerId --> $sourceId --> $code --> $videoType -->  ${file.name}")
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val trainerId = trainerId.toRequestBody("text/plain".toMediaType())
                val sourceId = sourceId.toRequestBody("text/plain".toMediaType())
                val code = code.toRequestBody("text/plain".toMediaType())
                val videoType = videoType.toRequestBody("text/plain".toMediaType())
                val file_size = (file.length() / 1024).toString().toInt()
                val requestFile: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)

                addWebinarVideoApiResponse.postValue(ApiResource.success(repository.uploadVideo(body, trainerId,sourceId, code, videoType)))
            } catch (e: HttpException) {
                addWebinarVideoApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
            }catch (e: IOException) {
                addWebinarVideoApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
            catch(e: Exception){
                addWebinarVideoApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun saveWebinar() {
        if (isLaunchedForEdit) {
            updateEditWebinar()
        } else {
            updateWebinar()
        }
    }

    fun updateWebinar() {
        updateWebinarApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            val id = if (getIsLaunchedForEdit()) addWebinarDetailsResData.id else addWebinarDetailsResData.insertId
            try {
                updateWebinarApiResponse.postValue(ApiResource.success(repository.updateWebinar(id.toString(), addWebinarRequest.value!!)))
            } catch (e: HttpException) {
                updateWebinarApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                updateWebinarApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
            }
        }
    }

    fun updateEditWebinar() {
        updateEditWebinarApi.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                updateEditWebinarApi.postValue(ApiResource.success(repository.updateEditWebinarDetails(webinarEditId.value.toString(), this@AddWebinarViewModel.addWebinarRequest.value!!)))
            } catch (e: HttpException) {
                updateEditWebinarApi.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                updateEditWebinarApi.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
            }
        }
    }

    /*   fun updateLanguageSelection(selectedList : ArrayList<LanguageData>){
           Log.d(TAG,"updateLanguageSelection")
           selectedLanguages  = selectedList
           val selectedString = StringBuilder()
           val selectedIds = StringBuilder()
           for(i in selectedLanguages){
               if(i.isSelected)
                   selectedString.append(i.name+", ")
               selectedIds.append(i.id.toString()+", ")
           }
           selectedLanguagesString.value = selectedString.toString().trim()
           if(selectedLanguagesString.value!!.endsWith(",")){
               selectedLanguagesString.value = selectedLanguagesString.value!!.substring(0,selectedLanguagesString.value!!.length-1)
           }
           selectedLanguagesIds.value = selectedIds.toString().trim()
           if(selectedLanguagesIds.value!!.endsWith(",")){
               selectedLanguagesIds.value = selectedLanguagesIds.value!!.substring(0,selectedLanguagesString.value!!.length-1)
           }
       }*/

    fun onImageUploaded(isSuccess: Boolean) {
        if (isSuccess) {
            setIsNewImageSelected(false)
            addWebinarRequest.value?.image = addWebinarDetailsResData.code + PNG /*Image is added to Guest Lecture after successful upload of image*/
        }
    }

    fun onVideoUploaded(isSuccess: Boolean) {
        if (isSuccess) {
            addWebinarRequest.value?.video = addWebinarDetailsResData.code + MP4 /*Video is added to Guest Lecture after successful upload of video*/
        }
    }

    fun updateLanguageSelection(selectedList: ArrayList<LanguageData>) {
        selectedLanguages = selectedList
        val selectedString = StringBuilder()
        val selectedIds = StringBuilder()
        for (i in selectedLanguages) {
            if (i.isSelected) {
                selectedString.append(i.name + ", ")
                selectedIds.append(i.id.toString() + ", ")
            }
        }
        selectedLanguagesString.value = selectedString.toString().trim()
        if (selectedLanguagesString.value!!.endsWith(",")) {
            selectedLanguagesString.value = selectedLanguagesString.value!!.substring(0, selectedLanguagesString.value!!.length - 1)
        }
        selectedLanguagesIds.value = selectedIds.toString().trim()
        if (selectedLanguagesIds.value!!.endsWith(",")) {
            selectedLanguagesIds.value = selectedLanguagesIds.value!!.substring(0, selectedLanguagesIds.value!!.length - 1)
            Log.d(TAG, "selectedLanguagesIds.value  " + selectedLanguagesIds.value)
        }
    }

    fun isDataValid(startTime : String?): Boolean {
        if (TextUtils.isEmpty(this.addWebinarRequest.value?.title)) {
            showToast("Please enter Webinar Title")
            return false
        }
        if (TextUtils.isEmpty(this.addWebinarRequest.value?.startDate)) {
            showToast("Please select Webinar Start Date")
            return false
        }
        if (TextUtils.isEmpty(this.addWebinarRequest.value?.endDate)) {
            showToast("Please select Webinar End Date")
            return false
        }
        if (TextUtils.isEmpty(startTime)) {
            showToast("Please select Webinar Start Time")
            return false
        }
        if (TextUtils.isEmpty(addWebinarRequest.value?.durationHrs)
            || addWebinarRequest.value?.durationHrs == "0"
        ) {
            showToast("Please select Webinar Duration")
            return false
        }
        if (TextUtils.isEmpty(selectedLanguagesIds.value)) {
            showToast("Please Select Languages")
            return false
        }
        if (TextUtils.isEmpty(addWebinarRequest.value?.noOfAttendees)) {
            showToast("Please enter number of attendees")
            return false
        }
        if (addWebinarRequest.value?.noOfAttendees!!.toInt()<=0) {
            showToast("Attendees can't be 0")
            return false
        }
        if (addWebinarRequest.value?.noOfAttendees!!.toInt()>50) {
            showToast("Maximum Attendees can't exceed 50")
            return false
        }
        if (TextUtils.isEmpty(addWebinarRequest.value?.price)) {
            showToast("Please enter Webinar Price")
            return false
        }
        if (addWebinarRequest.value?.price!!.toInt()>100000) {
            showToast("Maximum Webinar price is 100000")
            return false
        }
        if (addWebinarRequest.value?.price!!.toInt()<100) {
            showToast("Minimum Webinar price is 100")
            return false
        }
        if (TextUtils.isEmpty(this.addWebinarRequest.value?.speakerName)) {
            showToast("Please enter Webinar Speaker name")
            return false
        }
        return true
    }

    fun showToast(text: String) {
        Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show()
    }
}