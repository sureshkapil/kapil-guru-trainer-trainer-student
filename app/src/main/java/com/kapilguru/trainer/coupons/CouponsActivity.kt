package com.kapilguru.trainer.coupons

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityCouponsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class CouponsActivity : BaseActivity() {

    lateinit var binding: ActivityCouponsBinding
    lateinit var adapter: CouponsAdapter
    lateinit var viewModel: CouponsViewModel
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupons)
        viewModel = ViewModelProvider(this, CouponsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(CouponsViewModel::class.java)
        dialog = CustomProgressDialog(this)
        setCustomActionBar()
        setClickListeners()
        setRecycler()
        observeViewModel()
    }

    private fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.coupons))
    }

    private fun setClickListeners() {
        binding.buttonAddCoupon.setOnClickListener {
            navigateToAddCoupons()
        }
    }

    private fun navigateToAddCoupons() {
        startActivity(Intent(this,AddCoupons::class.java))
    }

    fun setRecycler() {
        adapter = CouponsAdapter()
        binding.recyclerview.adapter = adapter
    }

    private fun observeViewModel() {
        val trainerId: Int = StorePreferences(this).userId
        viewModel.getCouponsList(trainerId)
        viewModel.couponsList.observe(this, Observer {it->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.allCouponsResponseListApi?.let {response->
                        if(response.isNotEmpty()) {
                            setAdapterData(response)
                        }

                    }

                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setAdapterData(response: List<AllCouponsResponseListApi>) {
        adapter.couponsList = response as ArrayList<AllCouponsResponseListApi>
    }


}