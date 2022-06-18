package com.kapilguru.trainer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.R
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest
import com.kapilguru.trainer.ui.changePassword.model.LogoutResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeScreenViewModel(private val homeRepository: HomeScreenRepository, val context: Application) : AndroidViewModel(context) {

    val listOfHomeTopItems: MutableLiveData<MutableList<HomeViewPagerItem>> = MutableLiveData()
    val listOfHomeItems: MutableLiveData<MutableList<HomeItem>> = MutableLiveData()
    val upcomingResponse: MutableLiveData<ApiResource<UpComingScheduleResponse>> = MutableLiveData()
    val logoutResponse :MutableLiveData<ApiResource<LogoutResponse>> = MutableLiveData()
    val notificationCountResponse : MutableLiveData<ApiResource<NotificationCountResponse>> = MutableLiveData()
    val notificationCount: MutableLiveData<String> = MutableLiveData("")

    fun setHomeTopItems() {
        val homeViewPagerItem = mutableListOf<HomeViewPagerItem>()
        homeViewPagerItem.add(HomeViewPagerItem().apply {
            image = R.drawable.ic_live
            upperText = "ADD"
            title = "Live Courses"
            backgroundcolor = R.color.home_top_bg1
        })
        homeViewPagerItem.add(HomeViewPagerItem().apply {
            image = R.drawable.ic_recorded_classes
            upperText = "ADD"
            title = "Recorded Courses"
            backgroundcolor = R.color.home_top_bg2
        })
        homeViewPagerItem.add(HomeViewPagerItem().apply {
            image = R.drawable.ic_study_metrial
            upperText = "ADD"
            title = "Study Materials"
            backgroundcolor = R.color.home_top_bg3
        })

        homeViewPagerItem.add(HomeViewPagerItem().apply {
            image = R.drawable.ic_free_lecture
            upperText = "Schedule"
            title = "Free Classes"
            backgroundcolor = R.color.home_top_bg3
        })
        listOfHomeTopItems.postValue(homeViewPagerItem)
    }

    fun setHomeItems() {
        val homeItems = mutableListOf<HomeItem>()

        homeItems.add(HomeItem().apply {
            image = R.drawable.student_ic_course
            title = "Live Courses"
        })

        homeItems.add(HomeItem().apply {
            image = R.drawable.student_ic_demo_lectures
            title = "Recorded Courses"
        })

        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_my_course
            title = "Study materials"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_subscriptions
            title = "Free Lectures"
        })

        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_demo_lectures
            title = "Todays Schedules"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_exams
            title = "Exams"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_earnings
            title = "Enquiries"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_my_students
            title = "My Students"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.student_ic_messages
            title = "Messsages"
        })

        listOfHomeItems.postValue(homeItems)
    }

    fun fetchUpcomingSchedule() {
        val trainerId = StorePreferences(context).userId
        upcomingResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                upcomingResponse.postValue(
                    ApiResource.success(
                        homeRepository.fetchUpcomingSchedule(trainerId = trainerId.toString())
                    )
                )
            } catch (exception: HttpException) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun logoutUser(logoutRequest: LogoutRequest){
        logoutResponse.value = ApiResource.loading(data = null)
        try{
            viewModelScope.launch(Dispatchers.IO) {
                    logoutResponse.postValue(ApiResource.success(data = homeRepository.logoutUser(logoutRequest)))
            }
        }catch(exception : HttpException){
            logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun notificationCountApiCall() {
        notificationCountResponse.value = ApiResource.loading(data = null)
        try{
            viewModelScope.launch(Dispatchers.IO) {
                notificationCountResponse.postValue(ApiResource.success(data = homeRepository.getNotificationCount(StorePreferences(context).userId.toString())))
            }
        }catch(exception : HttpException){
            notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}