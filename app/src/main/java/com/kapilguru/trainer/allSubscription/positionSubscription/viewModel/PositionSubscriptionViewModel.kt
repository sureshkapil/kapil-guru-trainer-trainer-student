package com.kapilguru.trainer.allSubscription.positionSubscription.viewModel

import android.app.Application
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.MyApplication
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.positionSubscription.PositionSubscriptionRepository
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapRequest
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapResponse
import com.kapilguru.trainer.allSubscription.positionSubscription.model.TrainerCourseData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.add_batch.viewModel.AddBatchViewModel.bindingAdapterDays.setCustomBackGround
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.HttpException
import java.io.IOException

class PositionSubscriptionViewModel(val repository : PositionSubscriptionRepository,application: Application) : AndroidViewModel(application) {
    private val TAG = "PosSubscripViewModel"
    var trainerId : String = StorePreferences(application).trainerId.toString()
    lateinit var allSubscriptionsData : AllSubscriptionsData
    val positionSubscriptionList = ArrayList<TrainerCourseData>()
    var myPositionSubscriptionList = ArrayList<MyPositionData>()
    var coursePositionMapResponse : MutableLiveData<ApiResource<CoursePositionMapResponse>> = MutableLiveData()

    val positionSubscriptionListResponse = liveData(Dispatchers.IO){
        emit(ApiResource.loading(data = null))
        try{
            emit(ApiResource.success(data = repository.getPositionSubscriptionList(trainerId)))
        }catch (exception : HttpException){
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception : IOException){
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun mapCoursePosition(coursePositionMapRequest : CoursePositionMapRequest){
        coursePositionMapResponse.value = ApiResource.loading(data =null)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                coursePositionMapResponse.postValue(ApiResource.success(data = repository.mapCoursePosition(coursePositionMapRequest)))
            }catch (exception : Exception){
                coursePositionMapResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    companion object bindingAdapterPositions{
        @JvmStatic
        @BindingAdapter(value=["courseData","position"],requireAll = true)
        fun AppCompatTextView.setPositionBackground(trainerCourseData : TrainerCourseData,positionNo :Int){
           /* if(trainerCourseData.isOwned){
                if(positionNo == trainerCourseData.ownedPosition){
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.selected_days_bg)
                }else{
                    this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_days_bg)
                }
            }else{
                if(trainerCourseData.id == 103){
                    Log.d("#######","trainercourseData : "+trainerCourseData)
                }
                when(positionNo){
                    1->{
                        if(trainerCourseData.isPosition_1_Occupied){
                            if(trainerCourseData.id == 103){
                                Log.d("######","position 1 ocupied")
                            }
                            this.background = ContextCompat.getDrawable(this.context, R.drawable.selected_days_bg)
                        }else{
                            if(trainerCourseData.id == 103){
                                Log.d("######","position 1  not ocupied")
                            }
                            this.background = null

                        }
                    }
                    2->{
                        if(trainerCourseData.isPosition_2_Occupied){
                            this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_days_bg)
                        }else{
                            this.background = null

                        }
                    }
                    3->{
                        if(trainerCourseData.isPosition_3_Occupied){
                            this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_days_bg)
                        }else{
                            this.background = null

                        }
                    }
                    4->{
                        if(trainerCourseData.isPosition_4_Occupied){
                            this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_days_bg)
                        }else{
                            this.background = null

                        }
                    }
                    5->{
                        if(trainerCourseData.isPosition_5_Occupied){
                            this.background = ContextCompat.getDrawable(this.context, R.drawable.unselected_days_bg)
                        }else{
                            this.background = null

                        }

                    } else  -> {
                    Log.d("###########", "setPositionBackground: check")
                    }
                }
            }*/
        }
    }
}