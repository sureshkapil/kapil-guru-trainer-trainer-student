package com.kapilguru.trainer.forgotPassword

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentGetOtpBinding
import com.kapilguru.trainer.forgotPassword.viewModel.ForgotPasswordViewModel
import com.kapilguru.trainer.network.Status

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GetOtpFragment : Fragment() {
    val TAG = "GetOtpFragment"

    val viewModel : ForgotPasswordViewModel by activityViewModels()
    lateinit var binding : FragmentGetOtpBinding
    lateinit var dialog : CustomProgressDialog

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dialog = CustomProgressDialog(requireContext())
        observeViewModelData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreateView")
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_get_otp,container,false)
        binding.viewModel = viewModel
        setClickListerns()
        return binding.root
    }


    private fun observeViewModelData(){
        viewModel.validateMobileResMutLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING ->{
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS ->{
                    dialog.dismissLoadingDialog()
                    if(it.data?.data?.contact_count == 1){
                        viewModel.getOTP()
                    }else{
                        viewModel.errorDescription.postValue("Mobile number not registered")
                    }
                }

                Status.ERROR ->{
                    dialog.dismissLoadingDialog()
                }
            }
        })
        viewModel.getOTPResponseMutLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS ->{
                    dialog.dismissLoadingDialog()
                    launchValidateOTPFragment()
                }

                Status.ERROR ->{
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun launchValidateOTPFragment(){
        val fragment = parentFragmentManager.findFragmentByTag(ValidateOtpFragment.FragTAG)
        if(fragment != null){
            return
        }
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit)
            .add(R.id.fl_forgot_password,ValidateOtpFragment.noInstance(), ValidateOtpFragment.FragTAG)
            .addToBackStack(null).commit()
    }

    private fun setClickListerns() {
        binding.getOtp.setOnClickListener {
            viewModel.validateMobile()
        }

        binding.rGroupType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rButtonMobile) {
                resetInputEditType(true)
            } else {
                resetInputEditType(false)
            }
        }

        binding.acivBack.setOnClickListener {
            activity?.finish()
        }
    }


    private fun resetInputEditType(isMobile: Boolean) {
        binding.etPhoneNo.inputType = if (isMobile) {
            binding.etPhoneNo.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
            binding.etPhoneNo.setText("")
            binding.etPhoneNo.setHint("Mobile Number")
            viewModel.isMobileSelected = true
            binding.mobileWarningText.visibility = View.VISIBLE
            InputType.TYPE_CLASS_NUMBER
        } else {
            binding.etPhoneNo.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(75))
            binding.etPhoneNo.setText("")
            binding.etPhoneNo.setHint("Email")
            binding.mobileWarningText.visibility = View.GONE
            viewModel.isMobileSelected = false
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
    }
    

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GetOtpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun noInstance() = GetOtpFragment()
    }
}