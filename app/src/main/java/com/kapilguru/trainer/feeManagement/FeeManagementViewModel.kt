package com.kapilguru.trainer.feeManagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.studentsList.model.AllStudentsListPerTrainerApi
import com.kapilguru.trainer.studentsList.model.StudentDetails
import com.kapilguru.trainer.ui.profile.data.SaveProfileResponse

class FeeManagementViewModel(private val feeManagementRepository: FeeManagementRepository) : ViewModel() {

    var resultDat: MutableLiveData<ApiResource<AllStudentsListPerTrainerApi>> = MutableLiveData()

    var totalAmount : MutableLiveData<Double> = MutableLiveData(0.0)

    var dueAmount : MutableLiveData<Double> = MutableLiveData(100.0)

    var numberOfInstallments : MutableLiveData<Int> = MutableLiveData(5)


}