package com.kapilguru.trainer.feeManagement.paidRecords

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentPaidRecordsBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel
import com.kapilguru.trainer.feeManagement.addFeeManagement.ActivityAddFeeInstallmentsDetails
import com.kapilguru.trainer.feeManagement.studentFeeRecords.StudentFeeRecordsResponseApi
import com.kapilguru.trainer.network.Status


/**
 * A simple [Fragment] subclass.
 * Use the [PaidRecordsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaidRecordsFragment : Fragment(), PaidRecordRecyclerAdapter.OnItemClick {


    lateinit var binding: FragmentPaidRecordsBinding
    val viewModel: FeeManagementViewModel by viewModels({ requireActivity() })
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: PaidRecordRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPaidRecordsBinding.inflate(inflater, container, false)
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
        viewModel.getStudentPaidRecords()
        viewModel.studentFeePaidResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    response?.data?.studentFeeRecordResponseApi?.let { it ->
                        addDataToAdapter(it)
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = PaidRecordRecyclerAdapter(this)
        binding.recy.adapter = adapter
    }

    private fun addDataToAdapter(it: List<StudentFeePaidResponseApi>) {
        adapter.listItem = it as ArrayList<StudentFeePaidResponseApi>
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismissLoadingDialog()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = PaidRecordsFragment()
    }

    override fun onCardClick(studentFeePaidResponseApi: StudentFeePaidResponseApi) {
        startActivity(Intent(this.requireContext(), ActivityAddFeeInstallmentsDetails::class.java).putExtra(PARAM_FEE_STUDENT_DETAILS, studentFeePaidResponseApi)
            .putExtra(PARAM_IS_FROM, PARAM_PAID_RECORDS))
    }
}