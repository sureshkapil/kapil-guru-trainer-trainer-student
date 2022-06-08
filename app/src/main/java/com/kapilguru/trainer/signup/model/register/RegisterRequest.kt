package com.kapilguru.trainer.signup.model.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("accepted") var accepted: Boolean = true,
    @SerializedName("contact_number") var contactNumber: String = "",
    @SerializedName("country_code")var countryCode: String = "91", //country code id ex : 91 fo india
    @SerializedName("email_id")var emailId: String = "",
    @SerializedName("is_student")var isStudent: Boolean = false,
    @SerializedName("is_trainer")var isTrainer: Boolean = true,
    @SerializedName("name")var name: String = "",
    @SerializedName("password")var password: String = "",
    @SerializedName("uuid")var uuid: String = "",
    @SerializedName("is_organization")var isOrganization: Boolean = false,
    @SerializedName("is_other_country_code")var isOtherCountryCode: Boolean = false,
)