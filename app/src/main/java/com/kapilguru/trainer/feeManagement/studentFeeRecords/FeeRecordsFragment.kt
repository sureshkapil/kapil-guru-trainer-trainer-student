package com.kapilguru.trainer.feeManagement.studentFeeRecords

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.PARAM_FEE_RECORDS
import com.kapilguru.trainer.PARAM_FEE_STUDENT_DETAILS
import com.kapilguru.trainer.PARAM_IS_FROM
import com.kapilguru.trainer.databinding.FragmentFeeRecordsBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel
import com.kapilguru.trainer.feeManagement.addFeeManagement.ActivityAddFeeInstallmentsDetails
import com.kapilguru.trainer.network.Status

class FeeRecordsFragment : Fragment(), FeeStudentRecyclerAdapter.OnItemClick {

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

                }
                Status.SUCCESS -> {
                    response?.data?.studentFeeRecordsResponseApi?.let { it ->
                        addDataToAdapter(it)
                    }

                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = FeeStudentRecyclerAdapter(this)
        binding.recy.adapter = adapter
    }

    private fun addDataToAdapter(it: List<StudentFeeRecordsResponseApi>) {
        adapter.listItem = it as ArrayList<StudentFeeRecordsResponseApi>
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeeRecordsFragment()
    }

    override fun onCardClick(studentFeeRecordsResponseApi: StudentFeeRecordsResponseApi) {
        naviagteToFeeInstallmentDetails(studentFeeRecordsResponseApi)
    }

    private fun naviagteToFeeInstallmentDetails(studentFeeRecordsResponseApi: StudentFeeRecordsResponseApi) {
        startActivity(Intent(this.requireContext(), ActivityAddFeeInstallmentsDetails::class.java).putExtra(PARAM_FEE_STUDENT_DETAILS, studentFeeRecordsResponseApi)
            .putExtra(PARAM_IS_FROM, PARAM_FEE_RECORDS))
    }
}