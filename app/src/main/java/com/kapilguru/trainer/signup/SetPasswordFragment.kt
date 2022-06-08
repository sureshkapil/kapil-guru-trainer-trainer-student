package com.kapilguru.trainer.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentSetPasswordBinding
import com.kapilguru.trainer.isStrongPassword
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.signup.viewModel.SignUpViewModel

class SetPasswordFragment : Fragment() {
    private val TAG = "SetPasswordFragment"
    lateinit var binding: FragmentSetPasswordBinding
    lateinit var progressDialog: CustomProgressDialog
    val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetPasswordBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        setClickListeners()
        setDoAfterTextChangeListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeViewModelData() {
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer { registerApiResponse ->
            when (registerApiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    launchAuthenticateScreen()
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener {
            // do nothing
        }
        binding.btnConfirmPassword.setOnClickListener {
            val password = viewModel.enterPassword.value ?: ""
            if (password.isStrongPassword(requireContext())) {
                viewModel.register()
            }
        }
        binding.llLogin.setOnClickListener {
            activity?.finish()
        }
        binding.acivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun launchAuthenticateScreen() {
        (activity as SignUpActivity).launchFragment(SignupAuthFragment())
    }

    private fun setDoAfterTextChangeListeners() {
        binding.tietCreatePassword.doAfterTextChanged { passwordOrNull ->
            passwordOrNull?.let { password ->
                binding.tietConfirmPassword.isEnabled = password.length > 5
                checkAndEnableConfirmButton()
            }
        }
        binding.tietConfirmPassword.doAfterTextChanged {
            checkAndEnableConfirmButton()
        }
    }

    private fun checkAndEnableConfirmButton() {
        if (TextUtils.isEmpty(viewModel.enterPassword.value) || TextUtils.isEmpty(viewModel.confirmPassword.value)) {
            shouldEnableSetPassword(false)
            return
        }
        viewModel.enterPassword.value?.let { enteredPassword ->
            if (enteredPassword.length > 5 && TextUtils.equals(enteredPassword, viewModel.confirmPassword.value)) {
                shouldEnableSetPassword(true)
            } else {
                shouldEnableSetPassword(false)
            }
        }
    }

    private fun shouldEnableSetPassword(shouldEnable: Boolean) {
        binding.btnConfirmPassword.isEnabled = shouldEnable
    }
}