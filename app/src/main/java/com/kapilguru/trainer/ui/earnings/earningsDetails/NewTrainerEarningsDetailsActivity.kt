package com.kapilguru.trainer.ui.earnings.earningsDetails

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.coupons.AllCouponsResponseListApi
import com.kapilguru.trainer.databinding.ActivityNewTrainerEarningsDetailsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsViewModel
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsViewModelFactory

class NewTrainerEarningsDetailsActivity : BaseActivity() {

    lateinit var binding: ActivityNewTrainerEarningsDetailsBinding
    lateinit var adapter: TrainerEarningsRecyclerAdapter
    lateinit var viewModel: EarningsViewModel
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new_trainer_earnings_details)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_trainer_earnings_details)
        viewModel = ViewModelProvider(this, EarningsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(EarningsViewModel::class.java)
        dialog = CustomProgressDialog(this)
        setCustomActionBar()
//        setClickListeners()
        setRecycler()
        observeViewModel()
    }

    private fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.coupons))
    }

    fun setRecycler() {
        adapter = TrainerEarningsRecyclerAdapter()
        binding.recyclerview.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.earningsDetailsResponseDetails()
        viewModel.earningsDetailsResponse.observe(this, Observer {it->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.earningsDetailsResponseApi?.let {response->
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

    private fun setAdapterData(response: List<EarningsDetailsResponseApi>) {
        adapter.listItem = response as ArrayList<EarningsDetailsResponseApi>
    }
}