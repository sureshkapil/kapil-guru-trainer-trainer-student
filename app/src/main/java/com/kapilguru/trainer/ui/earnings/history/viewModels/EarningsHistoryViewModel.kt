package com.kapilguru.trainer.ui.earnings.history.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.earnings.history.EarningsHistoryRepository
import com.kapilguru.trainer.ui.earnings.history.model.EarningsHistoryResponseApi
import com.kapilguru.trainer.ui.earnings.history.model.HistoryPaymentAmountDetailsApi
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsCourse
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmount
import com.kapilguru.trainer.ui.earnings.referralDetails.ReferralAmountDetails
import com.kapilguru.trainer.ui.earnings.webinarAmount.WebinarAmount
import com.kapilguru.trainer.ui.earnings.webinarAmount.WebinarAmountResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class EarningsHistoryViewModel(
    val earningsHistoryRepository: EarningsHistoryRepository,
    appliaction: Application
): AndroidViewModel(appliaction) {

    var resultInfo: MutableLiveData<ApiResource<EarningsHistoryResponseApi>> = MutableLiveData()
    var paymentInfo: MutableLiveData<ApiResource<HistoryPaymentAmountDetailsApi>> = MutableLiveData()
    var earningsDetailsCourse: ArrayList<EarningsDetailsCourse> = arrayListOf()
    var referralAmountDetails: ArrayList<ReferralAmount> = arrayListOf()
    var webinarAmountDetails: ArrayList<WebinarAmountResponse> = arrayListOf()

    fun fetchResponse(trainerId: String) {
        resultInfo.value = ApiResource.loading(data= null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultInfo.postValue(
                    ApiResource.success(
                        earningsHistoryRepository.getEarningsHistory(trainerId)
                    )
                )
            } catch (exception: HttpException) {
                resultInfo.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                resultInfo.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun fetchHistoryDetailsAmount(trainerId: String, selectedId: String) {
        paymentInfo.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                paymentInfo.postValue(
                    ApiResource.success(
                        earningsHistoryRepository.getHistoryDetailAmount(trainerId, selectedId)
                    )
                )
            } catch (exception: HttpException) {
                paymentInfo.postValue(ApiResource.error(data = null, exception.message()))
            } catch (exception: IOException) {
                paymentInfo.postValue(ApiResource.error(data = null,exception.message?: "Error Occurred!"))
            }
        }
    }

}