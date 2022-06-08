package com.kapilguru.trainer.ui.otpLogin

import android.app.AlertDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentRequestOTPBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.signup.SignUpActivity
import com.kapilguru.trainer.ui.otpLogin.viewModel.OTPLoginViewModel
import kotlinx.android.synthetic.main.fragment_request_o_t_p.*

class RequestOTPFragment : Fragment() {
    private val TAG = "RequestOTPFragment"
    lateinit var binding: FragmentRequestOTPBinding
    val viewModel: OTPLoginViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentRequestOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        observeViewModelData()
        addTextWatchers()
        setClickListeners()
    }

    private fun  observeViewModelData(){
        observeOtpLoginValidateResponse()
    }

    private fun observeOtpLoginValidateResponse() {
        viewModel.otpLoginValidateResponse.observe(this.viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    response.data?.let { otpLoginValidRes ->
                        val result = otpLoginValidRes.otpLoginValidateResData?.get(0)?.result
                        if (result == 1) {
                            showAppToast(requireContext(),"OTP sent successfully")
                            launchVerifyOTPFragment()
                        }else{
                            showNotRegisteredDialog()
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

    private fun showNotRegisteredDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Register Now")
            .setMessage("You are not registered with us. Please create account to continue login")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, which -> launchSignUpActivity() }
            .create()
        alertDialog.show()
    }

    private fun launchSignUpActivity() {
        val intent = Intent(requireContext(), SignUpActivity::class.java)
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    private fun observeValidateMailResponse() {
        viewModel.validateMailResponse.observe(viewLifecycleOwner, Observer { validateMailResponse ->
            when (validateMailResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    if (validateMailResponse.data?.validateMailData?.emailCount == 0) {
                        showAppToast(requireContext(), "Email ID is not registered")
                    } else if (validateMailResponse.data?.validateMailData?.emailCount!! == 1) {
                        requestOTP()
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeValidateMobileResponse() {
        viewModel.validateMobileResponse.observe(viewLifecycleOwner, Observer { validateMobileResponse ->
            when (validateMobileResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    if (validateMobileResponse.data?.data?.contact_count == 0) {
                        showAppToast(requireContext(), "Phone NO is not registered")
                    } else if (validateMobileResponse.data?.data?.contact_count!! == 1) {
                        requestOTP()
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun requestOTP() {
        viewModel.requestOTP()
    }

    private fun addTextWatchers() {
        binding.etOtpMail.doAfterTextChanged {
            it?.let {
                if (it.toString().isValidMobileNo() || !it.toString().emailValidation()) {
                    shouldEnableRequestOTP(true)
                } else {
                    shouldEnableRequestOTP(false)
                }
            }
        }
    }

    private fun shouldEnableRequestOTP(shouldEnable: Boolean) {
        binding.btnRequestOtp.isEnabled = shouldEnable
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener {
            // do nothing consume click
        }
        binding.btnRequestOtp.setOnClickListener {
           /* validateInput()*/
            checkInputAndRequestOTP()
        }
        binding.llLogin.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.acivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.rGroupType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rButtonMobile) {
                   resetInputEditType(true)
            } else {
                resetInputEditType(false)
            }
        }
    }

    private fun resetInputEditType(isMobile: Boolean) {
        binding.etOtpMail.inputType = if (isMobile) {
            binding.etOtpMail.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
            binding.etOtpMail.setText("")
            binding.etOtpMail.hint = resources.getString(R.string.enter_registered_phone_no)
            binding.mobileWarningText.visibility = View.VISIBLE
            InputType.TYPE_CLASS_NUMBER
        } else {
            binding.etOtpMail.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(75))
            binding.etOtpMail.setText("")
            binding.etOtpMail.hint = resources.getString(R.string.enter_registered_email)
            binding.mobileWarningText.visibility = View.GONE
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
    }

    private fun checkInputAndRequestOTP(){
        val mobileNo = binding.etOtpMail.text.toString()
        when{
            binding.rButtonEmail.isChecked ->
                if(!mobileNo.emailValidation()){
                    requestOTP()
                }    else {
                    showAppToast(requireContext(),"Please enter correct Email Id")
                }
            binding.rButtonMobile.isChecked ->
                if(mobileNo.isValidMobileNo()){
                    requestOTP()
                }else{
                    showAppToast(requireContext(),"Please enter correct Mobile No")
                }
        }
    }

   /* private fun validateInput() {
        val input = binding.etOtpMail.text.toString()
        if (input.emailValidation()) { //  false, if email is valid
            if (input.isValidMobileNo()) { // true, if mobile no is valid
                validateMobile(input)
            } else {
                showAppToast(requireContext(), "Enter Valid Email ID /Phone No.")
            }
        } else {
            validateMail(input)
        }
    }

    private fun validateMobile(mobileNo: String) {
        viewModel.validateMobile(ValidateMobileRequest(mobileNo))
    }

    private fun validateMail(mail: String) {
        viewModel.validateMail(ValidateMailRequest(mail))
    }*/

    private fun launchVerifyOTPFragment() {
        (activity as OTPLoginActivity).replaceFragment(VerifyOTPFragment())
    }
}