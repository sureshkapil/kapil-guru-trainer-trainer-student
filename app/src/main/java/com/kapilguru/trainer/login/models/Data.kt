package com.kapilguru.trainer.login.models

import androidx.annotation.Keep

@Keep
open class Data(
    val auth: Boolean,
    val token: String?,
    val refreshToken: String?,
    val user_id: Int,
    val user_code: String?,
    val username: String?,
    val userImage: String?,
    val title: String?,
    val email: String?,
    val countryCode : Int,
    val contactNumber: String?,
    val userRoleId: Int,
    val isSubscribed: Int,
    val isProfileUpdated: Int,
    val isImageUpdated: Int,
    val isBankUpdated: Int,
    val isOtherCountryCode: Int,
    val lastAnnouncementId: Int,
    val isEnrolled: Int,
    val currency: String?,
    val isOrganization: Int,
    val isKycUpdated: Int,
)