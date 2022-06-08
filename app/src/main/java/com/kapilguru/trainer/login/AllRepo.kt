package com.kapilguru.trainer.login

import androidx.lifecycle.MutableLiveData
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest

interface AllRepo {

    suspend fun getUsers(loginUserRequest: LoginUserRequest):LoginResponse

}