package com.kapilguru.trainer.enquiries.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.enquiries.EnquiriesRepository
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResData
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResponse
import com.kapilguru.trainer.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class EnquiriesViewModel(private val repository: EnquiriesRepository, var contxt: Application) : AndroidViewModel(contxt) {
    private val TAG = "EnquiriesViewModel"
    val enquiriesListApiRes: MutableLiveData<ApiResource<EnquiriesResponse>> = MutableLiveData()
    val enquiriesList: MutableLiveData<ArrayList<EnquiriesResData>> = MutableLiveData()
    var kapilGuruEnquiries: MutableLiveData<ArrayList<EnquiriesResData>> = MutableLiveData()
    var offlineEnquiries: MutableLiveData<ArrayList<EnquiriesResData>> = MutableLiveData()
    val showLoadingIndicator: MutableLiveData<Boolean> = MutableLiveData()

    fun getEnquiriesList(trainerId: String) {
        enquiriesListApiRes.postValue(ApiResource.loading(data = null))
        try {
            viewModelScope.launch(Dispatchers.IO) {
                enquiriesListApiRes.postValue(ApiResource.success(data = repository.getEnquiries(trainerId)))
            }
        } catch (exception: IOException) {
            enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        } catch (exception: HttpException) {
            enquiriesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}