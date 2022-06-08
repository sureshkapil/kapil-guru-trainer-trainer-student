package com.kapilguru.trainer.batchExamReports

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SelectedBatchExamReportsViewModel(val selectedBatchExamReportsRepository: SelectedBatchExamReportsRepository) : ViewModel() {
    

    var batchExamReportModel: MutableLiveData<BatchExamResultModel> = MutableLiveData()
    var batchExamStudents: MutableLiveData<List<BatchStudentsItem>> = MutableLiveData()
    var batchStudentReportApi : MutableLiveData<ApiResource<BatchStudentReportApi>> = MutableLiveData()
    var courseTitle : MutableLiveData<String> = MutableLiveData()


    fun getBatchReports(batchReportsRequestModel: BatchReportsRequestModel) {
        batchStudentReportApi.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchStudentReportApi.postValue(ApiResource.success(selectedBatchExamReportsRepository.batchStudentReport(batchReportsRequestModel)))
            } catch (exception: IOException) {
                batchStudentReportApi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                batchStudentReportApi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


}