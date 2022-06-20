package com.kapilguru.trainer.student.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityStudentProfileOverViewBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.profile.viewModel.StudentProfileOptionsFactory
import com.kapilguru.trainer.student.profile.viewModel.StudentProfileOptionsViewModel
import com.kapilguru.trainer.student.profileInfo.StudentProfileDetailsActivity
import com.kapilguru.trainer.ui.changePassword.ChangePasswordActivity
import com.kapilguru.trainer.ui.profile.OTPDialogFragment

class StudentProfileOverViewActivity : AppCompatActivity() {
    private val TAG = "ProfileOverViewActivity"
    lateinit var studentProfileOptionsViewModel: StudentProfileOptionsViewModel
    lateinit var binding: ActivityStudentProfileOverViewBinding
    lateinit var dialog: CustomProgressDialog
    var isOTPDialogLaunchedForProfile = false
    var isOTPDialogLaunchedForBankDetails = false
    var isOTPDialogLaunchedForChangePassword = false
    lateinit var changePasswordDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActivityName()
        observeViewModelData()
        getProfileData()
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        setUpdateProfileVisibility()
    }

    private fun initLateInitVariables() {
        studentProfileOptionsViewModel =
            ViewModelProvider(this, StudentProfileOptionsFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudentProfileOptionsViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_profile_over_view)
        binding.lifecycleOwner = this
        binding.viewModel = studentProfileOptionsViewModel
        dialog = CustomProgressDialog(this)
        createChangePasswordDialog()
    }

    private fun createChangePasswordDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(false).setMessage(R.string.change_password_message).setPositiveButton(R.string.request_otp) { dialog, which ->
            launchChangePasswordActivity()
            dialog?.dismiss()
        }.setNegativeButton(R.string.Cancel) { dialog, which -> dialog?.dismiss() }
        changePasswordDialog = alertDialogBuilder.create()
    }

    private fun launchChangePasswordActivity() {
        startActivity(Intent(this, ChangePasswordActivity::class.java))
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.title_profile)
    }

    fun getProfileData() {
        val pref = StorePreferences(this)
        studentProfileOptionsViewModel.getProfileData(pref.userId.toString())
    }

    fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
        binding.btnChangePassword.setOnClickListener {
            launchChangePasswordActivity()
        }
        binding.ivEditProfile.setOnClickListener {
            launchProfileInfoActivity()
        }
        binding.ivBankDetails.setOnClickListener {
            launchBankDetailsActivity()
        }
    }

    private fun setUpdateProfileVisibility() {
        if (StorePreferences(this).isProfileUpdated != 1) {
            showUpdateProfileView()
            binding.actvUpateProfile.visibility = View.VISIBLE
        } else {
            binding.actvUpateProfile.visibility = View.GONE
        }
    }

    private fun showUpdateProfileView() {
        val textView = binding.actvUpateProfile
        textView.isSingleLine = true
        textView.setHorizontallyScrolling(true)
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.marqueeRepeatLimit = -1
        textView.isSelected = true
    }

    fun observeViewModelData() {
        observeProfileData()
        observeCheckOTPResponse()
    }

    private fun observeProfileData() {
        studentProfileOptionsViewModel.profileDataResponse.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    binding.profileData = it?.data?.data?.get(0)
                    it?.data?.data?.let { it ->
                        studentProfileOptionsViewModel.studentProfileData = it[0]
                    }
                    profilePercentage()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeCheckOTPResponse() {
        studentProfileOptionsViewModel.checkOTPResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status?.equals(200)!!) {
                        hideOTPFragment()
                        when {
                            isOTPDialogLaunchedForProfile -> launchProfileInfoActivity()
                            isOTPDialogLaunchedForBankDetails -> launchBankDetailsActivity()
                            isOTPDialogLaunchedForChangePassword -> launchChangePasswordActivity()
                        }
                    } else {
                        showToast("Enter Correct OTP")
                    }
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun profilePercentage() {
        val pref = StorePreferences(this)
        pref.let {
            var percentage = 0
            if (it.isImageUpdated == 1) percentage += 10
            if (it.isBankUpdated == 1) percentage += 20
            if (it.isProfileUpdated == 1) percentage += 70

            studentProfileOptionsViewModel.profilePercentage.value = percentage
        }
    }

    private fun hideOTPFragment() {
        val fragment: Fragment? = supportFragmentManager.findFragmentByTag(OTPDialogFragment.TAG)
        if (fragment != null) {
            val df: DialogFragment = fragment as DialogFragment
            df.dismiss()
        }
    }

    private fun launchProfileInfoActivity() {
        startActivity(Intent(this, StudentProfileDetailsActivity::class.java))
    }

    private fun launchBankDetailsActivity() {
//        startActivity(Intent(this, BankDetailsActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun launchActivity(activity: Activity) {
            val intent = Intent(activity, StudentProfileOverViewActivity::class.java)
            activity.startActivity(intent)
        }
    }
}