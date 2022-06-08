package com.kapilguru.trainer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
            image = R.drawable.upcoming_batch_icon
            title = "Upcoming Batches"
            backgroundcolor = R.color.home_top_bg1
        })
        homeViewPagerItem.add(HomeViewPagerItem().apply {
            image = R.drawable.schedule_webinar
            title = "Webinars"
            backgroundcolor = R.color.home_top_bg2
        })
        homeViewPagerItem.add(HomeViewPagerItem().apply {
            image = R.drawable.schedule_guest_lecture
            title = "Free Guest Lectures"
            backgroundcolor = R.color.home_top_bg3
        })
        listOfHomeTopItems.postValue(homeViewPagerItem)
    }

    fun setHomeItems() {
        val homeItems = mutableListOf<HomeItem>()

        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_my_course
            title = "My Courses"
        })

        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_my_students
            title = "My Students"
        })

        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_announcements
            title = "Messages"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_webinars
            title = "Webniars"
        })

        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_demo_lectures
            title = "Demo Lectures"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_exams
            title = "Exams"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_earnings
            title = "Earnings"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_refund_list
            title = "Refund List"
        })
        homeItems.add(HomeItem().apply {
            image = R.drawable.ic_subscriptions
            title = "Subscriptions"
        })

        listOfHomeItems.postValue(homeItems)
    }

    fun fetchUpcomingSchedule() {
        val trainerId = StorePreferences(context).trainerId
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
                notificationCountResponse.postValue(ApiResource.success(data = homeRepository.getNotificationCount(StorePreferences(context).trainerId.toString())))
            }
        }catch(exception : HttpException){
            notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}