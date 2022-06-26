package com.kapilguru.trainer.faculty

import androidx.lifecycle.MutableLiveData
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest

open class FacultyRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers(loginUserRequest: LoginUserRequest) = apiHelper.getUsers(loginUserRequest)

}