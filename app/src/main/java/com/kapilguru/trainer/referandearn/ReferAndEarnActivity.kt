package com.kapilguru.trainer.referandearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityReferAndEarnBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class ReferAndEarnActivity : BaseActivity() {
    private val TAG = "ReferAndEarnActivity"
    lateinit var viewModel: ReferAndEarnViewModel
    lateinit var binding: ActivityReferAndEarnBinding
    lateinit var dialog: CustomProgressDialog
    var canRefer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBarListener()
        observeViewModelData()
        setClickListeners()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refer_and_earn)
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, ReferAndEarnViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(ReferAndEarnViewModel::class.java)
        binding.vieModel = viewModel
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.refer_and_earn))
    }

    private fun observeViewModelData() {
        observeReferResponse()
    }

    private fun observeReferResponse() {
        viewModel.resultDat.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    showAppToast(this, "New Referral Sent Successfully")
                    finish()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setClickListeners() {
        binding.btnReferNow.setOnClickListener {
            onReferNowClicked()
        }
    }

    fun onReferNowClicked() {
        val mail = viewModel.inviteeEmail.value.toString()
        val mobile = viewModel.inviteeContactNumber.value.toString()
        when {
            binding.referTypeSpinner.selectedItemPosition == 0 -> {
                showAppToast(this, "Please select Referral Type")
            }
            TextUtils.isEmpty(mail) || TextUtils.equals(mail, "null") -> {
                showAppToast(this, "Please enter Email Id")
                return
            }
            viewModel.inviteeEmail.value?.trim()!!.emailValidation() -> {
                showAppToast(this, "Please enter valid Email Id")
                return
            }
            TextUtils.isEmpty(mobile) || TextUtils.equals(mobile, "null") -> {
                showAppToast(this, "Please enter Contact Number")
                return
            }
            viewModel.inviteeContactNumber.value.toString().trim().length < 10 -> {
                showAppToast(this, "Please enter valid Phone Number")
            }
            else -> {
                val pref = StorePreferences(application)
                viewModel.requestApiCall(pref.trainerId, binding.referTypeSpinner.selectedItem.toString().toLowerCase())
            }
        }
    }

    companion object {

        fun launchActivity(activity: Activity) {
            val intent = Intent(activity, ReferAndEarnActivity::class.java)
            activity.startActivity(intent)
        }
    }
}