package com.kapilguru.trainer.feeManagement

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.feeManagement.paidRecords.StudentFeePaidResponse
import com.kapilguru.trainer.feeManagement.studentFeeRecords.StudentFeeRecordsResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.studentsList.model.AllStudentsListPerTrainerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FeeManagementViewModel(private val repository: FeeManagementRepository, application: Application) : ViewModel() {

    var resultDat: MutableLiveData<ApiResource<AllStudentsListPerTrainerApi>> = MutableLiveData()
    var totalAmount : MutableLiveData<Double> = MutableLiveData(0.0)

    var dueAmount : MutableLiveData<Double> = MutableLiveData(100.0)

    var numberOfInstallments : MutableLiveData<Int> = MutableLiveData(5)


    var trainerId: Int =0
    var tenantId: Int=0

    var studentFeeRecordsResponse: MutableLiveData<ApiResource<StudentFeeRecordsResponse>> = MutableLiveData()

    var studentFeePaidResponse: MutableLiveData<ApiResource<StudentFeePaidResponse>> = MutableLiveData()


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

}