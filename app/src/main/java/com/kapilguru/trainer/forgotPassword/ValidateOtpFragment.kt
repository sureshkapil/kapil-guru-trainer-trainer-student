package com.kapilguru.trainer.forgotPassword

import android.os.Bundle
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
import com.kapilguru.trainer.databinding.FragmentValidateOtpBinding
import com.kapilguru.trainer.forgotPassword.viewModel.ForgotPasswordViewModel
import com.kapilguru.trainer.network.Status

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ValidateOtpFragment : Fragment() {
    private val TAG = "ValidateOtpFragment"

    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FragmentValidateOtpBinding
    val viewModel: ForgotPasswordViewModel by activityViewModels()
    lateinit var dialog : CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_validate_otp,container,false)
        binding.viewModel = viewModel
        setOtpDesc()
        setClickListeners()
        return binding.root
    }

    private fun observeViewModelData(){
        viewModel.checkOTPResponseMutLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS->{
                    dialog.dismissLoadingDialog()
                    if(it.data?.status == 200){
                        launchCreatePasswordFragment()
                    }else{
                        Log.d(TAG,"error message : "+it.data?.message)
                        viewModel.errorDescription.postValue(it.data?.message)
                    }
                }

                Status.ERROR->{
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setOtpDesc(){
        val desc = getString(R.string.please_enter_otp).format(viewModel.contactNo.value)
        binding.tvEnterOtpDesc.setText(desc)
    }

    private fun launchCreatePasswordFragment(){
        val validateFragment = parentFragmentManager.findFragmentByTag(FragTAG)
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit)
            .remove(validateFragment!!)
            .commit()
        parentFragmentManager.popBackStack()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit)
            .add(R.id.fl_forgot_password,CreatePasswordFragment.noInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun setClickListeners(){
        binding.root.setOnClickListener{
            //do nothing Inorder to consume the click
        }
        binding.btnValidateOtp.setOnClickListener {
            viewModel.checkOtp()
        }
        binding.tvResendOtp.setOnClickListener{
            viewModel.getOTP()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ValidateOtpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun noInstance() = ValidateOtpFragment()

        val FragTAG = "validateOtpFragment"
    }
}