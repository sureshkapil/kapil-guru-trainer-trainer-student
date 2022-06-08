package com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions.renewConfirmation.MyPosRenewConfirmActivity
import com.kapilguru.trainer.allSubscription.positionSubscription.confirmation.PositionSubscriptionConfirmActivity
import com.kapilguru.trainer.databinding.FragmentMyPositionBinding


class MyPositionFragment : Fragment(), MyPositionSubsAdapter.PositionRenewClickListener {
    lateinit var binding: FragmentMyPositionBinding
    val viewModel: AllSubscriptionViewModel by activityViewModels()
    lateinit var adapter: MyPositionSubsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_position, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelData()
        setAdapter()
    }

    private fun observeViewModelData() {
        viewModel.myPositionSubscriptionList.observe(viewLifecycleOwner, Observer {
            it?.let {
                setAdapterData(it)
            }
        })
    }

    private fun setAdapter() {
        adapter = MyPositionSubsAdapter(this)
        binding.rvMyPosition.adapter = adapter
    }

    private fun setAdapterData(myPositionList : ArrayList<MyPositionData>){
        if(myPositionList.isEmpty()){
            binding.rvMyPosition.visibility = View.GONE
            binding.actvNoSubscriptions.visibility = View.VISIBLE
        }else{
            adapter.setData(myPositionList)
        }
    }

    override fun onRenewClicked(myPositionData: MyPositionData) {
        MyPosRenewConfirmActivity.launchActivity(requireActivity(), myPositionData)
    }

}