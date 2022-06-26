package com.kapilguru.trainer.faculty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.studentsList.model.AllStudentsListPerTrainerApi
import com.kapilguru.trainer.studentsList.model.StudentDetails
import com.kapilguru.trainer.ui.profile.data.SaveProfileResponse

class FacultyViewModel(private val facultyRepository: FacultyRepository) : ViewModel() {

    var resultDat: MutableLiveData<ApiResource<AllStudentsListPerTrainerApi>> = MutableLiveData()




}