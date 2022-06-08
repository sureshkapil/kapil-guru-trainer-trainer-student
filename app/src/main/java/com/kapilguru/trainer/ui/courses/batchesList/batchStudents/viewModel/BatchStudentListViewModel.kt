package com.kapilguru.trainer.ui.courses.batchesList.batchStudents.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.BatchStudentsListRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class BatchStudentListViewModel (private val batchStudentsListRepository: BatchStudentsListRepository) : ViewModel() {

    var batchStudentListApi = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = batchStudentsListRepository.getBatchStudentList("31")))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}