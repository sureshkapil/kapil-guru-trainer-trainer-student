package com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.BatchSyllabusRepository
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.models.BatchSyllabusAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BatchSyllabusViewModel(val batchSyllabusRepository: BatchSyllabusRepository) : ViewModel() {

    companion object {
        private const val TAG = "BatchSyllabusViewModel"
    }

    var resultDat: MutableLiveData<ApiResource<BatchSyllabusAPI>> = MutableLiveData()


    fun fetchSyllabusInfoAPI(courseId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(
                    ApiResource.success(
                        batchSyllabusRepository.getSyllabusInfo(
                            courseId
                        )
                    )
                )
            } catch (exception: HttpException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }

        }
    }

}