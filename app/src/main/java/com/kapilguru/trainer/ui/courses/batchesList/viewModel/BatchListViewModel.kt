package com.kapilguru.trainer.ui.courses.batchesList.viewModel

import android.app.Application
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.R
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.batchesList.BatchListRepository
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BatchListViewModel(
    private val batchListRepository: BatchListRepository,
    application: Application,
    courseid: String,

    ) : AndroidViewModel(application) {

    var resultOfBatchListApi: MutableLiveData<ApiResource<BatchListApiRequest>> = MutableLiveData()
    var trainerId: Int
    var totalBatches: MutableLiveData<String> = MutableLiveData("0")
    var deleteBatchResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
    }

    val batchListRequest = BatchListApiRequest(trainerId, courseid)


    var batchListApi = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = batchListRepository.getBatchList(batchListRequest)))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteBatch(batchId: String) {
        deleteBatchResponse.postValue(ApiResource.loading(data = null))
        try {

            viewModelScope.launch(Dispatchers.IO) {
                deleteBatchResponse.postValue(
                    ApiResource.success(data = batchListRepository.deleteBatch(batchId))
                )
            }
        } catch (e: Exception){
            deleteBatchResponse.postValue(
                ApiResource.error(data = null, message = "Could not Connect, try after sometime")
            )
        }
    }


    companion object {
        @JvmStatic
        @BindingAdapter("classAvailable")
        fun AppCompatTextView.setCustomBackGround(boolean: Int) {
            if (boolean == 1) {
                this.setTextColor(ContextCompat.getColor(context, R.color.blue_2))
            } else {
                this.setTextColor(ContextCompat.getColor(context, R.color.grey_2))
                this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_day_bg_new)
            }
        }
    }
}