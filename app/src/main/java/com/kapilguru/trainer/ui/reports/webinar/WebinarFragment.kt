package com.kapilguru.trainer.ui.reports.webinar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentWebinarBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.reports.viewModel.ReportsViewModel

class WebinarFragment : Fragment() {

    private val TAG = "WebinarFragment"
    val viewModel: ReportsViewModel by activityViewModels()
    lateinit var binding: FragmentWebinarBinding
    lateinit var adapter: WebinarReportAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebinarBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        setAdapter()
        return binding.root
    }

    private fun observeViewModelData() {
        viewModel.webinarsList.observe(viewLifecycleOwner, Observer { webinarApiResponse ->
            when (webinarApiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    webinarApiResponse.data?.let { responseData ->
                        adapter.setData(responseData.webinarData)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setAdapter() {
        adapter = WebinarReportAdapter()
        binding.rvGuestLecture.adapter = adapter
    }
}