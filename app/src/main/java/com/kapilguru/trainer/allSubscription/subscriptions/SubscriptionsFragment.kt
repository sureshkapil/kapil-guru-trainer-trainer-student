package com.kapilguru.trainer.allSubscription.subscriptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.allSubscription.AllSubscriptionActivity
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.databinding.FragmentSubscriptionsBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.payment.PaymentPreviewActivity
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.ui.profile.ProfileUtils

class SubscriptionsFragment : Fragment(), SubscriptionsListAdapter.RegisterClickListener {
    private val TAG = "SubscriptionsFragment"
    lateinit var binding: FragmentSubscriptionsBinding
    val viewModel: AllSubscriptionViewModel by activityViewModels()
    lateinit var adapter: SubscriptionsListAdapter
    lateinit var progressDialog: CustomProgressDialog
    var subscribeAfterKyc: AllSubscriptionsData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscriptions, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        observeViewModelData()
    }

    private fun setAdapter() {
        adapter = SubscriptionsListAdapter(this)
        binding.rvSubscriptionsList.adapter = adapter
    }

    private fun observeViewModelData() {
        observeUpdateKycResponse()
        viewModel.allSubscriptionsList.observe(viewLifecycleOwner, Observer {
            setAdapterData(it)
        })
    }

    private fun observeUpdateKycResponse() {
        viewModel.updateKycApiResponse.observe(viewLifecycleOwner, Observer { updateKycApiRes ->
            when (updateKycApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    subscribeAfterKyc?.let {
                        launchPaymentPreviewActivity(it)
                    }
                    ProfileUtils.updateIsSubscribed(true, requireContext())
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setAdapterData(allSubscriptionslist: ArrayList<AllSubscriptionsData>) {
        if (ProfileUtils.isSubscribed(requireContext())) {
            //set only position and best trainer subscriptions
            val filteredList = allSubscriptionslist.filter {
                it.subscriptionSubType == SUBSCRIPTION_POSITION ||
                        it.subscriptionSubType == SUBSCRIPTION_SUB_TYPE_BADGE
            }
            adapter.setData(filteredList as ArrayList<AllSubscriptionsData>)
        } else {
            //setting organization Package subscription list or
            //setting individual package subscription list if not subscribed
            val filteredList: ArrayList<AllSubscriptionsData> = if (ProfileUtils.isOrganization(requireContext())) {
                allSubscriptionslist.filter {
                    it.subscriptionSubType == SUBSCRIPTION_PACKAGE && it.subscriptionCategory == SUBSCRIPTION_CATEGORY_ORGANISATION
                } as ArrayList<AllSubscriptionsData>
            } else {
                allSubscriptionslist.filter {
                    it.subscriptionSubType == SUBSCRIPTION_PACKAGE && it.subscriptionCategory == SUBSCRIPTION_CATEGORY_INDIVIDUAL
                } as ArrayList<AllSubscriptionsData>
            }
            adapter.setData(filteredList)
        }
    }

    override fun onRegisterClicked(subscriptionsData: AllSubscriptionsData) {
        viewModel.selectedSubscription = subscriptionsData
        when (subscriptionsData.subscriptionSubType) {
            "badge" -> {
                (activity as AllSubscriptionActivity).launchBestTrainerSubscriptionListActivity()
            }

            "position" -> {
                (activity as AllSubscriptionActivity).launchPositionSubscriptionListActivity(subscriptionsData)
            }

            "package" -> {
                if (ProfileUtils.isSubscribed(requireContext())) {
                    launchPaymentPreviewActivity(subscriptionsData)
                } else {
                    subscribeAfterKyc = subscriptionsData
                    UpdateKycFragment().show(parentFragmentManager, "")
                }
            }
        }
    }

    private fun launchPaymentPreviewActivity(subscriptionsData: AllSubscriptionsData) {
        val initiateTransactionRequest = InitiateTransactionRequest()
        initiateTransactionRequest.amount = subscriptionsData.subscriptionFee
        initiateTransactionRequest.productType = SUBSCRIPTION_PACKAGE
        initiateTransactionRequest.productId = subscriptionsData.id
        val durationDays = subscriptionsData.subscriptionDuration
        PaymentPreviewActivity.launchActivity(initiateTransactionRequest, durationDays, requireActivity())
    }
}