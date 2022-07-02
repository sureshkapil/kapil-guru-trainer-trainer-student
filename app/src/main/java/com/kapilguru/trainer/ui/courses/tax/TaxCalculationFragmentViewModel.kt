package com.kapilguru.trainer.ui.courses.tax

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.showAppToast
import com.kapilguru.trainer.ui.courses.addcourse.AddCourseRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TaxCalculationFragmentViewModel(private val addCourseRepository: AddCourseRepository) : ViewModel() {

    var taxCalculationResponse: MutableLiveData<ApiResource<TaxCalculationResponse>> = MutableLiveData()

    var taxCalculationResponseApi: MutableLiveData<TaxCalculationResponseApi> = MutableLiveData()

    var priceModel: MutableLiveData<PriceModel> = MutableLiveData(PriceModel())

    var fee: MutableLiveData<String> = MutableLiveData()
    var discountAmount: MutableLiveData<String> = MutableLiveData()
    var actualFee: MutableLiveData<String> = MutableLiveData()
    var isInternetChargesAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    var isTaxChargesAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    var errorText: MutableLiveData<String> = MutableLiveData()


    init {
        observePriceModel()
    }

    private fun observePriceModel() {

//        priceModel.value?.finalPrice = (firstName.value?.toDouble()!! - lastName.value?.toDouble()!!).toString()


    }



    fun getTax() {
        taxCalculationResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch {
            try {
                taxCalculationResponse.postValue(ApiResource.success(addCourseRepository.getTaxes()))
            } catch (e: HttpException) {
                taxCalculationResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
            } catch (e: IOException) {
                taxCalculationResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun calculateFinalPrice() {
        val price: String = this.fee.value ?: return
        val offerPrice: String = this.discountAmount.value ?:  "0"
        val isInternetChargesAdded: Boolean = this.isInternetChargesAdded.value ?: return
        var afterDeductionsPrice: Double = 0.0
        if(offerPrice.toDouble()<=price.toDouble()) {
            when (isInternetChargesAdded) {
                true -> {
                    afterDeductionsPrice = price.toDouble() - offerPrice.toDouble()
                    afterDeductionsPrice += (afterDeductionsPrice * (taxCalculationResponseApi.value!!.addPercent / 100.0))
                }

                false -> {
                    afterDeductionsPrice = price.toDouble() - offerPrice.toDouble()
                }
            }
            actualFee.value = afterDeductionsPrice.toString()
        } else {
            errorText.value = "Discount Amount can't be more than price"
            this.discountAmount.value = price
            return
        }
    }

}