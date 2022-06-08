package com.kapilguru.trainer.referandearn.myReferrals

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityMyReferalsListBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.referandearn.myReferrals.model.MyReferralResData
import com.kapilguru.trainer.referandearn.myReferrals.viewModel.MyReferralViewModel
import com.kapilguru.trainer.referandearn.myReferrals.viewModel.MyReferralViewModelFactory

class MyReferralsListActivity : BaseActivity() {
    lateinit var binding: ActivityMyReferalsListBinding
    lateinit var viewModel: MyReferralViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: MyReferralsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBarListener()
        observeViewModelData()
        getMyReferrals()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_referals_list)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, MyReferralViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(MyReferralViewModel::class.java)
        progressDialog = CustomProgressDialog(this)
        adapter = MyReferralsListAdapter()
        binding.rvMyReferrals.adapter = adapter
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.my_referrals))
    }

    private fun observeViewModelData() {
        viewModel.myReferralsApiResponse.observe(this, Observer { myRefApiRes ->
            when (myRefApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    myRefApiRes.data?.myReferralsList?.let { myReferralsList ->
                        checkAndSetAdapterData(myReferralsList)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.showLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetAdapterData(myReferralsList: ArrayList<MyReferralResData>) {
        if (myReferralsList.isEmpty()) {
            binding.actvNoMyReferrals.visibility = View.VISIBLE
            binding.rvMyReferrals.visibility = View.GONE
            binding.actvInfo.visibility = View.GONE
        } else {
            adapter.setData(myReferralsList)
        }
    }

    private fun getMyReferrals() {
        val trainerId = StorePreferences(this).userId.toString()
        viewModel.getMyReferrals(trainerId)
    }
}