package com.kapilguru.trainer.allSubscription.mySubscriptions.myPackages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.R
import com.kapilguru.trainer.SUBSCRIPTION_BADGE
import com.kapilguru.trainer.SUBSCRIPTION_PACKAGE
import com.kapilguru.trainer.allSubscription.AllSubscriptionActivity
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.viewModel.MySubscriptionViewModel
import com.kapilguru.trainer.databinding.FragmentMyPackageBinding
import com.kapilguru.trainer.payment.PaymentPreviewActivity
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest

class MyPackageFragment : Fragment(), MyPackagesAdapter.RenewClickListener {
    private val TAG = "MyPackageFragment"
    lateinit var binding : FragmentMyPackageBinding
    val viewModel : AllSubscriptionViewModel by activityViewModels()
    lateinit var adapter : MyPackagesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_package, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeViewModelData()
    }

    private fun observeViewModelData(){
        viewModel.myPackageSubscriptionList.observe(viewLifecycleOwner, Observer {
            setAdapterData(it)
        })
    }
    private fun setAdapter(){
        adapter = MyPackagesAdapter(this)
        binding.rvPackage.adapter = adapter
    }

    private fun setAdapterData(myPackageist : ArrayList<MyPackageData>){
        if(myPackageist.isEmpty()){
         binding.rvPackage.visibility = View.GONE
         binding.actvNoSubscriptions.visibility = View.VISIBLE
        }else{
            adapter.setData(myPackageist)
        }
    }

    override fun onRenewClicked(myPackageData: MyPackageData) {
            val initiateTransactionRequest = InitiateTransactionRequest()
            initiateTransactionRequest.amount = myPackageData.subsFee!!.toDouble()
            initiateTransactionRequest.productType = SUBSCRIPTION_PACKAGE
            initiateTransactionRequest.productId = myPackageData.subsId!!.toInt()
            PaymentPreviewActivity.launchActivity(initiateTransactionRequest,myPackageData.subsDuration!!,requireActivity())
    }
}