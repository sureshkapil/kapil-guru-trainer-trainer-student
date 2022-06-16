package com.kapilguru.trainer.ui.profile

import android.content.Context
import com.kapilguru.trainer.login.models.Data
import com.kapilguru.trainer.preferences.StorePreferences

class ProfileUtils {
    private val TAG = "ProfileUtils"

    companion object {
        fun saveProfileData(context: Context, loginResponseData: Data) {
            val pref = StorePreferences(context)
            pref.token = loginResponseData.token
            pref.userId = loginResponseData.user_id
            pref.userRoleId = loginResponseData.userRoleId
            pref.isProfileUpdated = loginResponseData.isProfileUpdated
            pref.contactNumber = loginResponseData.contactNumber
            pref.isBankUpdated = loginResponseData.isBankUpdated
            pref.isImageUpdated = loginResponseData.isImageUpdated
            pref.userName = loginResponseData.username
            pref.userCode = loginResponseData.user_code
            pref.isSubscribed = loginResponseData.isSubscribed
            pref.isKYCUpdated = loginResponseData.isKycUpdated
            pref.isOrganization = loginResponseData.isOrganization
            pref.lastAnnouncementId = loginResponseData.lastAnnouncementId
            pref.isOtherCountryCode = loginResponseData.isOtherCountryCode
            pref.userImage = loginResponseData.userImage
            pref.title  = loginResponseData.title
            pref.email = loginResponseData.email
            pref.countryCode = loginResponseData.countryCode
            pref.isEnrolled = loginResponseData.isEnrolled
            pref.currency = loginResponseData.currency
            pref.tenantId = loginResponseData.tenantId
        }

        fun isSubscribed(context: Context): Boolean {
            return StorePreferences(context).isSubscribed == 1
        }

        fun isOrganization(context: Context): Boolean {
            return StorePreferences(context).isOrganization == 1
        }

        fun updateIsSubscribed(isSubscribed: Boolean, context: Context) {
            val preferences = StorePreferences(context)
            preferences.isSubscribed = if (isSubscribed) 1 else 0
        }
    }
}