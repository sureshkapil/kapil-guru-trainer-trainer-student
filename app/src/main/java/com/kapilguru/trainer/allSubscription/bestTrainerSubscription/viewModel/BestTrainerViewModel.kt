package com.kapilguru.trainer.allSubscription.bestTrainerSubscription.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.BestTrainerCourseRepository
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.CourseBestTrainerMapRequest
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.CourseBestTrainerMapResponce
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.HttpException
import java.io.IOException

class BestTrainerViewModel(val repository: BestTrainerCourseRepository, application: Application) :
    AndroidViewModel(application) {
    var trainerId : Int = StorePreferences(application).trainerId
    var myBestTrainerSubscList  = ArrayList<MyBestTrainerData>()
    var courseBestTrainerMapResponse : MutableLiveData<ApiResource<CourseBestTrainerMapResponce>> = MutableLiveData()
    lateinit var badgeData : AllSubscriptionsData

    val bestTrainerCourseList = liveData(Dispatchers.IO){
        emit(ApiResource.loading(data = null))
        try{
            emit(ApiResource.success(data = repository.getBestTrainerCourseList(trainerId.toString())))
        }catch (exception : Exception){
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception : IOException){
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun mapCourseBestTrainer(courseBestTrainerMapRequest: CourseBestTrainerMapRequest){
        courseBestTrainerMapResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                courseBestTrainerMapResponse.postValue(ApiResource.success(data = repository.mapCourseBestTrainer(courseBestTrainerMapRequest)))
            }catch (exception : HttpException){
                courseBestTrainerMapResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception : IOException){
                courseBestTrainerMapResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}