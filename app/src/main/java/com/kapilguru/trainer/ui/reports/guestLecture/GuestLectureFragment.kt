package com.kapilguru.trainer.ui.reports.guestLecture

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentGuestLectureBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData
import com.kapilguru.trainer.ui.reports.viewModel.ReportsViewModel

class GuestLectureFragment : Fragment() {
    private val TAG = "GuestLectureFragment"
    val viewModel: ReportsViewModel by activityViewModels()
    lateinit var binding: FragmentGuestLectureBinding
    lateinit var adapter: GuestLectureReportAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuestLectureBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        setAdapter()
        return binding.root
    }

    private fun observeViewModelData() {
        viewModel.guestLectureList.observe(viewLifecycleOwner, Observer { guestLectureResponse ->
            when (guestLectureResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    guestLectureResponse.data?.let { responseData ->
                        adapter.setData(responseData.data as ArrayList<GuestLectureData>)
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
        adapter = GuestLectureReportAdapter()
        binding.rvGuestLecture.adapter = adapter
    }
}