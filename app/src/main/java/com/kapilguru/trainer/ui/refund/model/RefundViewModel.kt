package com.kapilguru.trainer.ui.refund.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers

class RefundViewModel(private val refundRepository: RefundRepository, application: Application): AndroidViewModel(application) {

    var trainerId: Int

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
    }

    var refundListApi =  liveData(Dispatchers.IO) {
        Log.v("checkInfo", "checkhere_2");
        emit(ApiResource.loading(data = null))
        try {
            Log.v("checkInfo", "checkhere");
            emit(ApiResource.success(data = refundRepository.getRefundList(trainerId.toString())))
        } catch (exception: Exception) {
            Log.v("checkInfo", "checkhere_22");
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}