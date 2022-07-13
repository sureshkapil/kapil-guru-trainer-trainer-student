package com.kapilguru.trainer.feeManagement.studentFeeRecords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentFeeRecordsBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel
import com.kapilguru.trainer.network.Status

class FeeRecordsFragment : Fragment() {

    lateinit var binding: FragmentFeeRecordsBinding
    val viewModel: FeeManagementViewModel by viewModels({ requireActivity() })
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: FeeStudentRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFeeRecordsBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(this.requireContext())
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewModel.getStudentFeeRecords()
        viewModel.studentFeeRecordsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    response?.data?.studentFeeRecordsResponseApi?.let { it ->
                        addDataToAdapter(it)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = FeeStudentRecyclerAdapter()
        binding.recy.adapter = adapter
    }

    private fun addDataToAdapter(it: List<StudentFeeRecordsResponseApi>) {
        adapter.listItem = it as ArrayList<StudentFeeRecordsResponseApi>
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeeRecordsFragment()
    }
}