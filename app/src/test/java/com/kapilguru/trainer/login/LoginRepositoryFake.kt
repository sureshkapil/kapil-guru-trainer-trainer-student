package com.kapilguru.trainer.login

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.login.models.Data
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest

open class LoginRepositoryFake: AllRepo {

   override suspend fun getUsers(loginUserRequest: LoginUserRequest) =
      LoginResponse(
         Data(
            auth = true,
            contactNumber = "9989",
            email = "abc@gmail.com",
            isAdmin = 1,
            isTrainer = 1,
            isStudent = 1,
            token = "123",
            user_id = 1,
            username = "123",
            isProfileUpdated = 1,
            isBankUpdated = 1,
            isImageUpdated = 1,
            user_code = "1",
            isSubscribed = 1
         ), "succes", 200
      )

}