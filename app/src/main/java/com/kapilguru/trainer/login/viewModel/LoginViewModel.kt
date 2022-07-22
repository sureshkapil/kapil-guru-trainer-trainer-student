package com.kapilguru.trainer.login.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.BuildConfig
import com.kapilguru.trainer.emailValidation
import com.kapilguru.trainer.login.AllRepo
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest
import com.kapilguru.trainer.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

open class LoginViewModel(private val loginRepository: AllRepo) : ViewModel() {

    private val TAG = "LoginViewModel"

    var userName: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()
    var resultDat: MutableLiveData<ApiResource<LoginResponse>> = MutableLiveData()
    var errorOnValidations: MutableLiveData<InValidErrors> = MutableLiveData()
    var abc: String = "sd"

    init {

        if (BuildConfig.DEVELOPER_BUILD) {
//            userName.value = "kapilgurutest@gmail.com" // raghu test credentials
//            password.value = "Kapil@123"
            userName.value = "newapp@gmail.com" //--Trainer production
            password.value = "Kapil@123"
//            userName.value = "student@g.com" //--Student staging
//            password.value = "Kapil@123"

            /*OLD*/
//            userName.value = "ritwikpradhan@kapilit.com" //--Student production
//            password.value = "Kapil@123"
//            userName.value = "ritwikpradhan@kcs-tech.com" //--Trainer
//            password.value = "Kapil@123"
//            userName.value = "nihar.goel4@gmail.com"
//            password.value = "Nihar@123"
//            userName.value = "suresh.gundaa@gmail.com" //-- Student
//            password.value = "Suresh@7055"
        }
    }

    fun onSubmitClick(v: View) {
        if (isInputValid(userName.value, password = password.value)) {
            val loginRequest = LoginUserRequest(userName.value.toString(), password.value.toString())
            loginUserApi(loginRequest)
        }
    }

    //---check - remove this debug function
    fun loginAsStudent(){
        val loginRequest = LoginUserRequest("student@g.com", "Kapil@123")
        loginUserApi(loginRequest)
    }

    open fun isInputValid(mailOrMobile: String?, password: String?): Boolean {
        arrayNotNulls(mailOrMobile, password)?.let { (mail, number) ->
            when {
                mail.trim().emailValidation() -> {
                    errorOnValidations.value = InValidErrors.EMAILINCORRECT
                    return false
                }
                password?.trim().isNullOrEmpty() -> {
                    errorOnValidations.value = InValidErrors.PASSWORDINCORRECT
                    return false
                }
                else -> return true
            }
        } ?: kotlin.run {
            errorOnValidations.value = InValidErrors.EMAILINCORRECT
            return false
        }
    }

    // used for unit testing purpose
    open fun getresultDat(): MutableLiveData<ApiResource<LoginResponse>> {
        onClickCheck()
        return resultDat
    }

    // used for unit testing purpose
    fun <T : Any> arrayNotNulls(vararg elements: T?): Array<T>? {
        if (null in elements) {
            return null
        }
        return elements as Array<T>
    }

    // used for unit testing purpose
    open fun onClickCheck() {
        var abc = "22"
    }

    open fun loginUserApi(loginRequest: LoginUserRequest) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(ApiResource.success(loginRepository.getUsers(loginRequest)))
            } catch (exception: IOException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", exception.code()))
            }
        }
    }
}

// Dont delete this as it may be used for future references
//    fun loginUser (loginRequest: LoginUserRequest) = liveData(Dispatchers.IO) {
//        Log.v("checkInfo", "checkhere_2");
//        emit(ApiResource.loading(data = null))
//        try {
//            Log.v("checkInfo", "checkhere");
//            emit(ApiResource.success(data = loginRepository.getUsers(loginRequest)))
//        } catch (exception: Exception) {
//            Log.v("checkInfo", "checkhere_22");
//            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
//        }
//    }

enum class InValidErrors {
    PASSWORDINCORRECT, EMAILINCORRECT, EMAIL_OR_MOBILE_EMPTY, MOBILE_IN_CORRECT, PASSWORD_EMPTY
}