package com.kapilguru.trainer.ui.courses.view_course.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.ui.courses.view_course.BatchRequest
import com.kapilguru.trainer.ui.courses.view_course.ContactTrainerResponseAPi
import com.kapilguru.trainer.ui.courses.view_course.EnrolledCourseResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponse
import com.kapilguru.trainer.ui.courses.view_course.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CourseDetailsViewModel(private val courseDetailsRepository: CourseDetailsRepository) : ViewModel() {

    var courseId: MutableLiveData<String> = MutableLiveData()
    var course: MutableLiveData<Course> = MutableLiveData<Course>(Course())
    var batchesList: ArrayList<BatchesItem> = arrayListOf()
    private val TAG = "CourseDetailsViewModel"

    var writeReviewRequest: MutableLiveData<WriteReviewRequest> = MutableLiveData(WriteReviewRequest(0f, "", "", 0))
    var writeReviewResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()

    var resultDat: MutableLiveData<ApiResource<CourseDetailsResponse>> = MutableLiveData()
    var syllabusResponse: MutableLiveData<ApiResource<CourseSyllabusResponse>> = MutableLiveData()
    var enrolledCourseResponse: MutableLiveData<ApiResource<EnrolledCourseResponse>> = MutableLiveData()
    var isCourseEnrolled: MutableLiveData<Boolean> = MutableLiveData(false)
    var alreadyPurchasedData: List<EnrolledCourseResponseApi>? = null
    var requestBatchResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    var studentListReviewResponse: MutableLiveData<ApiResource<StudentReviewResponse>> = MutableLiveData()
    var studentListReviewResponseData: List<StudentReviewedData> = listOf()
    var rating: MutableLiveData<Float?> = MutableLiveData()
    var studentRatingCount: MutableLiveData<Int> = MutableLiveData(0)

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
        {
            t.printStackTrace()
        }
    }

    fun getCourseDetails(CourseId: String, userId: String) {
        resultDat.value = ApiResource.loading(data = null)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val courseDetails = async { courseDetailsRepository.fetchCourseDetails(CourseId) }
            val courseDetailsInfo: CourseDetailsResponse = courseDetails.await()
            try {
                resultDat.postValue(ApiResource.success(courseDetailsInfo))
            } catch (exception: IOException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = 401))
            } catch (exception: HttpException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code()))
            } catch (exception: Exception) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }

        }
    }


    fun getPdfAttachments(attachmentId: String) {
        syllabusResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                syllabusResponse.postValue(ApiResource.success(courseDetailsRepository.fetchSyllabusAttachments(attachmentId)))
            } catch (exception: IOException) {
                syllabusResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                syllabusResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                syllabusResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun batchRequest(batchRequest: BatchRequest) {
        requestBatchResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                requestBatchResponse.postValue(ApiResource.success(courseDetailsRepository.requestBatch(batchRequest)))
            } catch (exception: IOException) {
                requestBatchResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                requestBatchResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                requestBatchResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun getStudentsReviewsList(studentId: String) {
        studentListReviewResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentListReviewResponse.postValue(ApiResource.success(courseDetailsRepository.getStudentReviews(studentId)))
            } catch (exception: IOException) {
                studentListReviewResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                studentListReviewResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                studentListReviewResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

//    fun writeReviewUpdate(batchId: Int, studentId: String) {
//        writeReviewRequest.value?.apply {
//            this.batchId = batchId
//            this.sId = studentId
//        }
//        writeReviewResponse.value = ApiResource.loading(data = null)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                writeReviewResponse.postValue(ApiResource.success(courseDetailsRepository.updateStudentReview(writeReviewRequest.value!!)))
//            } catch (exception: IOException) {
//                writeReviewResponse.postValue(
//                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
//                )
//            } catch (exception: HttpException) {
//                writeReviewResponse.postValue(
//                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
//                )
//            } catch (exception: IOException) {
//                writeReviewResponse.postValue(
//                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
//                )
//            }
//        }
//    }


    var contactTrainerResponseAPi: MutableLiveData<ApiResource<ContactTrainerResponseAPi>> = MutableLiveData()


    fun contactTrainer(contactTrainerRequest: ContactTrainerRequest) {
        contactTrainerResponseAPi.value = ApiResource.loading(data = null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                contactTrainerResponseAPi.postValue(ApiResource.success(courseDetailsRepository.contactTrainer(contactTrainerRequest)))

            } catch (exception: IOException) {
                contactTrainerResponseAPi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                contactTrainerResponseAPi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                contactTrainerResponseAPi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun calculateRating(response: List<StudentReviewedData>?) {
        var calculateRating = 0.0
        var count = 0
        response?.let { data ->
            if (data.isNotEmpty()) {
                data.forEachIndexed { index, studentReviewedData ->
                    if (studentReviewedData.studentRating != -1.0) {
                        count += 1
                        calculateRating = studentReviewedData.studentRating
                    }
                }
                if (calculateRating > 0) {
                    rating.value = (calculateRating / count).toFloat()
                } else {
                    rating.value = 0.0f
                }
                studentRatingCount.value = count
            }
        } ?: run {
            rating.value = null
            studentRatingCount.value = 0
        }
    }

}


// Dont delete this as it may be used for future references
//    fun loginUser (loginRequest: LoginUserRequest) = liveData(Dispatchers.IO) {
//        Log.v("checkInfo", "checkhere_2");
//        emit(ApiResource.loading(data = null))
//        try {
//            Log.v("checkInfo", "checkhere");
//            emit(ApiResource.success(data = loginRepository.getUsers(loginRequest)))
//        } catch (exception: Exception) {
//            Log.v("checkInfo", "checkhere_22");
//            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
//        }
//    }

enum class InValidErrors {
    PASSWORDINCORRECT, EMAILINCORRECT
}
