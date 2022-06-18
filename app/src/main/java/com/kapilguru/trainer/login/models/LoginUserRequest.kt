package com.kapilguru.trainer.login.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.BuildConfig

@Keep open class LoginUserRequest(
    @SerializedName("email_id") val emailId: String,
    @SerializedName("password") val password: String,
    @SerializedName("device") val device: String? = "ANDROID",
    @SerializedName("package_name") val packageName: String? = BuildConfig.PACKAGE_ID,
)