package com.kapilguru.trainer.ui.guestLectures.addGuestLecture.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.*
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourseResponse
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadVideoResponse
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.AddGuestLectureRepository
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureRequest
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureResData
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureResponse
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.guestLectures.model.EditGuestLectureResponse
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
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddGuestLectureViewModel(private val repository: AddGuestLectureRepository, val context: Application) :
    AndroidViewModel(context) {
    private val TAG = "AddGuestLectureViewModel"
    var currentIndex: MutableLiveData<Int> = MutableLiveData(0)
    var trainerId: Int = StorePreferences(context).userId
    private var isLaunchedForEdit: Boolean = false
    var addGuestLectureRequest: AddGuestLectureRequest = AddGuestLectureRequest()
    var addGuestLectureDetailsApiResponse: MutableLiveData<ApiResource<AddGuestLectureResponse>> = MutableLiveData()
    var addGuestLectureDetailsResData = AddGuestLectureResData()
    var addGuestLectureImageRequest = UploadImageCourse()
    var addGuestLectureImageApiResponse: MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()
    var datesJson: MutableLiveData<String> = MutableLiveData()
    var endCalendar: MutableLiveData<Calendar> = MutableLiveData()
    var endDate: MutableLiveData<String> = MutableLiveData()
    var endTime: MutableLiveData<String> = MutableLiveData()
    var endTimeCalendar: MutableLiveData<Calendar> = MutableLiveData()


    //    var addGuestLectureVideoRequest
    var addGuestLectureVideoApiResponse: MutableLiveData<ApiResource<AddGuestLectureResponse>> = MutableLiveData()
    var updateGuestLectureApiResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()
    var editGuestLectureApiResponse: MutableLiveData<ApiResource<EditGuestLectureResponse>> = MutableLiveData()
    var noOfAttendeesMutLiveData: MutableLiveData<String> = MutableLiveData()
    var selectedLanguages: ArrayList<LanguageData> = ArrayList<LanguageData>()
    var selectedLanguagesString: MutableLiveData<String> = MutableLiveData()
    var selectedLanguagesIds: MutableLiveData<String> = MutableLiveData()
    var isImageChangedAfterUpload = false

    // upload video
    var uploadVideoResponse: MutableLiveData<ApiResource<UploadVideoResponse>> = MutableLiveData()

    var courseLanguages = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = repository.getCourseLanguage()))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    var courses = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = repository.getCourses(trainerId.toString())))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        } catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun setIsLaunchedForEdit(value: Boolean) {
        isLaunchedForEdit = value
    }

    fun getIsLaunchedForEdit(): Boolean {
        return isLaunchedForEdit
    }

    fun setIsImageChangedAfterUpload(isUpdated: Boolean) {
        isImageChangedAfterUpload = isUpdated
    }

    fun getIsImageChanged(): Boolean = isImageChangedAfterUpload

    fun addValuesToGuestLecReqData() {
        addGuestLectureRequest.trainerId = trainerId
        if (!noOfAttendeesMutLiveData.value.isNullOrBlank()) {
            addGuestLectureRequest.noOfDays = Integer.valueOf(noOfAttendeesMutLiveData.value!!)
        } else {
            addGuestLectureRequest.noOfDays = null
        }
        addGuestLectureRequest.languages = selectedLanguagesIds.value?.toBase64()
        addGuestLectureRequest.datesJson = datesJson.value
        checkAndSetLectureDateInApiFormat1()
        checkAndSetLectureTimeInApiFormat()
        checkAndSetLectureEndDateInApiFormat()
    }

    /*Date and time are set differently*/
    private fun checkAndSetLectureDateInApiFormat() {
        val requiredCalender = Calendar.getInstance()
        val dateCalender = Calendar.getInstance()
        val timeCalender = Calendar.getInstance()
        val dateInputFormat: DateFormat = SimpleDateFormat("MM/dd/yy")
        val timeInputFormat: DateFormat = SimpleDateFormat("hh:mm aa")
        val outputFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        outputFmt.timeZone = TimeZone.getTimeZone("GMT")
        addGuestLectureRequest.lectureDate?.let { lectureDate ->
            addGuestLectureRequest.startTime?.let { startTime ->
                try{
                    dateCalender.time = dateInputFormat.parse(lectureDate)!!
                    timeCalender.time = timeInputFormat.parse(startTime)!!
//                addGuestLectureRequest.lectureDate = dateCalender.convertDateAndTimeToApiFormat(timeCalender)
                    requiredCalender.set(
                        dateCalender.get(Calendar.YEAR),
                        dateCalender.get(Calendar.MONTH),
                        dateCalender.get(Calendar.DATE),
                        timeCalender.get(Calendar.HOUR_OF_DAY),
                        timeCalender.get(Calendar.MINUTE),)
                    addGuestLectureRequest.lectureDate = outputFmt.format(requiredCalender.time)
                    Log.d(TAG, "checkAndSetLectureDateInApiFormat: ${outputFmt.format(requiredCalender.time)}")
                }catch (exception : ParseException){
                    exception.printStackTrace()
                }
            }
        }
    }

    private fun checkAndSetLectureDateInApiFormat1() {
        val requiredCalender = Calendar.getInstance()
        val dateCalender = Calendar.getInstance()
        val dateInputFormat: DateFormat = SimpleDateFormat("MM/dd/yy")
        val outputFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        outputFmt.timeZone = TimeZone.getTimeZone("GMT")
        addGuestLectureRequest.lectureDate?.let { lectureDate ->
                try{
                dateCalender.time = dateInputFormat.parse(lectureDate)!!
//                addGuestLectureRequest.lectureDate = dateCalender.convertDateAndTimeToApiFormat(timeCalender)
                requiredCalender.set(
                    dateCalender.get(Calendar.YEAR),
                    dateCalender.get(Calendar.MONTH),
                    dateCalender.get(Calendar.DATE))
                addGuestLectureRequest.lectureDate = outputFmt.format(requiredCalender.time)
                    Log.d(TAG, "checkAndSetLectureDateInApiFormat: ${outputFmt.format(requiredCalender.time)}")
                }catch (exception : ParseException){
                    exception.printStackTrace()
                }
        }
    }

    private fun checkAndSetLectureTimeInApiFormat() {
        val requiredCalender = Calendar.getInstance()
        val timeCalender = Calendar.getInstance()
        val timeInputFormat: DateFormat = SimpleDateFormat("hh:mm aa")
        val outputFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        addGuestLectureRequest.lectureDate?.let { lectureDate ->
            requiredCalender.time = outputFmt.parse(lectureDate)
            outputFmt.timeZone = TimeZone.getTimeZone("GMT")
            addGuestLectureRequest.startTime?.let { startTime ->
                timeCalender.time = timeInputFormat.parse(startTime)!!
                requiredCalender.set(Calendar.HOUR,timeCalender.get(Calendar.HOUR_OF_DAY))
                requiredCalender.set(Calendar.MINUTE,timeCalender.get(Calendar.MINUTE))
                addGuestLectureRequest.lectureDate = outputFmt.format(requiredCalender.time)
            }
        }
    }


    fun checkAndSetLectureEndDateInApiFormat(){
        val calendar = Calendar.getInstance()
        var dateString: String = ""
        endCalendar.value?.let { endCalendar ->
            endTimeCalendar.value?.let { endTimeCalendar ->
                calendar.set(
                    endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH),
                    endCalendar.get(Calendar.DATE),
                    endTimeCalendar.get(Calendar.HOUR_OF_DAY),
                    endTimeCalendar.get(Calendar.MINUTE),
                    0
                )
                val outputFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                outputFmt.timeZone = TimeZone.getTimeZone("GMT")
                val time = calendar.time
                dateString = outputFmt.format(time)
            }
        }

        addGuestLectureRequest.endDate = dateString

    }



    /*for the first time we are adding a guest lecture i.e, only details are sent to server*/
    fun addGuestLectureDetails() {
        Log.d(TAG, "addGuestLectureDetails: $addGuestLectureRequest")
        addGuestLectureDetailsApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addGuestLectureDetailsApiResponse.postValue(ApiResource.success(repository.addGuestLectureDetails(addGuestLectureRequest)))
            } catch (e: HttpException) {
                addGuestLectureDetailsApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                addGuestLectureDetailsApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
            }
        }
    }

    fun addGuestLectureImage(imageBase64: String) {
        setGuestLectureImageRequest(imageBase64, getIsLaunchedForEdit())
        addGuestLectureImageApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addGuestLectureImageApiResponse.postValue(ApiResource.success(repository.addGuestLectureImage(addGuestLectureImageRequest)))
            } catch (e: HttpException) {
                addGuestLectureImageApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                addGuestLectureImageApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
            }
        }
    }

    /*isForFirstTime - image request source id is addGuestLectureRes insertId,
    * if not for first time i.e, if for edit - then source id is addGuestLecture Id*/
    private fun setGuestLectureImageRequest(imageBase64: String, isForEdit: Boolean) {
        addGuestLectureImageRequest.code = if(getIsLaunchedForEdit()) addGuestLectureRequest.code!! else  addGuestLectureDetailsResData.code!!
        addGuestLectureImageRequest.sourceId = if (getIsLaunchedForEdit()) addGuestLectureRequest.id!! else addGuestLectureDetailsResData.insertId
        addGuestLectureImageRequest.sourceType = "Lecture"
        addGuestLectureImageRequest.baseImage = imageBase64
    }

    /*this function is called when we are updating the image and video for newly created guest lecture*/
    fun addGuestLectureVideo() {
        /* addGuestLectureVideoApiResponse.value = ApiResource.loading(data = null)
         viewModelScope.launch {
             try {
                 addGuestLectureVideoApiResponse.postValue(ApiResource.success(addGuestLectureRepository.addGuestLectureVideo(addGuestLectureRequest.id.toString(),addGuestLectureRequest)))
             } catch (e: HttpException) {
                 addGuestLectureVideoApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
             }
         }*/
    }

    fun saveGuestLecture() {
        if (getIsLaunchedForEdit()) {
            updateEditGuestLecture()
        } else {
            updateGuestLecture()
        }
    }

    fun updateGuestLecture() {
//        Log.d(TAG, "updateGuestLecture: $addGuestLectureRequest")
        updateGuestLectureApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            val id = if (getIsLaunchedForEdit()) addGuestLectureDetailsResData.id else addGuestLectureDetailsResData.insertId
            try {
                updateGuestLectureApiResponse.postValue(ApiResource.success(repository.updateGuestLecture(id.toString(), addGuestLectureRequest)))
            } catch (e: HttpException) {
                updateGuestLectureApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                updateGuestLectureApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !!"))
            }
        }
    }

    fun onImageUploaded(isSuccess: Boolean) {
        if (isSuccess) {
            setIsImageChangedAfterUpload(false)
            addGuestLectureRequest.image = if(!getIsLaunchedForEdit()) {
                addGuestLectureDetailsResData.code+PNG /*Image is added to Guest Lecture after successful upload of image*/
            } else {
                addGuestLectureRequest.code+PNG
            }
        }
    }

    fun onVideoUploaded(isSuccess: Boolean) {
        if (isSuccess) {
            addGuestLectureRequest.lectureVideo = if(!getIsLaunchedForEdit()) {
                addGuestLectureDetailsResData.code+MP4/*Video is added to Guest Lecture after successful upload of video*/
            } else {
                addGuestLectureRequest.code+MP4
            }
        }
    }

    private fun updateEditGuestLecture() {
        editGuestLectureApiResponse.value = ApiResource.loading(null)
        Log.d(TAG, "updateEditGuestLecture: ${addGuestLectureRequest}")
        val id = addGuestLectureRequest.id.toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                editGuestLectureApiResponse.postValue(ApiResource.success(repository.updateEditGuestLecture(id, addGuestLectureRequest)))
            } catch (e: HttpException) {
                editGuestLectureApiResponse.postValue(ApiResource.error(data = null, message = e.code().toString()))
            }catch (e: IOException) {
                editGuestLectureApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred !"))
            }
        }
    }

    fun updateLanguageSelection(selectedList: ArrayList<LanguageData>) {
        Log.d(TAG, "updateLanguageSelection")
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


    fun uploadVideo(file: File, trainerId: String, sourceId: String, code: String, videoType: String) {
        uploadVideoResponse.postValue(ApiResource.loading(null))
//        Log.d(AddCourseViewModel.TAG, "uploadVideo: $trainerId --> $sourceId --> $code --> $videoType -->")
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val trainerId = trainerId.toRequestBody("text/plain".toMediaType())
                val sourceId = sourceId.toRequestBody("text/plain".toMediaType())
                val code = code.toRequestBody("text/plain".toMediaType())
                val videoType = videoType.toRequestBody("text/plain".toMediaType())
                val file_size = (file.length() / 1024).toString().toInt()
                val requestFile: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)

                uploadVideoResponse.postValue(ApiResource.success(repository.uploadVideo(body, trainerId,sourceId, code, videoType)))
            } catch (e: HttpException) {
                uploadVideoResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
            }catch (e: IOException) {
                uploadVideoResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
            catch(e: Exception){
                uploadVideoResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

}