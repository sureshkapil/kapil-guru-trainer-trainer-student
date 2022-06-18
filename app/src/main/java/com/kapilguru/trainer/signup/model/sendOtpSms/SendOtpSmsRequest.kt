package com.kapilguru.trainer.signup.model.sendOtpSms


import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.UserRole

data class SendOtpSmsRequest(
//    @SerializedName("accepted") var accepted: Boolean?=true,
    @SerializedName("name") var name: String?="",
    @SerializedName("email_id") var emailId: String?="",
    @SerializedName("password") var password: String?="",
    @SerializedName("contact_number") var contactNumber: String?="",
    @SerializedName("country_code") var countryCode: String?="91",
    @SerializedName("user_role_id") var userRoleId: Int?=UserRole.STUDENT.roleId(),
    @SerializedName("uuid") var uuid: String?="",
    @SerializedName("is_other_country_code") var isOtherCountryCode: Boolean?=false,
)