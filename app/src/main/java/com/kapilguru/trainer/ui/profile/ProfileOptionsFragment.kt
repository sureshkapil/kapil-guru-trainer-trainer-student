package com.kapilguru.trainer.ui.profile

import android.app.AlertDialog
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.allSubscription.AllSubscriptionActivity
import com.kapilguru.trainer.databinding.FragmentProfileOptionsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.changePassword.ChangePasswordActivity
import com.kapilguru.trainer.ui.profile.bank.BankDetailsActivity
import com.kapilguru.trainer.ui.profile.profileInfo.view.ProfileDetailsActivity
import com.kapilguru.trainer.ui.profile.profileInfo.view.ProfileDetailsActivityNew
import com.kapilguru.trainer.ui.profile.viewModel.ProfileOptionsFactory
import com.kapilguru.trainer.ui.profile.viewModel.ProfileOptionsViewModel
import android.app.AlertDialog.Builder as AlertDialogBuilder

class ProfileOptionsFragment : Fragment() {
    val TAG = "ProfileOptionsFragment"
    private lateinit var profileOptionsViewModel: ProfileOptionsViewModel
    private lateinit var profileOptionsBinding: FragmentProfileOptionsBinding
    lateinit var dialog: CustomProgressDialog
    var isProfileUpdated: Boolean = false
    private var isOTPDialogLaunchedForProfile = false
    private var isOTPDialogLaunchedForBankDetails = false
    private var isOTPDialogLaunchedForChangePassword = false
    lateinit var changePasswordDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileOptionsViewModel = ViewModelProvider(this, ProfileOptionsFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(ProfileOptionsViewModel::class.java)
        dialog = CustomProgressDialog(requireContext())
        createChangePasswordDialog()
        observeViewModelData()
        getProfileData()
    }

    private fun createChangePasswordDialog() {
        val alertDialogBuilder = AlertDialogBuilder(requireContext())
        alertDialogBuilder.setCancelable(false).setMessage(R.string.change_password_message).setPositiveButton(R.string.request_otp) { dialog, which ->
                launchChangePasswordActivity()
                dialog?.dismiss()
            }.setNegativeButton(R.string.Cancel) { dialog, which -> dialog?.dismiss() }
        changePasswordDialog = alertDialogBuilder.create()
    }

    private fun launchChangePasswordActivity() {
        startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
    }

    fun getProfileData() {
        val pref = StorePreferences(requireContext())
        profileOptionsViewModel.getProfileData(pref.userId.toString())
    }

    fun observeViewModelData() {
        observeProfileData()
        observeCheckOTPResponce()
    }

    private fun observeProfileData() {
        profileOptionsViewModel.profileDataResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    profileOptionsBinding.profileData = it?.data?.data?.get(0)
                    it?.data?.data?.let { it ->
                       /* val image = it[0].code + PNG // adding image as image is getting as null
                        val profileData = it[0]
                        profileData.image = image
                        profileOptionsViewModel.profileData = profileData*/
                        profileOptionsViewModel.profileData = it[0]
                    }
                    profilePercentage()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }

        })
    }

    private fun observeCheckOTPResponce() {
        profileOptionsViewModel.checkOTPResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status?.equals(200)!!) {
                        hideOTPFragment()
                        if (isOTPDialogLaunchedForProfile) {
                            launchProfileInfoActivity()
                        } else if (isOTPDialogLaunchedForBankDetails) {
                            launchBankDetailsActivity()
                        } else if (isOTPDialogLaunchedForChangePassword) {
                            launchChangePasswordActivity()
                        }
                    } else {
                        showToast("Enter Correct OTP")
                    }
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        profileOptionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_options, container, false)
        profileOptionsBinding.lifecycleOwner = viewLifecycleOwner
        profileOptionsBinding.viewModel = profileOptionsViewModel
        setClickListeners()
        return profileOptionsBinding.root
    }

    override fun onResume() {
        super.onResume()
        setUpdateProfileVisibility()
        setSubscriptionsEnable()
    }

    private fun setSubscriptionsEnable() {
        profileOptionsBinding.ivSubscriptions.isEnabled = StorePreferences(requireContext()).isProfileUpdated == 1
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

    //Just Testing
    private fun setProfileImage() {
        val bitmap = profileOptionsViewModel.profileDataResponse.value?.data?.data?.get(0)?.image?.toBitMapWithPadding()
        if (bitmap != null) {
            profileOptionsBinding.ivProfile.setImageBitmap(bitmap)
        }
    }

    fun setClickListeners() {
        profileOptionsBinding.btnChangePassword.setOnClickListener {
//            launchProfileInfoActivity()
            setOTPDialogLaunchedFor(false, false, true)
            showOTPFragment()
        }
        profileOptionsBinding.ivEditProfile.setOnClickListener {
            launchProfileInfoActivity() // ---check comment
//            setOTPDialogLaunchedFor(true, false, false) // ---check uncomment
//            showOTPFragment()
        }
        profileOptionsBinding.ivSubscriptions.setOnClickListener {
            startActivity(Intent(requireContext(), AllSubscriptionActivity::class.java))
        }
        profileOptionsBinding.ivBankDetails.setOnClickListener {
            setOTPDialogLaunchedFor(false, true, false)
            showOTPFragment()
        }
        profileOptionsBinding.profileCard.setOnClickListener {
            setOTPDialogLaunchedFor(true, false, false)
            showOTPFragment()
        }
        profileOptionsBinding.bankCard.setOnClickListener {
            setOTPDialogLaunchedFor(false, true, false)
            showOTPFragment()
        }
        profileOptionsBinding.changePasswordCard.setOnClickListener {
//            showChangePasswordDialog()
            setOTPDialogLaunchedFor(false, false, true)
            showOTPFragment()
        }
    }

    private fun setOTPDialogLaunchedFor(isForProfile: Boolean, isForBank: Boolean, isForChangePassword: Boolean) {
        isOTPDialogLaunchedForProfile = isForProfile
        isOTPDialogLaunchedForBankDetails = isForBank
        isOTPDialogLaunchedForChangePassword = isForChangePassword
    }

    private fun showOTPFragment() {
        OTPDialogFragment.newInstance(profileData = profileOptionsViewModel.profileData).show(childFragmentManager, OTPDialogFragment.TAG)
    }

    private fun hideOTPFragment() {
        val fragment: Fragment? = childFragmentManager.findFragmentByTag(OTPDialogFragment.TAG)
        if (fragment != null) {
            val df: DialogFragment = fragment as DialogFragment
            df.dismiss()
        }
    }

    private fun launchProfileInfoActivity() {
       /* val intent = Intent(requireContext(), ProfileDetailsActivity::class.java)
        val bundle = Bundle()
        val profileData = profileOptionsViewModel.profileDataResponse.value?.data?.data?.get(0)
        bundle.putParcelable(ProfileDetailsActivity.KEY_PROFILE_DATA, profileData)
        intent.putExtras(bundle)
        startActivity(intent)*/

        val intent = Intent(requireContext(), ProfileDetailsActivityNew::class.java)
        val bundle = Bundle()
        val profileData = profileOptionsViewModel.profileDataResponse.value?.data?.data?.get(0)
        bundle.putParcelable(ProfileDetailsActivity.KEY_PROFILE_DATA, profileData)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun launchBankDetailsActivity() {
        startActivity(Intent(requireContext(), BankDetailsActivity::class.java))
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

            profileOptionsViewModel.profilePercentage.value = percentage
        }
    }
}