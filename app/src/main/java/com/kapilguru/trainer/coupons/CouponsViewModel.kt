package com.kapilguru.trainer.coupons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CouponsViewModel(
    private val repository: CouponRepository, application: Application
) : AndroidViewModel(application) {

    private val TAG = "MyClassroomViewModel"
    val trainerId = StorePreferences(application).userId.toString()

    var couponsList: MutableLiveData<ApiResource<AllCouponsResponseList>> = MutableLiveData()

    var addCouponResponse: MutableLiveData<ApiResource<AddCouponResponse>> = MutableLiveData()

    var addCouponRequest: MutableLiveData<AddCouponsRequest> = MutableLiveData(AddCouponsRequest())

    var couponLiveCoursesResponse: MutableLiveData<ApiResource<CouponLiveCoursesResponse>> = MutableLiveData()

    var courseId: MutableLiveData<Int> = MutableLiveData()
    var varidUpto: MutableLiveData<String> = MutableLiveData()
    var couponCode: MutableLiveData<String> = MutableLiveData()
    var studentId: MutableLiveData<Int> = MutableLiveData()
    var discountPercent: MutableLiveData<Int> = MutableLiveData()
    var createdBy: MutableLiveData<Int> = MutableLiveData()


    fun getCouponsList(trainerId: Int) {
        couponsList.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                couponsList.postValue(ApiResource.success(data = repository.getAllCoupons(trainerId)))
            } catch (exception: HttpException) {
                couponsList.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                couponsList.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }

    fun addCoupon(addCouponRequest: AddCouponsRequest) {
        addCouponResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addCouponResponse.postValue(ApiResource.success(data = repository.addCoupon(addCouponRequest)))
            } catch (exception: HttpException) {
                addCouponResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                addCouponResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }


    fun getCourseList(couponCourseCategoryRequest:CouponCourseCategoryRequest) {
        couponLiveCoursesResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                couponLiveCoursesResponse.postValue(ApiResource.success(data = repository.getCategoryCourse(couponCourseCategoryRequest)))
            } catch (exception: HttpException) {
                couponLiveCoursesResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                couponLiveCoursesResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }

}