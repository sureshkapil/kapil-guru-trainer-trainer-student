package com.kapilguru.trainer.myClassRoomDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageResponse
import com.kapilguru.trainer.network.ApiResource

class MyClassDetailsViewModel() : ViewModel() {
    private val TAG= "OverViewViewModel"

    var batchInfo:MutableLiveData<NewMessageData?> = MutableLiveData()
    var batchApiResponse:MutableLiveData<ApiResource<NewMessageResponse>> = MutableLiveData()
    var batchId:String?=null


    fun getBatchDetails() {

    }

}