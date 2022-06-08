package com.kapilguru.trainer.referandearn

import com.kapilguru.trainer.ApiHelper

class ReferAndEarnRepository(private val apiHelper: ApiHelper) {
    suspend fun referAndEarn(referAndEarnRequest:ReferAndEarnRequest) = apiHelper.postReferAndEarn(referAndEarnRequest)
}