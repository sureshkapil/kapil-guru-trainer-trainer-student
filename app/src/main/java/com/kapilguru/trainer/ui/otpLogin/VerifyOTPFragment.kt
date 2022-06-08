package com.kapilguru.trainer.ui.otpLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentVerifyOTPBinding
import com.kapilguru.trainer.login.models.Data
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.showAppToast
import com.kapilguru.trainer.ui.home.HomeActivity
import com.kapilguru.trainer.ui.otpLogin.viewModel.OTPLoginViewModel
import com.kapilguru.trainer.ui.profile.ProfileUtils

class VerifyOTPFragment : Fragment() {

    lateinit var binding: FragmentVerifyOTPBinding
    val viewModel: OTPLoginViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVerifyOTPBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        setDoAfterTextChangeListener()
        setClickListeners()
        return binding.root
    }

    private fun observeViewModelData() {
        observeResendOTPResponse()
        observeOtpLoginResponse()
    }

    private fun observeResendOTPResponse() {
        viewModel.otpLoginValidateResponse.observe(this.viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    response.data?.let { otpLoginValidRes ->
                        val result = otpLoginValidRes.otpLoginValidateResData?.get(0)?.result
                        if (result == 1) {
                            showAppToast(requireContext(), getString(R.string.otp_resent))
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeOtpLoginResponse() {
        viewModel.otpLoginResponse.observe(viewLifecycleOwner, Observer { otpLoginResponse ->
            when (otpLoginResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    otpLoginResponse.data?.let { loginResponse ->
                        loginResponse.data?.let { loginResData ->
                            storeTokenToSharedPreferences(loginResData)
                            progressDialog.dismissLoadingDialog()
                        }
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun storeTokenToSharedPreferences(loginResponse: Data) {
        ProfileUtils.saveProfileData(requireContext(),loginResponse)
        navigateToHomeActivity()
    }

    private fun navigateToHomeActivity() {
        activity?.startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finishAffinity()
    }

    private fun setDoAfterTextChangeListener() {
        binding.tietOtp.doAfterTextChanged { otpOrNull ->
            otpOrNull?.let { otp ->
                shouldEnableProceedAndVerify(otp.length == 6)
            }
        }
    }

    private fun shouldEnableProceedAndVerify(shouldEnable: Boolean) {
        binding.btnVerifyAndProceed.isEnabled = shouldEnable
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener {
            //do nothing consume click
        }
        binding.btnVerifyAndProceed.setOnClickListener {
            viewModel.sendOTPLogin()
        }
        binding.actvResendOtp.setOnClickListener {
            viewModel.requestOTP()
        }
        binding.llLogin.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}