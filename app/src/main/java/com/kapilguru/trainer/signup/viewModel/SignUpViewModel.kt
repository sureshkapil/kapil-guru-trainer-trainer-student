package com.kapilguru.trainer.signup.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileResponse
import com.kapilguru.trainer.generateUuid
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.signup.SignUpRepository
import com.kapilguru.trainer.signup.model.register.RegisterRequest
import com.kapilguru.trainer.signup.model.register.RegisterResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOptSmsResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailResponse
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpRequest
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpResponse
import com.kapilguru.trainer.ui.profile.profileInfo.models.CountryResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SignUpViewModel(private val repository: SignUpRepository) : ViewModel() {
    private val TAG = "SignUpViewModel"
    var previousEnteredMail: String = ""
    var previousEnteredMobile: String = ""
    var isMailValid = false
    var isMobileValid = false
    val enterPassword: MutableLiveData<String> = MutableLiveData()
    val confirmPassword: MutableLiveData<String> = MutableLiveData()
    val otpMail: MutableLiveData<String> = MutableLiveData()
    val otpMobile: MutableLiveData<String> = MutableLiveData()
    val informUser: MutableLiveData<String> = MutableLiveData()
    val isTermsChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    val countryList: MutableLiveData<ApiResource<CountryResponce>> = MutableLiveData()
    val validateMailRequest: MutableLiveData<ValidateMailRequest> = MutableLiveData(ValidateMailRequest())
    val validateMobileRequest: MutableLiveData<ValidateMobileRequest> = MutableLiveData(ValidateMobileRequest())
    var registerRequest: MutableLiveData<RegisterRequest> = MutableLiveData(RegisterRequest())
    val validateMailResponse: MutableLiveData<ApiResource<ValidateMailResponse>> = MutableLiveData()
    val validateMobileResponse: MutableLiveData<ApiResource<ValidateMobileResponse>> = MutableLiveData()
    val registerResponse: MutableLiveData<ApiResource<RegisterResponse>> = MutableLiveData()
    val validateOtpRequest: MutableLiveData<ValidateOtpRequest> = MutableLiveData(ValidateOtpRequest())
    val validateOtpResponse: MutableLiveData<ApiResource<ValidateOtpResponse>> = MutableLiveData()
    val sendOptSmsResponse: MutableLiveData<ApiResource<SendOptSmsResponse>> = MutableLiveData()

    fun fetchCountriesList() {
        countryList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                countryList.postValue(ApiResource.success(repository.fetchCountryListForSignUp()))
            } catch (exception: HttpException) {
                countryList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                countryList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun validateMail() {
        validateMailResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                validateMailRequest.value?.let { validateMail ->
                    previousEnteredMail = validateMail.emailId
                    validateMailResponse.postValue(ApiResource.success(repository.validateMail(validateMail)))
                }
            } catch (exception: Exception) {
                validateMailResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun validateMobile() {
        validateMobileResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                validateMobileRequest.value?.let { validateMobile ->
                    previousEnteredMobile = validateMobile.contactNo!!
                    validateMobileResponse.postValue(ApiResource.success(data = repository.validateMobile(validateMobile)))
                }
            } catch (exception: HttpException) {
                validateMobileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                validateMobileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun validateOtp() {
        validateOtpResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                validateOtpRequest.value?.let { validateOtpRequest ->
                    validateOtpResponse.postValue(ApiResource.success(data = repository.validateOtp(validateOtpRequest)))
                }
            } catch (exception: HttpException) {
                validateOtpResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                validateOtpResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun addIsOrganizationValueToRegisterRequest(isOrganization : Boolean){
        registerRequest.value?.isOrganization = isOrganization
    }

    fun addIsOtherCountryCodeValueToRegisterRequest(isOtherCountryCode : Boolean){
        registerRequest.value?.isOtherCountryCode = isOtherCountryCode
    }

    fun register() {
        setRegisterRequestData()
        registerResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerRequest.value?.let { accountRegisterReq ->
                    registerResponse.postValue(ApiResource.success(data = repository.registerAccount(accountRegisterReq)))
                }
            } catch (exception: HttpException) {
                registerResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                registerResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    private fun setRegisterRequestData() {
        registerRequest.value?.contactNumber = validateMobileRequest.value?.contactNo!!
        registerRequest.value?.emailId = validateMailRequest.value?.emailId!!
        registerRequest.value?.password = enterPassword.value!!
        registerRequest.value?.uuid = generateUuid().toString()
    }

    fun sendOtpMessage() {
        val sendOtpSmsRequest: SendOtpSmsRequest = SendOtpSmsRequest().apply {
            contactNumber = validateMobileRequest.value?.contactNo
            emailId = validateMailRequest.value?.emailId
            name = registerRequest.value?.name
            password = enterPassword.value!!
            uuid = registerRequest.value?.uuid
        }
        sendOptSmsResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendOptSmsResponse.postValue(ApiResource.success(repository.sendOtpMessage(sendOtpSmsRequest)))
            } catch (exception: HttpException) {
                sendOptSmsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                sendOptSmsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: Exception) {
                sendOptSmsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}