package com.kapilguru.trainer.ui.courses.tax

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.addcourse.AddCourseRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TaxCalculationFragmentViewModel(private val addCourseRepository: AddCourseRepository) : ViewModel() {

    var taxCalculationResponse: MutableLiveData<ApiResource<TaxCalculationResponse>> = MutableLiveData()

    var taxCalculationResponseApi: MutableLiveData<TaxCalculationResponseApi> = MutableLiveData()

    var priceModel: MutableLiveData<PriceModel> = MutableLiveData(PriceModel())

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

}