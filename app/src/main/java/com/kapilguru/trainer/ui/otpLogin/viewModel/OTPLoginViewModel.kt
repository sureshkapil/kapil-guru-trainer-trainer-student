package com.kapilguru.trainer.ui.otpLogin.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.emailValidation
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileResponse
import com.kapilguru.trainer.generateUuid
import com.kapilguru.trainer.isValidMobileNo
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailResponse
import com.kapilguru.trainer.ui.otpLogin.OTPLoginRepository
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginValidateRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginValidateResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class OTPLoginViewModel(private val repository: OTPLoginRepository) : ViewModel() {
    private val TAG = "OTPLoginViewModel"
    var otpLoginValidateRequest : MutableLiveData<OTPLoginValidateRequest> = MutableLiveData(OTPLoginValidateRequest()) //this variableName should be otpLoginRequest, but as the url is named as OTPLoginValidateRequest, the same is applied.
    val otpLoginValidateResponse: MutableLiveData<ApiResource<OTPLoginValidateResponse>> = MutableLiveData()
    val otpLoginRequest :  MutableLiveData<OTPLoginRequest> = MutableLiveData(OTPLoginRequest())
    val otpLoginResponse : MutableLiveData<ApiResource<LoginResponse>> = MutableLiveData()
    val validateMailResponse: MutableLiveData<ApiResource<ValidateMailResponse>> = MutableLiveData()
    val validateMobileResponse: MutableLiveData<ApiResource<ValidateMobileResponse>> = MutableLiveData()

    /*fun validateMail(validateMailRequest: ValidateMailRequest) {
        validateMailResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                validateMailResponse.postValue(ApiResource.success(repository.validateMail(validateMailRequest)))
            } catch (exception: Exception) {
                validateMailResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }*/

    /*fun validateMobile(validateMobileRequest: ValidateMobileRequest) {
        validateMobileResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                validateMobileResponse.postValue(ApiResource.success(data = repository.validateMobile(validateMobileRequest)))
            } catch (exception: HttpException) {
                validateMobileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                validateMobileResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }*/

    fun requestOTP() {
        otpLoginValidateResponse.value = ApiResource.loading(data = null)
        setOTPLoginValidateRequest()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                otpLoginValidateRequest.value?.let { otpLoginValidateReq ->
                    otpLoginValidateResponse.postValue(ApiResource.success(data = repository.requestOTP(otpLoginValidateReq)))
                }
            } catch (exception: HttpException) {
                otpLoginValidateResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                otpLoginValidateResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    /*private fun setOTPLoginValidateRequest() {
        val uuid = generateUuid()
        otpLoginValidateRequest.value?.uuid = uuid.toString()
        otpLoginValidateRequest.value?.let { otpLoginValidReq ->
            if (otpLoginValidReq.emailOrPhone.isValidMobileNo()) {
                otpLoginValidateRequest.value?.otpType = "contact_number"
            } else if (!otpLoginValidReq.emailOrPhone.emailValidation()) {
                otpLoginValidateRequest.value?.otpType = "email"
            }
        }
        otpLoginRequest.value?.uuid = uuid.toString() // The same UUID sent for OTPLoginValidateRequest should be sent for OTPLoginRequest.
        //So setting here.
    }*/

    private fun setOTPLoginValidateRequest() {
        val uuid = generateUuid()
        otpLoginValidateRequest.value?.uuid = uuid.toString()
        otpLoginValidateRequest.value?.let { otpLoginValidReq ->
            if (otpLoginValidReq.emailOrPhone.isValidMobileNo()) {
                otpLoginValidateRequest.value?.otpType = ""
            } else if (!otpLoginValidReq.emailOrPhone.emailValidation()) {
                otpLoginValidateRequest.value?.otpType = "email"
            }
        }
        otpLoginRequest.value?.uuid = uuid.toString() // The same UUID sent for OTPLoginValidateRequest should be sent for OTPLoginRequest.
        //So setting here.
    }

    fun sendOTPLogin(){
        otpLoginResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO){
            try{
                otpLoginRequest.value?.let{ request->
                    otpLoginResponse.postValue(ApiResource.success(data = repository.otpLogin(request)))
                }
            }catch (exception : Exception){
                otpLoginResponse.postValue(ApiResource.error(data = null,message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}