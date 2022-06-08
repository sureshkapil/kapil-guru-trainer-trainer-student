package com.kapilguru.trainer.login.models

import androidx.annotation.Keep

@Keep
open class LoginResponse(
    val data: Data?,
    val message: String,
    val status: Int
)