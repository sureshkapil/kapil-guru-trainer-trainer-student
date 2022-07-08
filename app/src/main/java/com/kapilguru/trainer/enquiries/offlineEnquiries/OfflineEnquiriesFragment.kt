package com.kapilguru.trainer.enquiries.offlineEnquiries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentOfflineEnquiriesBinding
import com.kapilguru.trainer.enquiries.addOfflineEnquiry.AddOfflineEnquiryActivity
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.EnquiriesAdapter
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResData
import com.kapilguru.trainer.enquiries.viewModel.EnquiriesViewModel
import com.kapilguru.trainer.preferences.StorePreferences

class OfflineEnquiriesFragment : Fragment() {
    private val TAG = "OfflineEnquiriesFragment"
    private lateinit var binding: FragmentOfflineEnquiriesBinding
    private lateinit var adapter: EnquiriesAdapter
    private lateinit var progressDialog: CustomProgressDialog
    private val viewModel: EnquiriesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOfflineEnquiriesBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLateInitVariables()
        setAdapter()
        observeViewModelData()
        setClickListeners()
    }

    private fun initLateInitVariables() {
        adapter = EnquiriesAdapter()
        binding.recyclerview.adapter = adapter
        progressDialog = CustomProgressDialog(requireActivity())
    }

    private fun setAdapter() {
        adapter = EnquiriesAdapter()
        binding.recyclerview.adapter = adapter
    }

    private fun observeViewModelData() {
        val trainerId = StorePreferences(requireContext()).userId
        viewModel.enquiriesList.observe(viewLifecycleOwner, Observer { enquiries ->
            val offlineEnquiries = enquiries.filter { enquiry ->
                enquiry.createdBy == trainerId
            }
            adapter.setEnquiriesList(offlineEnquiries as ArrayList<EnquiriesResData>)
            viewModel.offlineEnquiries.value = offlineEnquiries
        })
    }

    private fun setClickListeners() {
        binding.btnAddEnquiry.setOnClickListener {
            onAddEnquiryClicked()
        }
    }

    private fun onAddEnquiryClicked() {
        AddOfflineEnquiryActivity.startActivity(requireActivity())
    }
}