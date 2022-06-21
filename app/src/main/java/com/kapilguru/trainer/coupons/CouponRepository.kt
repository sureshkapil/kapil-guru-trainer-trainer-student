package com.kapilguru.trainer.coupons

import com.kapilguru.trainer.ApiHelper

open class CouponRepository(private val apiHelper: ApiHelper) {

  open suspend fun getAllCoupons(trainerId: Int) = apiHelper.getCouponsList(trainerId)

}