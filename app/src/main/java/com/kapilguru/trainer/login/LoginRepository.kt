package com.kapilguru.trainer.login

import androidx.lifecycle.MutableLiveData
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest

open class LoginRepository(private val apiHelper: ApiHelper):AllRepo {

  open override suspend fun getUsers(loginUserRequest: LoginUserRequest) = apiHelper.getUsers(loginUserRequest)

}