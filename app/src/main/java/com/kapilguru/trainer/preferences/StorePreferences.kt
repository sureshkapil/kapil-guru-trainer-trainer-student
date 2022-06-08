package com.kapilguru.trainer.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.kapilguru.trainer.*

class StorePreferences(context: Context) {


    private val prefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE);

    var trainerToken: String
        get() = prefs.getString(JWT_TOKEN, "").toString()
        set(value) = prefs.edit().putString(JWT_TOKEN, value).apply()

    var trainerId: Int
        get() = prefs.getInt(USER_ID,0)
        set(value) = prefs.edit().putInt(USER_ID,value).apply()

    var studentId: Int
        get() = prefs.getInt(USER_ID,0)
        set(value) = prefs.edit().putInt(USER_ID,value).apply()

    var isProfileUpdated: Int
        get() = prefs.getInt(IS_PROFILE_UPDATED,0)
        set(value) = prefs.edit().putInt(IS_PROFILE_UPDATED,value).apply()

    var contactNumber: String
        get() = prefs.getString(CONTACT_NO, "").toString()
        set(value) = prefs.edit().putString(CONTACT_NO, value).apply()

    var bankUpdateId: Int
        get() = prefs.getInt(UPDATE_BANK_ID, -1)
        set(value) = prefs.edit().putInt(UPDATE_BANK_ID, value).apply()

    var isBankUpdated: Int
        get() = prefs.getInt(IS_BANK_UPDATED,0)
        set(value) = prefs.edit().putInt(IS_BANK_UPDATED,value).apply()

    var isImageUpdated: Int
        get() = prefs.getInt(IS_IMAGE_UPDATED,0)
        set(value) = prefs.edit().putInt(IS_IMAGE_UPDATED,value).apply()

    var userName: String
       get() = prefs.getString(USER_NAME,"").toString()
       set(value) = prefs.edit().putString(USER_NAME,value).apply()

    var userCode: String
        get() = prefs.getString(USER_CODE,"").toString()
        set(value) = prefs.edit().putString(USER_CODE,value).apply()

    var isSubscribed: Int
        get() = prefs.getInt(IS_SUBSCRIBED,0)
        set(value) = prefs.edit().putInt(IS_SUBSCRIBED,value).apply()

    var isMarketing: Int
        get() = prefs.getInt(IS_MARKETING,0)
        set(value) = prefs.edit().putInt(IS_MARKETING,value).apply()

    var isKYCUpdated: Int
        get() = prefs.getInt(IS_KYC_UPDATED,0)
        set(value) = prefs.edit().putInt(IS_KYC_UPDATED,value).apply()

    var isOrganization: Int
        get() = prefs.getInt(IS_ORGANIZATION,0)
        set(value) = prefs.edit().putInt(IS_ORGANIZATION,value).apply()

    var lastAnnouncementId: Int
        get() = prefs.getInt(LAST_ANNOUNCEMENT_ID,0)
        set(value) = prefs.edit().putInt(LAST_ANNOUNCEMENT_ID,value).apply()

    var isOtherCountryCode: Int
        get() = prefs.getInt(IS_OTHER_COUNTRY_CODE,0)
        set(value) = prefs.edit().putInt(IS_OTHER_COUNTRY_CODE,value).apply()


    var email: String
        get() = prefs.getString(USER_EMAIL,"").toString()
        set(value) = prefs.edit().putString(USER_EMAIL,value).apply()

    fun clearStorePreferences(){
        prefs.edit().clear().apply()
    }
}
