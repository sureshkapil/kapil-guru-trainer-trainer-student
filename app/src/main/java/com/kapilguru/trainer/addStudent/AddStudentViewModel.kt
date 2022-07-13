package com.kapilguru.trainer.addStudent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.addStudent.AddofflineStudent.AddOfflineStudentRequest
import com.kapilguru.trainer.addStudent.AddofflineStudent.AddOfflineStudentResponse
import com.kapilguru.trainer.addStudent.addOnlineStudent.OnlineStudentRequest
import com.kapilguru.trainer.addStudent.coursesStudentList.MyCourseStudents
import com.kapilguru.trainer.addStudent.offlineStudentList.OfflineStudentsListResponse
import com.kapilguru.trainer.addStudent.signedUpStudentList.SignedUpStudentsListResponse
import com.kapilguru.trainer.addStudent.studyMaterialStudentsList.MyStudentsRecordedStudyMaterialsResponse
import com.kapilguru.trainer.emailValidation
import com.kapilguru.trainer.generateUuid
import com.kapilguru.trainer.isValidMobileNo
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApi
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseApi
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddStudentViewModel(
    private val repository: AddStudentRepository, application: Application
) : AndroidViewModel(application) {

    var checkStudentResponse: MutableLiveData<ApiResource<CheckStudentResponse>> = MutableLiveData()
    var checkStudentRequest: MutableLiveData<CheckStudentRequest> = MutableLiveData(CheckStudentRequest())
    var name : MutableLiveData<String> = MutableLiveData("")

    var errorText: MutableLiveData<String> = MutableLiveData("")

    var studentId: MutableLiveData<Int> = MutableLiveData(0)

    // Course Info
    var courseList: MutableLiveData<ApiResource<CourseApi>> = MutableLiveData()
    var courseListApi = arrayListOf<CourseResponse>()
    var courseId :Int =0


    // Batch Info
    var batchList: MutableLiveData<ApiResource<BatchListApi>> = MutableLiveData()
    var batchListApi = arrayListOf<BatchListResponse>()
    var batchId :Int =0


    var onlineStudentRequest: MutableLiveData<OnlineStudentRequest> = MutableLiveData()
    var addOnlineStudentResponse: MutableLiveData<ApiResource<AddOnlineStudentResponse>> = MutableLiveData()

    var trainerId: Int =0
    var tenantId: Int=0

    // add offline Student Request
    var offlineStudentRequest: MutableLiveData<AddOfflineStudentRequest> = MutableLiveData(AddOfflineStudentRequest())
    var offlineStudentStartDate: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())
    var offlineStudentStartTime: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())
    var startDate: MutableLiveData<String> = MutableLiveData("")
    var startTime: MutableLiveData<String> = MutableLiveData("")
    var addOfflineStudentResponse: MutableLiveData<ApiResource<AddOfflineStudentResponse>> = MutableLiveData()
    var errorOfflineText: MutableLiveData<String> = MutableLiveData("")


    // MyCourseStudents
    var myCourseStudents: MutableLiveData<ApiResource<MyCourseStudents>> = MutableLiveData()

    // My RecordedStudents
    var myRecordedStudents: MutableLiveData<ApiResource<MyStudentsRecordedStudyMaterialsResponse>> = MutableLiveData()

    // My Offline students
    var offlineStudentsListResponse: MutableLiveData<ApiResource<OfflineStudentsListResponse>> = MutableLiveData()


    // SignedUpStudents
    var signedUpStudentsResponse: MutableLiveData<ApiResource<SignedUpStudentsListResponse>> = MutableLiveData()

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
        tenantId = pref.tenantId
    }

    fun checkStudent() {
        checkStudentResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkStudentResponse.postValue(ApiResource.success(data = repository.checkStudent(checkStudentRequest.value!!)))
            } catch (exception: HttpException) {
                checkStudentResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                checkStudentResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }


    fun getCourseList() {
        viewModelScope.launch(Dispatchers.IO) {
            courseList.postValue(ApiResource.loading(data = null))
            try {
                courseList.postValue(ApiResource.success(data = repository.getCousesList(trainerId.toString())))
            } catch (exception: HttpException) {
                courseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                courseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getBatchList(courseid: String) {
        val batchListRequest = BatchListApiRequest(trainerId, courseid)
        viewModelScope.launch (Dispatchers.IO) {
            batchList.postValue(ApiResource.loading(data = null))
            try {
                batchList.postValue(ApiResource.success(data = repository.getBatchList(batchListRequest)))
            } catch (exception: HttpException) {
                batchList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                batchList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun addOnlineStudent() {
        val onlineStudentRequest: OnlineStudentRequest = getOnlineStudentRequestInfo()
        viewModelScope.launch (Dispatchers.IO) {
            addOnlineStudentResponse.postValue(ApiResource.loading(data = null))
            try {
                addOnlineStudentResponse.postValue(ApiResource.success(data = repository.addOnlineStudent(onlineStudentRequest)))
            } catch (exception: HttpException) {
                addOnlineStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                addOnlineStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun validateUserData(): Boolean {
        var shouldAllow: Boolean = true
        when {
            checkStudentRequest.value?.emailId!!.isEmpty() -> {
                errorText.value = "Please Enter Email Id"
                shouldAllow = false
            }
            checkStudentRequest.value?.emailId!!.emailValidation() -> {
                errorText.value = "Please Enter valid Email Id"
                shouldAllow = false
            }
            checkStudentRequest.value?.contactNumber!!.isEmpty() -> {
                errorText.value = "Please Phone Number"
                shouldAllow = false
            }
            checkStudentRequest.value?.contactNumber!!.isValidMobileNo() -> {
                errorText.value = "Please Enter valid phone Number"
                shouldAllow = false
            }
            courseId == 0 -> {
                errorText.value = "Please Select Course"
                shouldAllow = false
            }
        }
        return shouldAllow
    }

    private fun getOnlineStudentRequestInfo(): OnlineStudentRequest {
        return OnlineStudentRequest(
            emailId = checkStudentRequest.value?.emailId,
            tenantId =tenantId,
            countryCode = "91",
            courseId = courseId.toString(),
            batchId = batchId,
            name = name.value,
            studentId = studentId.value,
            isOtherCountryCode = 0,
            uuid= generateUuid().toString(),
            contactNumber = checkStudentRequest.value?.contactNumber,
            isAddedByTrainer = 1,
            trainerId = trainerId
        )
    }

    fun validateOfflineAddStudentUserData(): Boolean {
        var shouldAllow: Boolean = true
        when {
            offlineStudentRequest.value?.name!!.trim().isEmpty() -> {
                errorOfflineText.value = "Please Enter Email Id"
                shouldAllow = false
            }
            offlineStudentRequest.value?.emailId!!.emailValidation() -> {
                errorOfflineText.value = "Please Enter valid Email Id"
                shouldAllow = false
            }
            offlineStudentRequest.value?.contactNumber!!.isEmpty() -> {
                errorOfflineText.value = "Please Phone Number"
                shouldAllow = false
            }
            offlineStudentRequest.value?.contactNumber!!.length<10 -> {
                errorOfflineText.value = "Please Enter valid phone Number"
                shouldAllow = false
            }
            offlineStudentRequest.value?.course!!.trim().isEmpty() -> {
                errorOfflineText.value = "Please Enter course Name"
                shouldAllow = false
            }
            startDate.value!!.toString().trim().isEmpty() -> {
                errorOfflineText.value = "Please Add Batch Start Date"
                shouldAllow = false
            }
            startTime.value!!.toString().trim().isEmpty() -> {
                errorOfflineText.value = "Please Add Batch Start Time"
                shouldAllow = false
            }
            offlineStudentRequest.value?.fee!!.isEmpty() -> {
                errorOfflineText.value = "Please Add Fee Details"
                shouldAllow = false
            }
            offlineStudentRequest.value?.daysAttended!!.toString().trim().isEmpty() -> {
                errorOfflineText.value = "Please add days Attended Details"
                shouldAllow = false
            }
            offlineStudentRequest.value?.daysConducted!!.toString().trim().isEmpty() -> {
                errorOfflineText.value = "Please Add Days Conducted"
                shouldAllow = false
            }
        }
        return shouldAllow
    }


    fun addOfflineStudent() {
        viewModelScope.launch (Dispatchers.IO) {
            addOfflineStudentResponse.postValue(ApiResource.loading(data = null))
            try {
                addOfflineStudentResponse.postValue(ApiResource.success(data = repository.addOfflineStudent(offlineStudentRequest.value!!)))
            } catch (exception: HttpException) {
                addOfflineStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                addOfflineStudentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun setBatchStartDate() {
        offlineStudentRequest.value!!.batchDate = convertStartDate()
    }

    fun convertStartDate(): String {
        val calendar = Calendar.getInstance()
        var dateString: String = ""
        offlineStudentStartDate.value?.let { startCalendar ->
            offlineStudentStartTime.value?.let { startTimeCalendar ->
                calendar.set(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DATE),
                    startTimeCalendar.get(Calendar.HOUR_OF_DAY),
                    startTimeCalendar.get(Calendar.MINUTE),
                    0
                )
                val outputFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                outputFmt.timeZone = TimeZone.getTimeZone("GMT")
//                calendar.timeZone = TimeZone.getTimeZone("UTC")
                val time = calendar.time
                dateString = outputFmt.format(time)
            }
        }
        return dateString
    }


    fun getMyCourseStudents() {
        viewModelScope.launch (Dispatchers.IO) {
            myCourseStudents.postValue(ApiResource.loading(data = null))
            try {
                myCourseStudents.postValue(ApiResource.success(data = repository.getMyCourseStudents(trainerId.toString())))
            } catch (exception: HttpException) {
                myCourseStudents.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                myCourseStudents.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getMyRecordedStudents() {
        viewModelScope.launch (Dispatchers.IO) {
            myRecordedStudents.postValue(ApiResource.loading(data = null))
            try {
                myRecordedStudents.postValue(ApiResource.success(data = repository.getMyRecordedStudents(trainerId.toString())))
            } catch (exception: HttpException) {
                myRecordedStudents.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                myRecordedStudents.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun geOfflineStudents() {
        viewModelScope.launch (Dispatchers.IO) {
            myRecordedStudents.postValue(ApiResource.loading(data = null))
            try {
                offlineStudentsListResponse.postValue(ApiResource.success(data = repository.geOfflineStudents(trainerId.toString())))
            } catch (exception: HttpException) {
                offlineStudentsListResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                offlineStudentsListResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getSignedUpStudentsList() {
        viewModelScope.launch (Dispatchers.IO) {
            signedUpStudentsResponse.postValue(ApiResource.loading(data = null))
            try {
                signedUpStudentsResponse.postValue(ApiResource.success(data = repository.getSignedUpStudentsList(trainerId.toString())))
            } catch (exception: HttpException) {
                signedUpStudentsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                signedUpStudentsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

}