package com.kapilguru.trainer.coupons

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getCouponsInfo()
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
        startActivity(Intent(this,AddCoupons::class.java).putParcelableArrayListExtra(COUPONS_DATA,viewModel.allCouponsResponseListApi))
    }

    fun setRecycler() {
        adapter = CouponsAdapter()
        binding.recyclerview.adapter = adapter
    }

    private fun observeViewModel() {
        getCouponsInfo()
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

    private fun getCouponsInfo() {
        val trainerId: Int = StorePreferences(this).userId
        viewModel.getCouponsList(trainerId)
    }

    private fun setAdapterData(response: List<AllCouponsResponseListApi>) {
        viewModel.allCouponsResponseListApi = response as ArrayList<AllCouponsResponseListApi>
        adapter.couponsList = response as ArrayList<AllCouponsResponseListApi>
    }


}