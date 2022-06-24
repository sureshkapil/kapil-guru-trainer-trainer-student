package com.kapilguru.trainer.coupons

import com.kapilguru.trainer.ApiHelper

open class CouponRepository(private val apiHelper: ApiHelper) {

  open suspend fun getAllCoupons(trainerId: Int) = apiHelper.getCouponsList(trainerId)


  open suspend fun addCoupon(addCouponsRequest: AddCouponsRequest) = apiHelper.addCoupon(addCouponsRequest)


  open suspend fun getCategoryCourse(couponCourseCategoryRequest:CouponCourseCategoryRequest) = apiHelper.getCategoryCourse(couponCourseCategoryRequest)

}