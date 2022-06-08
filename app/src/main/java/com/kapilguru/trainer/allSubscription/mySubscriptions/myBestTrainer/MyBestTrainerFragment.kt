package com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer.renewConfirmation.MyBadgeRenewConfirmActivity
import com.kapilguru.trainer.allSubscription.mySubscriptions.viewModel.MySubscriptionViewModel
import com.kapilguru.trainer.databinding.FragmentMyBestTrainerBinding

class MyBestTrainerFragment : Fragment(), MyBestTrainerAdapter.RenewMyBestTrainerClickListener {
    lateinit var binding : FragmentMyBestTrainerBinding
    val viewModel : AllSubscriptionViewModel by activityViewModels()
    lateinit var adapter : MyBestTrainerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_best_trainer,container,false)
        observeViewModelData()
        setAdapter()
        return binding.root
    }

    private fun observeViewModelData(){
        viewModel.myBestTrainerSubscriptionList.observe(viewLifecycleOwner, Observer {
            it?.let{
                setAdapterData(it)
            }
        })
    }

    private fun setAdapter(){
        adapter = MyBestTrainerAdapter(this)
        binding.rvMyBestTrainer.adapter = adapter
    }

    private fun setAdapterData(myBestTrainerList : ArrayList<MyBestTrainerData>){
        if(myBestTrainerList.isEmpty()){
            binding.rvMyBestTrainer.visibility = View.GONE
            binding.actvNoSubscriptions.visibility = View.VISIBLE
        }else{
            adapter.setData(myBestTrainerList)
        }
    }

    override fun onRenewBestTrainerClicked(myBestTrainerData: MyBestTrainerData) {
        MyBadgeRenewConfirmActivity.launchActivity(requireActivity(),myBestTrainerData)
    }
}