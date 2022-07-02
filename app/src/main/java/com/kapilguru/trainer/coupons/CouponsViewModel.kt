package com.kapilguru.trainer.coupons

import android.app.Application
import android.util.Log
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

    var studentModelResponse: MutableLiveData<ApiResource<ResponseStudentModel>> = MutableLiveData()

    var createCouponResponse: MutableLiveData<ApiResource<CreateCouponResponse>> = MutableLiveData()

    var allCouponsResponseListApi: ArrayList<AllCouponsResponseListApi>? = arrayListOf()

    var courseId: MutableLiveData<Int> = MutableLiveData(0)
    var validUpto: MutableLiveData<String> = MutableLiveData("")
    var validUptoDateToApi: MutableLiveData<String> = MutableLiveData("")
    var couponCode: MutableLiveData<String> = MutableLiveData("")
    var studentId: MutableLiveData<Int> = MutableLiveData(null)
    var discountPercent: MutableLiveData<String> = MutableLiveData("")
    var createdBy: String = trainerId
    var showToastText : MutableLiveData<String> = MutableLiveData()
    var isPublic : MutableLiveData<Boolean> = MutableLiveData(true)


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


    fun getStudentList(studentRequestModel:StudentRequestModel) {
        studentModelResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentModelResponse.postValue(ApiResource.success(data = repository.getStudentList(studentRequestModel)))
            } catch (exception: HttpException) {
                studentModelResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                studentModelResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }


    fun createCouponCode() {
        val createCouponCodeRequestModel: CreateCouponCodeRequestModel = getCouponObject()
        createCouponResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                createCouponResponse.postValue(ApiResource.success(data = repository.createCouponCode(createCouponCodeRequestModel)))
            } catch (exception: HttpException) {
                createCouponResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            } catch (exception: IOException) {
                createCouponResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }

    private fun getCouponObject(): CreateCouponCodeRequestModel {
        return CreateCouponCodeRequestModel(
            courseId = courseId.value,
            validUpto = validUptoDateToApi.value,
            couponCode = couponCode.value,
            studentId = studentId.value,
            discountPercent = discountPercent.value!!.toInt(),
            createdBy = createdBy
        )

    }

    fun validateCreateCoupon(): Boolean {
        var isValid = true
        when {
            couponCode.value.toString().trim().isEmpty() -> {
                showToastText.value = "Please enter Coupon Code"
                isValid = false
            }
            couponCode.value.toString().trim().length<4 -> {
                showToastText.value = "Coupon Code should contain at least of 4 characters"
                isValid = false
            }
            isCouponCodeAlreadyExists(couponCode.value.toString().trim()) -> {
                showToastText.value = "Entered Coupon code already exits, Duplicate Coupon codes are not allowed"
                isValid = false
            }
            discountPercent.value.toString().trim().isEmpty() -> {
                showToastText.value = "Please Enter Discount Percentage"
                isValid = false
            }
            discountPercent.value.toString().trim() == "0" -> {
                showToastText.value = "Please Enter Discount Percentage can't be 0"
                isValid = false
            }
            validUptoDateToApi.value.toString().trim().isEmpty() -> {
                showToastText.value = "Please Enter Coupon Expiry Date"
                isValid = false
            }
            courseId.value == 0 -> {
                showToastText.value = "Please Select Category"
                isValid = false
            }
            isPublic.value == true -> {
                studentId.value = null
            }
        }
        return isValid
    }

    private fun isCouponCodeAlreadyExists(couponCode: String): Boolean {
        allCouponsResponseListApi?.let {info ->
        val data:AllCouponsResponseListApi? = info.singleOrNull { it.couponCode.equals(couponCode, true) }
           data?.let {
               Log.d(TAG, "isCouponCodeAlreadyExists: there")
               return true
           }?:run {
               Log.d(TAG, "isCouponCodeAlreadyExists: not present")
               return false
           }

        }?:run{
           return true
        }

    }

}