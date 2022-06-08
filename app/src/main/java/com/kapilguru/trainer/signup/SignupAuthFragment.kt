package com.kapilguru.trainer.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentSignupAuthBinding
import com.kapilguru.trainer.login.LoginActivity
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.showSingleButtonErrorDialog
import com.kapilguru.trainer.signup.viewModel.SignUpViewModel

class SignupAuthFragment : Fragment() {
    lateinit var binding: FragmentSignupAuthBinding
    val viewModel: SignUpViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        // API call for OTP
        viewModel.sendOtpMessage()

        viewModel.validateOtpResponse.observe(viewLifecycleOwner, Observer { validateOtpRes ->
            when (validateOtpRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    validateOtpRes.data?.let {apiInfo->
                        if(apiInfo.status ==200) {
                            launchLoginActivity()
                        } else {
                            showSingleButtonErrorDialog(requireContext(),apiInfo.message)
                        }
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    showSingleButtonErrorDialog(requireContext(),"Something went wrong please try after some time")
                }
            }
        })

        /*viewModel.sendOptSmsResponse.observe(viewLifecycleOwner, Observer {res->
            when (res.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                }
                Status.ERROR -> {
                    showSingleButtonErrorDialog(requireContext(),"something went wrong please try, after some time")
                }
            }
        })*/
    }

    private fun launchLoginActivity() {
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        activity?.finishAffinity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignupAuthBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        setText()
        addTextWatchers()
        setClickListeners()
        return binding.root
    }

    private fun setText() {
        viewModel.registerRequest.value?.contactNumber?.let {number->
            binding.tvOtpPhoneNo.text = String.format(
                getString(R.string.masked_phone_number),
                number.subSequence(number.length-4,number.length)
            )
        }
    }

    private fun addTextWatchers() {
        binding.tietOtpPhoneNo.doAfterTextChanged {
            checkAndEnableAuthButton()
        }
    }

    private fun checkAndEnableAuthButton() {
        if (viewModel.otpMobile.value?.length == 6) {
            shouldEnableAuthenticateButton(true)
        } else {
            shouldEnableAuthenticateButton(true)
        }
    }

    private fun shouldEnableAuthenticateButton(shouldEnable: Boolean) {
        binding.btnAuthenticate.isEnabled = shouldEnable
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener {
            //do nothing
        }
        binding.btnAuthenticate.setOnClickListener {
            validateOtp()
        }
        binding.llLogin.setOnClickListener {
            activity?.finish()
        }
        binding.acivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnResend.setOnClickListener {
            viewModel.sendOtpMessage()
        }
    }

    private fun validateOtp() {
        setOTPData()
        viewModel.validateOtp()
    }

    private fun setOTPData() {
        viewModel.validateOtpRequest.value?.uuid = viewModel.registerRequest.value?.uuid
        viewModel.validateOtpRequest.value?.otp = viewModel.otpMobile.value
    }
}