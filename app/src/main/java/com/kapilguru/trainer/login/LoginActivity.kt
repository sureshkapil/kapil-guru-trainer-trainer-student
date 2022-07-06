package com.kapilguru.trainer.login

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.UserRole
import com.kapilguru.trainer.allSubscription.AllSubscriptionActivity
import com.kapilguru.trainer.databinding.ActivityLoginBinding
import com.kapilguru.trainer.forgotPassword.ForgotPasswordActivity
import com.kapilguru.trainer.login.models.Data
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.viewModel.InValidErrors
import com.kapilguru.trainer.login.viewModel.LoginViewModel
import com.kapilguru.trainer.login.viewModel.LoginViewModelFactory
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.signup.SignUpActivity
import com.kapilguru.trainer.student.homeActivity.StudentHomeActivity
import com.kapilguru.trainer.ui.home.HomeActivity
import com.kapilguru.trainer.ui.otpLogin.OTPLoginActivity
import com.kapilguru.trainer.ui.profile.ProfileUtils

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    lateinit var loginViewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(LoginViewModel::class.java)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        setMovementMethods()
        setClickListeners()
        observeViewModelData()
    }

    private fun setMovementMethods() {
        binding.tvTermsAndConds.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTermsAndConds.setLinkTextColor(getColor(R.color.blue_2))
    }

    private fun setClickListeners() {
        binding.llSignUp.setOnClickListener {
            launchSignUpActivity()
        }
        binding.buttonOtpLogin.setOnClickListener {
            launchOTPLoginActivity()
        }
    }

    private fun launchSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun launchOTPLoginActivity() {
        startActivity(Intent(this, OTPLoginActivity::class.java))
    }

    private fun observeViewModelData() {
        // API Call for login observer
        loginViewModel.resultDat.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    checkAndStoreTokenToSharedPreferences(it)
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when (it.code) {
                        400 -> {
                            showErrorDialog(resources.getString(R.string.invalid_credentials))
                        }
                        422 -> {
                            showErrorDialog(resources.getString(R.string.user_not_registered))
                        }
                        208 -> {
                            showErrorDialog(resources.getString(R.string.login_exceeded))
                        }
                        else -> {
                            showErrorDialog(getString(R.string.try_again))
                        }
                    }
                }
            }
        })

        loginViewModel.errorOnValidations.observe(this, Observer {
            when (it) {
                InValidErrors.EMAIL_OR_MOBILE_EMPTY -> {
                    showErrorDialog(getString(R.string.mail_or_mobile_empty))
                }
                InValidErrors.EMAILINCORRECT -> {
                    showErrorDialog(getString(R.string.invalid_email))
                }
                InValidErrors.MOBILE_IN_CORRECT -> {
                    showErrorDialog(getString(R.string.invalid_mobile))
                }
                InValidErrors.PASSWORD_EMPTY -> {
                    showErrorDialog(getString(R.string.empty_password))
                }
                InValidErrors.PASSWORDINCORRECT -> {
                    showErrorDialog(getString(R.string.invalid_password))
                }
            }
        })
    }

    private fun checkAndStoreTokenToSharedPreferences(it: ApiResource<LoginResponse>) {
        if (it.data?.data?.userRoleId == UserRole.TRAINER.roleId()) {
            saveAndNavigateToNextScreens(UserRole.TRAINER.roleId(), it.data.data)
        } else if (it.data?.data?.userRoleId == UserRole.STUDENT.roleId()) {
            saveAndNavigateToNextScreens(UserRole.STUDENT.roleId(), it.data.data)
        } else {
            if (it.data?.status == 208) {
                showErrorDialog(getString(R.string.login_exceeded))
            } else {
                showErrorDialog("Login Credentials are incorrect")
            }
        }
    }

    private fun saveAndNavigateToNextScreens(roleId: Int, data: Data) {
        ProfileUtils.saveProfileData(this, data)
        when (roleId) {
            UserRole.TRAINER.roleId() -> {
                navigateToTrainerNextScreens(data.isSubscribed)
            }
            UserRole.STUDENT.roleId() -> {
                navigateToStudentHomeScreen()
            }
        }
    }

    private fun navigateToStudentHomeScreen() {
        startActivity(Intent(this, StudentHomeActivity::class.java))
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok) { _, _ ->
                    setCancelable(true)
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun navigateToTrainerNextScreens(isSubscribed: Int) {
        if (isSubscribed == 1) {
            navigateToHomeActivity()
        } else {
            launchSubscriptionsActivity()
        }
    }

    private fun navigateToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }

    private fun launchSubscriptionsActivity() {
        startActivity(Intent(this, AllSubscriptionActivity::class.java))
        finishAffinity()
    }

    fun onForgotPasswordClicked(view: View) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }
}