package com.kapilguru.trainer.ui.profile.businessType

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityBusinessTypeBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.ui.profile.businessType.viewModel.BusinessTypeViewModel
import com.kapilguru.trainer.ui.profile.businessType.viewModel.BusinessTypeViewModelFactory
import com.kapilguru.trainer.ui.profile.profileInfo.viewModel.ProfileInfoViewModelFactory
import com.kapilguru.trainer.ui.profile.profileInfo.viewModel.ProfileInfoViewmodel
import com.kapilguru.trainer.ui.profile.profileOrganisation.ProfileOrganisationActivity

class BusinessTypeActivity : AppCompatActivity() {
    private val TAG = "BusinessTypeActivi"
    lateinit var viewModel : BusinessTypeViewModel
    lateinit var binding : ActivityBusinessTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_business_type)
        viewModel = ViewModelProvider(this, BusinessTypeViewModelFactory()).get(BusinessTypeViewModel::class.java)
        viewModel.profileMutLiveData.value = intent.extras?.getParcelable(ProfileOrganisationActivity.KEY_PROFILE_DATA)
        setClickListeners()
        binding.viewModel = viewModel
    }
    private fun setClickListeners(){
        binding.businessIndividual.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d(TAG,"businessIndividual : isChecked : "+isChecked)
            if(isChecked){
                viewModel.profileMutLiveData.value?.isOrganization = 0
            }else{
                viewModel.profileMutLiveData.value?.isOrganization = 1
            }
        }

        binding.tvNext.setOnClickListener {
            val intent = Intent(this, ProfileOrganisationActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(ProfileOrganisationActivity.KEY_PROFILE_DATA, viewModel.profileMutLiveData.value)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}