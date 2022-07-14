package com.kapilguru.trainer.feeManagement

import androidx.lifecycle.MutableLiveData
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.feeManagement.addFeeManagement.AddFeeManagementRequest
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest

open class FeeManagementRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers(loginUserRequest: LoginUserRequest) = apiHelper.getUsers(loginUserRequest)


    suspend fun getStudentFeeRecords(trainerId: String) = apiHelper.getStudentFeeRecords(trainerId)


    suspend fun getStudentPaidRecords(trainerId: String) = apiHelper.getStudentPaidRecords(trainerId)


    suspend fun getStudentFeeFollowUps(trainerId: String) = apiHelper.getStudentFeeFollowUps(trainerId)


    suspend fun addFeeDetailsRequest(addFeeDetailsRequest: AddFeeManagementRequest) = apiHelper.addFeeDetailsRequest(addFeeDetailsRequest)





}