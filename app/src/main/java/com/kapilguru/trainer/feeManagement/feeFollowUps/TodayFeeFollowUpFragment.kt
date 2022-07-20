package com.kapilguru.trainer.feeManagement.feeFollowUps

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.addStudent.coursesStudentList.MyCourseStudentsApi
import com.kapilguru.trainer.addStudent.studyMaterialStudentsList.MyStudentsRecordedStudyMaterialsResponseApi
import com.kapilguru.trainer.databinding.FragmentTodayFeeFollowUpBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel
import com.kapilguru.trainer.feeManagement.addFeeManagement.ActivityAddFeeInstallmentsDetails
import com.kapilguru.trainer.feeManagement.paidRecords.PaidRecordRecyclerAdapter
import com.kapilguru.trainer.network.Status


/**
 * A simple [Fragment] subclass.
 * Use the [TodayFeeFollowUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodayFeeFollowUpFragment : Fragment(), FeeFollowUpsRecyclerAdapter.OnItemClick  {

    lateinit var binding: FragmentTodayFeeFollowUpBinding
    val viewModel: FeeManagementViewModel by viewModels({ requireActivity() })
    lateinit var adapter: FeeFollowUpsRecyclerAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodayFeeFollowUpBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(this.requireContext())
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModelObserver()
    }

    private fun setUpRecyclerView() {
        adapter = FeeFollowUpsRecyclerAdapter(this)
        binding.recy.adapter = adapter
    }

    private fun viewModelObserver() {
        viewModel.getStudentFeeFollowUps()
        viewModel.feeFollowUpResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {

                    response?.data?.feeFollowUpResponseApi?.let { it ->
                        if(it.isNotEmpty()) {
                            viewModel.setToDayUPComingFeeFollowUps(it)
                        }
                    }

                }
                Status.ERROR -> {

                }
            }
        })

        viewModel.todaysFeeFollowUpResponse.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) addDataToAdapter(it)
        })
    }

    private fun addDataToAdapter(data: List<FeeFollowUpResponseApi>) {
        adapter.listItem = data as ArrayList<FeeFollowUpResponseApi>
    }

    companion object {
        @JvmStatic
        fun newInstance() = TodayFeeFollowUpFragment()
    }

    override fun onCardClick(feeFollowUpResponseApi: FeeFollowUpResponseApi) {
        startActivity(Intent(this.requireContext(), ActivityAddFeeInstallmentsDetails::class.java).putExtra(PARAM_FEE_STUDENT_DETAILS, feeFollowUpResponseApi)
            .putExtra(PARAM_IS_FROM, PARAM_FEE_FOLLOWUPS))
    }

}