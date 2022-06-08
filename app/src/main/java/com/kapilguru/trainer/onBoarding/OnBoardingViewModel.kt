package com.kapilguru.trainer.onBoarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kapilguru.trainer.R

class OnBoardingViewModel(application: Application) : AndroidViewModel(application) {

    val listOfOnBoardingItem: MutableLiveData<MutableList<OnBoardingItem>> = MutableLiveData()
    private val myApp = application

    fun setOnBoardingItem() {
        val onBoardingItems = mutableListOf<OnBoardingItem>()
        onBoardingItems.add(OnBoardingItem().apply {
            image = R.drawable.bulb_icon
            toptext = myApp.getString(R.string.onboard1_top_text)
            title = myApp.getString(R.string.onboard1_title)
            description = myApp.getString(R.string.onboard1_subtitle)
        })
       /* onBoardingItems.add(OnBoardingItem().apply {
            image = R.drawable.increase_reach_icon
            toptext = myApp.getString(R.string.onboard2_top_text)
            title = myApp.getString(R.string.onboard2_title)
            description = myApp.getString(R.string.onboard2_subtitle)
        })
        onBoardingItems.add(OnBoardingItem().apply {
            image = R.drawable.earn_flexible_icon
            toptext = myApp.getString(R.string.onboard3_top_text)
            title = myApp.getString(R.string.onboard3_title)
            description = myApp.getString(R.string.onboard3_subtitle)
        })*/
        listOfOnBoardingItem.postValue(onBoardingItems)
    }
}