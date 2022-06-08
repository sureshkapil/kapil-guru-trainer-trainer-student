package com.kapilguru.trainer.network


import android.content.Context

import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.profile.data.ProfileResponse
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route



class TokenAuthenticator(var context: Context, var  myServiceHolder: MyServiceHolder?) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (myServiceHolder == null) {
            return null
        }


//        val username = StorePreferences(context).trainerId.toString()
//        val retrofitResponse: Response<*> = myServiceHolder.get()!!.refreshToken(username).execute()
//        if (retrofitResponse != null) {
//            val refreshTokenResponse: ProfileResponse? = retrofitResponse.body()
//            val newAccessToken: String = refreshTokenResponse?.data.
//            return response.request().newBuilder().header("Authorization", newAccessToken).build()
//        }
        return null
    }

    init {
        this.context = context
        this.myServiceHolder = myServiceHolder
    }
}
