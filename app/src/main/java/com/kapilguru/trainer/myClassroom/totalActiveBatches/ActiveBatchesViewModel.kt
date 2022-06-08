package com.kapilguru.trainer.myClassroom.totalActiveBatches

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData

class ActiveBatchesViewModel() : ViewModel() {
    private val TAG = "ActiveBatchesViewModel"

    /*batch lit and courseName are stroed in viewModel instead of using saved instances.*/
    var batchList = ArrayList<NewMessageData>()
    var courseName = ""
}