package com.kapilguru.trainer.ui.profile.profileInfo.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityProfileDetailsNewBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.profile.profileInfo.viewModel.ProfileInfoViewModelFactory
import com.kapilguru.trainer.ui.profile.profileInfo.viewModel.ProfileInfoViewmodel

class ProfileDetailsActivityNew : BaseActivity() {
    private val TAG = "ProfileDetailsActivity2"

    lateinit var binding: ActivityProfileDetailsNewBinding
    lateinit var viewModel: ProfileInfoViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInnitVariables()
        setCustomActionBarListener()
        showProfileFragment()
    }

    private fun initLateInnitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_details_new)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, ProfileInfoViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(ProfileInfoViewmodel::class.java)
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.profile))
    }

    private fun showProfileFragment() {
        if (StorePreferences(this).isOrganization == 1) {
            showFragment(ProfileOrganisationFragment())
        } else {
            showFragment(ProfileIndividualFragment())
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.fl_profile, fragment).commit()
    }
}