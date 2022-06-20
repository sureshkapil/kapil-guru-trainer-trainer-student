package com.kapilguru.trainer.student.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentStudentProfileOptionsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.DialogFragmentCustom
import com.kapilguru.trainer.student.homeActivity.StudentHomeActivity
import com.kapilguru.trainer.student.profile.viewModel.StudentProfileOptionsFactory
import com.kapilguru.trainer.student.profile.viewModel.StudentProfileOptionsViewModel
import com.kapilguru.trainer.student.profileInfo.StudentProfileDetailsActivity
import com.kapilguru.trainer.ui.changePassword.ChangePasswordActivity
import com.kapilguru.trainer.ui.profile.OTPDialogFragment
import android.app.AlertDialog.Builder as AlertDialogBuilder

class StudentProfileOptionsFragment : Fragment() {
    val TAG = "ProfileOptionsFragment"
    lateinit var studentProfileOptionsViewModel: StudentProfileOptionsViewModel
    lateinit var profileOptionsBinding: FragmentStudentProfileOptionsBinding
    lateinit var dialog: CustomProgressDialog
    lateinit var dialogFragmentCustom: DialogFragmentCustom

    //    var isProfileUpdated: Boolean = false
    var isOTPDialogLaunchedForProfile = false
    var isOTPDialogLaunchedForBankDetails = false
    var isOTPDialogLaunchedForChangePassword = false
    lateinit var changePasswordDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        studentProfileOptionsViewModel =
            ViewModelProvider(this, StudentProfileOptionsFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudentProfileOptionsViewModel::class.java)
        dialog = CustomProgressDialog(requireContext())
//        dialogFragmentCustom = DialogFragmentCustom()
        createChangePasswordDialog()
        observeViewModelData()
        getProfileData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shouldShowActionBarSearch(true)
//        ((activity) as HomeActivity).fetchLatestNotification()
    }

    private fun shouldShowActionBarSearch(shouldShowActionSearchBar: Boolean) {
        ((activity) as StudentHomeActivity).shouldShowSearchInActionBar(shouldShowActionSearchBar)
    }

    private fun createChangePasswordDialog() {
        val alertDialogBuilder = AlertDialogBuilder(requireContext())
        alertDialogBuilder.setCancelable(false).setMessage(R.string.change_password_message).setPositiveButton(R.string.request_otp, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    launchChangePasswordActivity()
                    dialog?.dismiss()
                }
            }).setNegativeButton(R.string.Cancel, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
        changePasswordDialog = alertDialogBuilder.create()
    }

    private fun launchChangePasswordActivity() {
        startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
    }

    fun getProfileData() {
        val pref = StorePreferences(requireContext())
        studentProfileOptionsViewModel.getProfileData(pref.userId.toString())
    }

    fun observeViewModelData() {
        observeProfileData()
        observeCheckOTPResponce()
    }

    private fun observeProfileData() {
        studentProfileOptionsViewModel.profileDataResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
//                    showDialogFragment()
                }
                Status.SUCCESS -> {
//                    dismissDialogFragment()
                    dialog.dismissLoadingDialog()
                    profileOptionsBinding.profileData = it?.data?.data?.get(0)
                    it?.data?.data?.let { it ->
                        studentProfileOptionsViewModel.studentProfileData = it[0]
                    }
                    profilePercentage()
//                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
//                    dismissDialogFragment()
                }
            }

        })
    }

    private fun observeCheckOTPResponce() {
        studentProfileOptionsViewModel.checkOTPResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
//                    showDialogFragment()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
//                    dismissDialogFragment()
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
//                    dismissDialogFragment()
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(requireContext())
                        }
                    }
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        profileOptionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_profile_options, container, false)
        profileOptionsBinding.lifecycleOwner = viewLifecycleOwner
        profileOptionsBinding.viewModel = studentProfileOptionsViewModel
        setClickListeners()
        return profileOptionsBinding.root
    }

    override fun onResume() {
        super.onResume()
//        setUpdateProfileVisibility()
    }

    private fun setUpdateProfileVisibility() {
        if (StorePreferences(requireContext()).isProfileUpdated != 1) {
            showUpdateProfileView()
            profileOptionsBinding.actvUpateProfile.visibility = View.VISIBLE
        } else {
            profileOptionsBinding.actvUpateProfile.visibility = View.GONE
        }
    }

    private fun showUpdateProfileView() {
        val textView = profileOptionsBinding.actvUpateProfile
        textView.isSingleLine = true
        textView.setHorizontallyScrolling(true)
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.marqueeRepeatLimit = -1
        textView.isSelected = true
    }

    fun setClickListeners() {
        profileOptionsBinding.btnChangePassword.setOnClickListener {
            launchChangePasswordActivity()
//            setOTPDialogLaunchedFor(false, false, true)
//            showOTPFragment()
        }
        profileOptionsBinding.ivEditProfile.setOnClickListener {
            launchProfileInfoActivity()
//            setOTPDialogLaunchedFor(true, false, false)
//            showOTPFragment()
        }
        profileOptionsBinding.ivBankDetails.setOnClickListener {
            launchBankDetailsActivity()
//            setOTPDialogLaunchedFor(false, true, false)
//            showOTPFragment()
        }
    }

    private fun setOTPDialogLaunchedFor(isForProfile: Boolean, isForBank: Boolean, isForChangePassword: Boolean) {
        isOTPDialogLaunchedForProfile = isForProfile
        isOTPDialogLaunchedForBankDetails = isForBank
        isOTPDialogLaunchedForChangePassword = isForChangePassword
    }

    private fun showOTPFragment() {
//        OTPDialogFragment.newInstance(studentProfileData = studentProfileOptionsViewModel.studentProfileData).show(childFragmentManager, OTPDialogFragment.TAG)
    }

    private fun hideOTPFragment() {
        val fragment: Fragment? = childFragmentManager.findFragmentByTag(OTPDialogFragment.TAG)
        if (fragment != null) {
            val df: DialogFragment = fragment as DialogFragment
            df.dismiss()
        }
    }

    private fun launchProfileInfoActivity() {
        startActivity(Intent(requireContext(), StudentProfileDetailsActivity::class.java))
    }

    private fun launchBankDetailsActivity() {
//        startActivity(Intent(requireContext(), BankDetailsActivity::class.java))
    }

    private fun showChangePasswordDialog() {
        changePasswordDialog.let { dialog ->
            dialog.show()
        }
    }

    private fun profilePercentage() {
        val pref = context?.let { StorePreferences(it) }
        pref?.let {
            var percentage = 0
            if (it.isImageUpdated == 1) percentage += 10
            if (it.isBankUpdated == 1) percentage += 20
            if (it.isProfileUpdated == 1) percentage += 70

            studentProfileOptionsViewModel.profilePercentage.value = percentage
        }
    }

    fun showDialogFragment() {
        activity?.let { it ->
            val abc: Fragment? = activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG_PROFILE)
            abc?.let {

            } ?: kotlin.run {
                val fm: FragmentManager = it.supportFragmentManager
//                if (activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG_PROFILE) == null)
                dialogFragmentCustom.show(fm, DIALOG_FRAGMENT_TAG_PROFILE)
            }
        }
    }

    fun dismissDialogFragment() {
        val fm: Fragment? = activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG_PROFILE)
        fm?.let { it ->
            val abc = it as DialogFragmentCustom
            abc.dismiss()
//            Log.d(TAG, "dismissDialogFragment: DISSMISS")
        } ?: run {
//            Log.d(TAG, "dismissDialogFragment: whattt")
            activity?.supportFragmentManager?.executePendingTransactions()
        }

    }

}