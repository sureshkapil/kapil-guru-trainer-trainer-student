package com.kapilguru.trainer.ui.courses.addcourse.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.ui.courses.addcourse.AddCourseRepository
import com.kapilguru.trainer.ui.courses.addcourse.models.*
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.http.Part
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class AddCourseViewModel(private val addCourseRepository: AddCourseRepository, application: Application) : AndroidViewModel(application) {

    var pdfName: MutableLiveData<String> = MutableLiveData()
    var isPdfSelected: Boolean = true
    var isPdfUploaded: Boolean = false
    val isEditImageNull: MutableLiveData<Boolean> = MutableLiveData(false)
    var isEdit: Boolean = false
    var addCourseRequest: AddCourseRequest = AddCourseRequest()
    var currentIndex: MutableLiveData<Int> = MutableLiveData(0)
    var addCourseResponse: MutableLiveData<ApiResource<AddCourseResponse>> = MutableLiveData()
    var getCourseRequest: MutableLiveData<ApiResource<GetCourseResponse>> = MutableLiveData()
    var updateCourseApi: MutableLiveData<ApiResource<UpdateCourseApi>> = MutableLiveData()
    var courseId: MutableLiveData<Int> = MutableLiveData()
    var selectedLanguagesString: MutableLiveData<String> = MutableLiveData("Select Language")
    var selectedLanguages: ArrayList<LanguageData> = ArrayList<LanguageData>()
    var selectedLanguagesIds: MutableLiveData<String> = MutableLiveData()
    var courseImageCode: MutableLiveData<String> = MutableLiveData()
    var uploadImageCourseResponse: MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()
    var imageUpdated: Boolean = false // if user chooses another image.
    var isVideoUpdated: Boolean = false // if user chooses another video.
    var uploadPdfResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()
    var uploadVideoResponse: MutableLiveData<ApiResource<UploadVideoResponse>> = MutableLiveData()
    var getPdfResponse: MutableLiveData<ApiResource<CoursePdfResponse>> = MutableLiveData()
    var downloadPdfUrl: MutableLiveData<String> = MutableLiveData()



    // updated api call variables
    var newisApiRequestMade: Boolean = false



    var categoryListApi = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = addCourseRepository.fetchCategory()))
        } catch (exception: Exception) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    var courseLanguages = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = addCourseRepository.getCourseLanguage()))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun saveAndPostTitleAndText() {
        Log.d(TAG, "saveAndPostTitleAndText: ${addCourseRequest.toString()}")
        addCourseResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
//                Log.d(TAG, "saveAndPostTitleAndText: ${addCourseRequest.toString()}")
                addCourseRequest.isApiRequestMade = true
                addCourseResponse.postValue(ApiResource.success(addCourseRepository.saveAndNext(addCourseRequest)))
            } catch (e: HttpException) {
                addCourseResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
            }catch (e: IOException) {
                addCourseResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateCourse() {
        updateCourseApi.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                Log.d(TAG, "updatedText: ${addCourseRequest.toString()} other_ino_id${courseId.value.toString()}")
                updateCourseApi.postValue(
                    ApiResource.success(
                        addCourseRepository.updateCourse(courseId.value.toString(), addCourseRequest)
                    )
                )
            } catch (e: HttpException) {
                updateCourseApi.postValue(
                    ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!")
                )
            }catch (e: IOException) {
                updateCourseApi.postValue(
                    ApiResource.error(data = null, message = e.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun uploadCourseImage(uploadImageCourse: UploadImageCourse) {
        uploadImageCourseResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                Log.d(TAG, "updatedText: ${addCourseRequest.toString()} other_ino_id${courseId.value.toString()}")
                uploadImageCourseResponse.postValue(
                    ApiResource.success(
                        addCourseRepository.uploadCourseImage(uploadImageCourse)
                    )
                )
            } catch (e: HttpException) {
                uploadImageCourseResponse.postValue(
                    ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!")
                )
            }catch (e: IOException) {
                uploadImageCourseResponse.postValue(
                    ApiResource.error(data = null, message = e.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun fetchCourseDetails() {
        getCourseRequest.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                Log.d(TAG, "updatedText: ${addCourseRequest.toString()} other_ino_id${courseId.value.toString()}")
                getCourseRequest.postValue(
                    ApiResource.success(
                        addCourseRepository.getCourseDetails(courseId.value.toString())
                    )
                )
            } catch (e: HttpException) {
                updateCourseApi.postValue(
                    ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!")
                )
            }catch (e: IOException) {
                updateCourseApi.postValue(
                    ApiResource.error(data = null, message = e.message ?: "Error Occurred!")
                )
            }
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

    fun uploadPDF(file: File, trainerId: String, courseId: String, pdfName: String) {
        uploadPdfResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trainerId = trainerId.toRequestBody("text/plain".toMediaType())
                val courseId = courseId.toRequestBody("text/plain".toMediaType())
                val pdfName = pdfName.toRequestBody("text/plain".toMediaType())
                val file_size = (file.length() / 1024).toString().toInt()
                val requestFile: RequestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)

                uploadPdfResponse.postValue(ApiResource.success(addCourseRepository.uploadCoursePdfFile(body, trainerId, courseId, pdfName)))
            } catch (e: HttpException) {
                uploadPdfResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
            }catch (e: IOException) {
                uploadPdfResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun uploadVideo(file: File, trainerId: String, sourceId: String, code: String, videoType: String) {
        uploadVideoResponse.postValue(ApiResource.loading(null))
        Log.d(TAG, "uploadVideo: $trainerId --> $sourceId --> $code --> $videoType -->")
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val trainerId = trainerId.toRequestBody("text/plain".toMediaType())
                val sourceId = sourceId.toRequestBody("text/plain".toMediaType())
                val code = code.toRequestBody("text/plain".toMediaType())
                val videoType = videoType.toRequestBody("text/plain".toMediaType())
                val file_size = (file.length() / 1024).toString().toInt()
                val requestFile: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)


                uploadVideoResponse.postValue(ApiResource.success(addCourseRepository.uploadVideo(body, trainerId,sourceId, code, videoType)))
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

    fun getPDF(pdfId: String) {
        getPdfResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch {
            try {
                getPdfResponse.postValue(
                    ApiResource.success(
                        data = addCourseRepository.getCoursePdfFile(pdfId)
                    )
                )
            } catch (e: HttpException) {
                getPdfResponse.postValue(
                    ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!")
                )
            }catch (e: IOException) {
                getPdfResponse.postValue(
                    ApiResource.error(data = null, message = e.message ?: "Error Occurred!")
                )
            }
        }

    }


    companion object {
        private val TAG = "uploadVideo"

      /*  @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun TextInputEditText.putCourseTitle(): AddCourseRequest {
//            val data = this.text.toString()
            return AddCourseRequest(courseTitle = this.text.toString())
        }*/

        @JvmStatic
        @BindingAdapter("android:text")
        fun getCourseTitle(editText: TextInputEditText, addCourseRequest: AddCourseRequest?) {
            addCourseRequest?.let {
                editText.setText(addCourseRequest.courseTitle.toString())
            }
        }

    }

}
