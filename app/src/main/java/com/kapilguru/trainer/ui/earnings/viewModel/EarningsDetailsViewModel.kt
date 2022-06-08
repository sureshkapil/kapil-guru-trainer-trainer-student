package com.kapilguru.trainer.ui.earnings.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsApiAllData
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsCourse
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsReferral
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsWebinar
import kotlinx.coroutines.Dispatchers

class EarningsDetailsViewModel(private val earningsDetailsRepository: EarningsDetailsRepository,
           application: Application):AndroidViewModel(application) {

    var trainerId: Int
    var earningsDetailsApiData: MutableLiveData<EarningsDetailsApiAllData>  = MutableLiveData()
    var earningsDetailsData: MutableLiveData<ArrayList<EarningsDetailsCourse>>  = MutableLiveData()
    var earningsDetailsReferrals: MutableLiveData<ArrayList<EarningsDetailsReferral>>  = MutableLiveData()
    var earningsDetailsWebinar: MutableLiveData<ArrayList<EarningsDetailsWebinar>>  = MutableLiveData()
    var jsonObjectString: MutableLiveData<String> = MutableLiveData()
    var studentId: MutableLiveData<String> = MutableLiveData()
    var studentName: MutableLiveData<String> = MutableLiveData()

    init
    {
        val pref = StorePreferences(application)
        trainerId = pref.trainerId
        earningsDetailsData.value = arrayListOf()
        earningsDetailsReferrals.value = arrayListOf()
    }
    var earningsDetailsListApi =  liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = earningsDetailsRepository.getearningDetailsList(trainerId.toString())))
        } catch (exception: Exception) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}