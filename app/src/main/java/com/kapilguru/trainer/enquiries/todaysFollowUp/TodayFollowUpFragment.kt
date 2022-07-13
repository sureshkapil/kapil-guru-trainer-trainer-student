package com.kapilguru.trainer.enquiries.todaysFollowUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentTodaysFollowUpBinding
import com.kapilguru.trainer.enquiries.viewModel.EnquiriesViewModel

class TodayFollowUpFragment : Fragment() {
    private val TAG = "TodayFollowUpFragment"
    private lateinit var binding: FragmentTodaysFollowUpBinding
    private lateinit var adapter: TodaysFollowUpAdapter
    private lateinit var progressDialog: CustomProgressDialog
    private val viewModel: EnquiriesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodaysFollowUpBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLateInitVariables()
        observeViewModelData()
        viewModel.getTodayFollowUpList()
    }

    private fun initLateInitVariables() {
        adapter = TodaysFollowUpAdapter()
        binding.recyclerview.adapter = adapter
        progressDialog = CustomProgressDialog(requireActivity())
    }

    private fun observeViewModelData() {
        viewModel.todaysFollowUpListMutLiveData.observe(viewLifecycleOwner, Observer { followUpList ->
            adapter.setTodaysFollowUpList(followUpList)
        })
    }

}