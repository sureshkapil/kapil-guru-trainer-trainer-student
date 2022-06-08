package com.kapilguru.trainer.allSubscription.subscriptions

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.allSubscription.AllSubscriptionViewModel
import com.kapilguru.trainer.allSubscription.models.UpdateKycRequest
import com.kapilguru.trainer.databinding.FragmnetUpdateKycBinding
import com.kapilguru.trainer.isGstValid
import com.kapilguru.trainer.isPanValid
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.showAppToast
import kotlinx.android.synthetic.main.fragmnet_update_kyc.view.*

class UpdateKycFragment : DialogFragment() {
    private val TAG = "UpdateKycFragment"
    private lateinit var binding: FragmnetUpdateKycBinding
    private lateinit var viewModel: AllSubscriptionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmnetUpdateKycBinding.inflate(inflater, container, false)
        binding.updateKycRequest = UpdateKycRequest()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(AllSubscriptionViewModel::class.java)
        dialog?.setCancelable(false)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.actvClose.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnSubmit.btn_submit.setOnClickListener {
            onUpdateKycDetailsClicked()
        }
    }

    private fun onUpdateKycDetailsClicked() {
        val updateKycRequest = binding.updateKycRequest
        val pan = updateKycRequest!!.pan.toUpperCase()
        var gst : String? = null
        updateKycRequest.gst?.let {
            gst = it
        }
        val isPanValid = pan.isPanValid()
        var isGstValid = true
        gst?.let { gstNotNull ->
            isGstValid = gstNotNull.isGstValid()
        }
        if (!isPanValid) {
            showAppToast(requireContext(), "Please enter correct PAN")
        } else if (!isGstValid) {
            showAppToast(requireContext(), "Please enter correct GST")
        } else {
            val request = UpdateKycRequest(pan, gst)
            val trainerId = StorePreferences(requireContext()).trainerId.toString()
            viewModel.updateKyc(trainerId, request)
            dialog?.dismiss()
        }
    }
}