package com.kapilguru.trainer.enquiries.kapilGuruEnquiries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentKapilGuruEnquiriesBinding
import com.kapilguru.trainer.enquiries.enquiryStatusUpdate.EnquiryStatusUpdateActivity
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResData
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiryStatusUpdateRequest
import com.kapilguru.trainer.enquiries.viewModel.EnquiriesViewModel
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class KapilGuruEnquiriesFragment : Fragment(), EnquiriesAdapter.ClickListeners {
    private val TAG = "KapilGuruEnquiriesFragment"
    private lateinit var binding: FragmentKapilGuruEnquiriesBinding
    private lateinit var adapter: EnquiriesAdapter
    private lateinit var progressDialog: CustomProgressDialog
    private val viewModel: EnquiriesViewModel by activityViewModels()
    private var mUpdateIsSeenPosition: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentKapilGuruEnquiriesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLateInitVariables()
        observeViewModelData()
        val trainerId = StorePreferences(requireContext()).userId
        viewModel.getEnquiriesList(trainerId.toString())
    }

    private fun initLateInitVariables() {
        adapter = EnquiriesAdapter(false, this)
        binding.recyclerview.adapter = adapter
        progressDialog = CustomProgressDialog(requireActivity())
    }

    private fun observeViewModelData() {
        observeLoadingIndicatorLivedata()
        observeEnquiries()
        observeEnquiryStatusUpdateResponse()
    }

    private fun observeLoadingIndicatorLivedata() {
        viewModel.showLoadingIndicator.observe(viewLifecycleOwner, Observer { shouldShow ->
            if (shouldShow) {
                progressDialog.showLoadingDialog()
            } else {
                progressDialog.dismissLoadingDialog()
            }
        })
    }

    private fun observeEnquiries() {
        viewModel.enquiriesListApiRes.observe(viewLifecycleOwner, Observer { enquiriesApiRes ->
            when (enquiriesApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    val enquiriesList = enquiriesApiRes.data?.enquiries
                    viewModel.enquiriesList.value = enquiriesList
                    setAdapterData()
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeEnquiryStatusUpdateResponse() {
        viewModel.isEnquiryStatusUpdated.observe(viewLifecycleOwner, Observer { isUpdated ->
            if (isUpdated) {
                adapter.updateIsContactSeen(mUpdateIsSeenPosition)
            }
        })
    }

    private fun setAdapterData() {
        val trainerId = StorePreferences(requireContext()).userId
        val enquiries = viewModel.enquiriesList.value
        enquiries?.let { enquiriesNotNull ->
            val kapilGuruEnquiries = enquiriesNotNull.filter { enquiry ->
                enquiry.createdBy != trainerId
            }
            adapter.setEnquiriesList(kapilGuruEnquiries as ArrayList<EnquiriesResData>)
            viewModel.kapilGuruEnquiries.value = kapilGuruEnquiries
        }
    }

    override fun onViewContactClicked(enquiry: EnquiriesResData, positionInList: Int) {
        mUpdateIsSeenPosition = positionInList
        val enquiryStatusUpdateRequest = EnquiryStatusUpdateRequest(enquiry.id, enquiry.createdBy, EnquiryStatusUpdateRequest.ENQUIRY_STATUS_VIEWED)
        viewModel.updateEnquiryStatus(enquiryStatusUpdateRequest)
    }

    override fun onLaunchEnquiryListClicked(enquiry: EnquiriesResData) {
        EnquiryStatusUpdateActivity.launchActivity(requireActivity(),enquiry.id.toString())
    }
}