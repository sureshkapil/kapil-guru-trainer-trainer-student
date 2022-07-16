package com.kapilguru.trainer.feeManagement

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.kapilguru.trainer.feeManagement.addFeeManagement.AddFeeManagementResponse
import com.kapilguru.trainer.feeManagement.addFeeManagement.AddFeeManagementRequest
import com.kapilguru.trainer.feeManagement.addFeeManagement.InstallmentsListResponse
import com.kapilguru.trainer.feeManagement.feeFollowUps.FeeFollowUpResponse
import com.kapilguru.trainer.feeManagement.feeFollowUps.FeeFollowUpResponseApi
import com.kapilguru.trainer.feeManagement.paidRecords.StudentFeePaidResponse
import com.kapilguru.trainer.feeManagement.studentFeeRecords.StudentFeeRecordsResponse
import com.kapilguru.trainer.isDateToday
import com.kapilguru.trainer.isValidMobileNo
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.studentsList.model.AllStudentsListPerTrainerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FeeManagementViewModel(private val repository: FeeManagementRepository, application: Application) : ViewModel() {

    var resultDat: MutableLiveData<ApiResource<AllStudentsListPerTrainerApi>> = MutableLiveData()

    var totalAmount: String? = null
    var paidAmount: String? = null
    var dueAmount: MutableLiveData<String> = MutableLiveData()


    var trainerId: Int = 0
    var tenantId: Int = 0

    var studentFeeRecordsResponse: MutableLiveData<ApiResource<StudentFeeRecordsResponse>> = MutableLiveData()

    var studentFeePaidResponse: MutableLiveData<ApiResource<StudentFeePaidResponse>> = MutableLiveData()

    var feeFollowUpResponse: MutableLiveData<ApiResource<FeeFollowUpResponse>> = MutableLiveData()

    var upComingFeeFollowUpResponse: MutableLiveData<List<FeeFollowUpResponseApi>> = MutableLiveData()

    var todaysFeeFollowUpResponse: MutableLiveData<List<FeeFollowUpResponseApi>> = MutableLiveData()

    var addFeeManagementRequest: MutableLiveData<AddFeeManagementRequest> = MutableLiveData(AddFeeManagementRequest())

    var addFeeDetailsResponse: MutableLiveData<ApiResource<AddFeeManagementResponse>> = MutableLiveData()

    var errorMessage: MutableLiveData<String> = MutableLiveData()
    var installmentListResponse: MutableLiveData<ApiResource<InstallmentsListResponse>> = MutableLiveData()

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
        tenantId = pref.tenantId
    }

    fun getStudentFeeRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            studentFeeRecordsResponse.postValue(ApiResource.loading(data = null))
            try {
                studentFeeRecordsResponse.postValue(ApiResource.success(data = repository.getStudentFeeRecords(trainerId.toString())))
            } catch (exception: HttpException) {
                studentFeeRecordsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                studentFeeRecordsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getStudentPaidRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            studentFeePaidResponse.postValue(ApiResource.loading(data = null))
            try {
                studentFeePaidResponse.postValue(ApiResource.success(data = repository.getStudentPaidRecords(trainerId.toString())))
            } catch (exception: HttpException) {
                studentFeePaidResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                studentFeePaidResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getStudentFeeFollowUps() {
        viewModelScope.launch(Dispatchers.IO) {
            studentFeePaidResponse.postValue(ApiResource.loading(data = null))
            try {
                feeFollowUpResponse.postValue(ApiResource.success(data = repository.getStudentFeeFollowUps(trainerId.toString())))
            } catch (exception: HttpException) {
                feeFollowUpResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                feeFollowUpResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun setToDayUPComingFeeFollowUps(info: List<FeeFollowUpResponseApi>) {

        val differntiatedInfo = info.partition { it ->
            it.dueDate.isDateToday()
        }
        todaysFeeFollowUpResponse.value = differntiatedInfo.first
        upComingFeeFollowUpResponse.value = differntiatedInfo.second

    }


    fun calculateDueAmount() {
        val totalAmount = this.totalAmount ?: return
        val paidAmount = this.paidAmount ?: return
        this.dueAmount.value = (totalAmount.toDouble() - paidAmount.toDouble()).toString()
        addFeeManagementRequest.value?.dueFee = this.dueAmount.value.toString().toDouble()
        addFeeManagementRequest.value?.totalFee = this.totalAmount
        addFeeManagementRequest.value?.paidFee = this.paidAmount
    }


    fun validateUserInfo(): Boolean {
        var isValid = true
        when {
            addFeeManagementRequest.value?.name.isNullOrEmpty() -> {
                errorMessage.value = "Please enter Name"
                isValid = false
            }
            addFeeManagementRequest.value?.name.toString().trim().length < 4 -> {
                errorMessage.value = "Student Name should contain at least of 4 characters"
                isValid = false
            }
            addFeeManagementRequest.value?.contactNumber.isNullOrEmpty() -> {
                errorMessage.value = "Please enter Contact Number"
                isValid = false
            }
            !addFeeManagementRequest.value?.contactNumber.toString().trim().isValidMobileNo() -> {
                errorMessage.value = "Please enter Valid Number"
                isValid = false
            }
            addFeeManagementRequest.value?.dateOfJoining.isNullOrEmpty() -> {
                errorMessage.value = "Please enter Joining Date"
                isValid = false
            }
            addFeeManagementRequest.value?.course.isNullOrEmpty() -> {
                errorMessage.value = "Please enter Course Name"
                isValid = false
            }
            totalAmount.isNullOrEmpty() -> {
                errorMessage.value = "Please enter total Amount"
                isValid = false
            }
            paidAmount.isNullOrEmpty() -> {
                errorMessage.value = "Please enter total Amount"
                isValid = false
            }
            paidAmount!!.toDouble() > totalAmount!!.toDouble() -> {
                errorMessage.value = "Please Amount can't be grater than total Amount"
                isValid = false
            }
            /* addFeeManagementRequest.value?.dueDate.toString().trim().isEmpty() -> {
                 errorMessage.value = "Please enter Due Date"
                 isValid = false
             }*/
        }
        return isValid
    }

    fun addFeeDetails() {
        addFeeManagementRequest.value?.tenantId = tenantId
        addFeeManagementRequest.value?.trainerId = trainerId
        viewModelScope.launch(Dispatchers.IO) {
            addFeeDetailsResponse.postValue(ApiResource.loading(data = null))
            try {
                addFeeDetailsResponse.postValue(ApiResource.success(data = repository.addFeeDetailsRequest(addFeeManagementRequest.value!!)))
            } catch (exception: HttpException) {
                addFeeDetailsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                addFeeDetailsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getInstallments(insertId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            installmentListResponse.postValue(ApiResource.loading(data = null))
            try {
                installmentListResponse.postValue(ApiResource.success(data = repository.getInstallmentList(insertId)))
            } catch (exception: HttpException) {
                installmentListResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                installmentListResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    companion object {
         @JvmStatic
         @BindingAdapter("textInputDoubleToString")
         fun TextInputEditText.doubleToString(data: Double?) {
             data?.let {
                 this.setText(it.toString())
             }

         }

         @JvmStatic
         @InverseBindingAdapter(attribute = "textInputDoubleToString", event = "android:textAttrChanged")
         fun StringToDouble(data: TextInputEditText): Double {
             return if(!data.text.isNullOrBlank()) {
                 data.text.toString().toDouble()
             } else {
                 0.0
             }
         }

        /* @JvmStatic
         @BindingAdapter("textInputIntToString")
         fun TextInputEditText.InttoString(data: Int) {
             this.setText(data.toString())
         }

         @JvmStatic
         @InverseBindingAdapter(attribute = "textInputIntToString", event = "android:textAttrChanged")
         fun StringToInt(data: TextInputEditText): Int {
             return data.text.toString().toInt()
         }*/

    }

}