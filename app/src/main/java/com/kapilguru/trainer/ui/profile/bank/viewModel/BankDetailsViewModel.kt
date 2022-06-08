package com.kapilguru.trainer.ui.profile.bank.viewModel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.profile.bank.BankDetailsRepository
import com.kapilguru.trainer.ui.profile.bank.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BankDetailsViewModel(private val bankDetailsRepository: BankDetailsRepository) : ViewModel() {
    private val TAG = "BankDetailsViewModel"
    val bankDetailsFetchResMUtLiveData : MutableLiveData<ApiResource<BankDetailsFetchResponce>> = MutableLiveData()
    val bankDetailsUploadResMutLiveData : MutableLiveData<ApiResource<BankDetailsUploadResponce>> = MutableLiveData()
    val bankDetails : MutableLiveData<BankDetailsFetchResData> = MutableLiveData()
    val errorDescription : MutableLiveData<String> = MutableLiveData()
    val isBankDetailsEditable : MutableLiveData<Boolean> = MutableLiveData()

    fun getBankDetails(userId : String){
        bankDetailsFetchResMUtLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bankDetailsFetchResMUtLiveData.postValue(ApiResource.success(bankDetailsRepository.getBankDetails(userId)))
            } catch (exception: HttpException) {
                bankDetailsFetchResMUtLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                bankDetailsFetchResMUtLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun saveBankDetails(bankDetailsUploadReq: BankDetailsUploadRequest){
        Log.d(TAG,"saveBankDetails")
        bankDetailsUploadResMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bankDetailsUploadResMutLiveData.postValue(ApiResource.success(bankDetailsRepository.saveBakDetails(bankDetailsUploadReq)))
            } catch (exception: HttpException) {
                bankDetailsUploadResMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                bankDetailsUploadResMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun updateBakDetails(bankId: String, bankDetailsUploadReq : BankDetailsUploadRequest){
        Log.d(TAG,"updateBakDetails")
        bankDetailsUploadResMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bankDetailsUploadResMutLiveData.postValue(ApiResource.success(bankDetailsRepository.updateBakDetails(bankId,bankDetailsUploadReq)))
            } catch (exception: HttpException) {
                bankDetailsUploadResMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                bankDetailsUploadResMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun dataValid() : Boolean{
        if(TextUtils.isEmpty(bankDetails.value?.accountName)){
            errorDescription.value = "Please enter Account Name"
            return false
        }
        if(TextUtils.isEmpty(bankDetails.value?.accountNumber)){
            errorDescription.value = "Please enter Account Number"
            return false
        }
        if(TextUtils.isEmpty(bankDetails.value?.bankName)){
            errorDescription.value = "Please enter Ban Name"
            return false
        }
        if(TextUtils.isEmpty(bankDetails.value?.branchName)){
            errorDescription.value = "Please enter Branch Name"
            return false
        }
        if(TextUtils.isEmpty(bankDetails.value?.ifscCode)){
            errorDescription.value = "Please enter IFSC Code"
            return false
        }
        return true
    }
}