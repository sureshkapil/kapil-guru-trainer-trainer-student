package com.kapilguru.trainer.allSubscription.mySubscriptions.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.kapilguru.trainer.allSubscription.mySubscriptions.MySubscriptionRepository
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MySubscriptionData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class MySubscriptionViewModel(mySubscriptionRepository: MySubscriptionRepository, application: Application) :
    AndroidViewModel(application) {
    private val TAG = "MySubscriptionViewModel"
    val trainerId = StorePreferences(application).trainerId.toString()
    var myPackageSubscriptionList : MutableLiveData<ArrayList<MyPackageData>> = MutableLiveData(ArrayList<MyPackageData>())
    var myPositionSubscriptionList : MutableLiveData<ArrayList<MyPositionData>> = MutableLiveData()
    var myBestTrainerSubscriptionList : MutableLiveData<ArrayList<MyBestTrainerData>> = MutableLiveData()

  /*  val mySubscriptionResponse = liveData(Dispatchers.IO){
        emit(ApiResource.loading(data = null))
        try{
            emit(ApiResource.success(data = mySubscriptionRepository.getMySubscriptions(trainerId)))
        }catch (exception: HttpException){
            exception.printStackTrace()
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }*/
}