package com.kapilguru.trainer.allSubscription.mySubscriptions.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kapilguru.trainer.allSubscription.mySubscriptions.MySubscriptionRepository
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.preferences.StorePreferences

class MySubscriptionViewModel(mySubscriptionRepository: MySubscriptionRepository, application: Application) :
    AndroidViewModel(application) {
    private val TAG = "MySubscriptionViewModel"
    val trainerId = StorePreferences(application).userId.toString()
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