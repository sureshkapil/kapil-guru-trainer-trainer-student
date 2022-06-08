package com.kapilguru.trainer.ui.changePassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityChangePasswordBinding
import com.kapilguru.trainer.login.LoginActivity
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.changePassword.viewModel.ChangePasswordViewModel
import com.kapilguru.trainer.ui.changePassword.viewModel.ChangePasswordViewModelFactory

class ChangePasswordActivity : AppCompatActivity() {
    private val TAG = "ChangePasswordActivity"
    lateinit var viewModel: ChangePasswordViewModel
    lateinit var binding: ActivityChangePasswordBinding
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(
            this,
            ChangePasswordViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),
                application
            )
        )
            .get(ChangePasswordViewModel::class.java)
        binding.viewModel = viewModel
        setDoAfterTextChanged()
        setClickListeners()
        observeViewModel()
    }

    private fun setDoAfterTextChanged() {
        binding.tietPassword.doAfterTextChanged { password ->
            password?.let { text ->
                shouldEnableConfirmPasswordEditText(text.length > 5)
            }
        }
        binding.tietConfirmPassword.doAfterTextChanged { confirmPassword ->
            if (TextUtils.equals(
                    confirmPassword,
                    viewModel.changePasswordRequest.value?.password
                )
            ) {
                shouldEnableChangePasswordBtn(true)
            } else {
                shouldEnableChangePasswordBtn(false)
            }
        }
    }

    private fun shouldEnableConfirmPasswordEditText(shouldEnable: Boolean) {
        binding.tilConfirmPassword.isEnabled = shouldEnable
    }

    private fun shouldEnableChangePasswordBtn(shouldEnable: Boolean) {
        binding.btnConfirmPassword.isEnabled = shouldEnable
    }

    private fun setClickListeners() {
        binding.btnConfirmPassword.setOnClickListener {
            viewModel.changePasswordRequest.value?.contactNo = StorePreferences(this).contactNumber
            viewModel.changePassword()
        }
        binding.btnCancel.setOnClickListener {
            binding.tietConfirmPassword.setText("")
            binding.tietPassword.setText("")
        }
    }

    private fun observeViewModel() {
        observeChangePasswordResponse()
        observeLogoutResponse()
    }

    private fun observeChangePasswordResponse() {
        viewModel.changePasswordResponse.observe(this, Observer { changePasswordResponse ->
            when (changePasswordResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    callLogoutApi()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }
        })
    }

    private fun callLogoutApi() {
        viewModel.logoutUser()
    }

    private fun observeLogoutResponse() {
        viewModel.logoutResponse.observe(this, Observer { logoutResponse ->
            when (logoutResponse.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    logoutUser()
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun logoutUser(){
        StorePreferences(this).clearStorePreferences()
        startActivity(Intent(this,LoginActivity::class.java))
        finishAffinity()
    }
}