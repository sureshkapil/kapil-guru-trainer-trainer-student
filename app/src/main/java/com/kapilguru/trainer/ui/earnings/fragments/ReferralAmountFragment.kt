package com.kapilguru.trainer.ui.earnings.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ui.earnings.adapter.EarningsDetailsReferralAdapter
import com.kapilguru.trainer.ui.earnings.viewModel.EarningsDetailsViewModel


class ReferralAmountFragment : Fragment() {
//
//    lateinit var referralViewBinding: FragmentReferralAmountBinding
//   val viewModel: EarningsDetailsViewModel by activityViewModels()
//    lateinit var referralAdapter: EarningsDetailsReferralAdapter
//    private val TAG = "ReferralAmountFragment"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        referralViewBinding = FragmentReferralAmountBinding.inflate(inflater)
//        return referralViewBinding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        referralViewBinding.referralViewModel = viewModel
//
//        referralViewBinding.lifecycleOwner = viewLifecycleOwner
//
//        referralAdapter = EarningsDetailsReferralAdapter()
//
//        viewModel.earningsDetailsReferrals.observe(viewLifecycleOwner, {
//            referralAdapter.setDetailsReferralList(it)
//        })
//
//        referralViewBinding.referralRecyclerView.adapter = referralAdapter
//    }
}