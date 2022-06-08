package com.kapilguru.trainer.ui.changePassword

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest

class ChangePasswordRepository(private val apiHelper : ApiHelper) {
    suspend fun changePassword(changePasswordRequest : ChangePasswordRequest) = apiHelper.changePassword(changePasswordRequest)
    suspend fun logoutUser(logoutRequest : LogoutRequest) = apiHelper.logoutUser(logoutRequest)
}