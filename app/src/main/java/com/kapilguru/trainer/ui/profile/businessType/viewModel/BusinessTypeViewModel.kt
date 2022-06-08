package com.kapilguru.trainer.ui.profile.businessType.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.ui.profile.data.ProfileData

class BusinessTypeViewModel : ViewModel(){
    private val TAG = "BusinessTypeViewModel"
    var profileMutLiveData: MutableLiveData<ProfileData> = MutableLiveData()
}