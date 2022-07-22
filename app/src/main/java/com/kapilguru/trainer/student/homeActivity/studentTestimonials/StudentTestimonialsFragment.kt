package com.kapilguru.trainer.student.homeActivity.studentTestimonials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentStudentTestimonialsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModel
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModelFactory
import com.kapilguru.trainer.student.homeActivity.studentTestimonials.model.StudentTestimonialResData


class StudentTestimonialsFragment : Fragment() {
    private val TAG = "StudentTestimonialsFragment"
    private lateinit var binding: FragmentStudentTestimonialsBinding
    lateinit var viewModel: StudentDashBoardViewModel
    lateinit var adapter: StudentTestimonialAdapter
    lateinit var progressDialog: CustomProgressDialog
    private var mIsForDashboard = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStudentTestimonialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getIntentData()
        progressDialog = CustomProgressDialog(this.requireContext())
        val viewModelStoreOwner = if (mIsForDashboard) {
            this.requireParentFragment()
        } else {
            this.requireActivity()
        }
        viewModel = ViewModelProvider(viewModelStoreOwner, StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application)).get(
            StudentDashBoardViewModel::class.java
        )
        setAdapter()
        setVisibility()
        setClickListeners()
        observeViewModelData()
        getStudentTestimonials()
    }

    private fun getIntentData() {
        mIsForDashboard = arguments?.getBoolean(IS_FOR_DASHBOARD) ?: false
    }

    private fun setClickListeners() {
        binding.viewAll.setOnClickListener {
            StudentTestimonialsActivity.launchActivity(requireActivity())
        }
    }

    private fun getStudentTestimonials() {
        viewModel.getStudentTestimonials()
    }

    private fun observeViewModelData() {
        viewModel.studentTestimonialListApiRes.observe(viewLifecycleOwner, Observer { studentTestimonialsApiRes ->
            when (studentTestimonialsApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    val list = studentTestimonialsApiRes.data?.studentTestimonialList
                    checkAndSetAdapterData(list)
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetAdapterData(testimonialsList: ArrayList<StudentTestimonialResData>?) {
        testimonialsList?.let { listNotNull ->
            if (listNotNull.isEmpty()) {
                showOrHideEmptyListView(true)
            } else {
                adapter.setStudentTestimonialsList(listNotNull)
                showOrHideEmptyListView(false)
            }
        } ?: kotlin.run {
            showOrHideEmptyListView(true)
        }
    }

    private fun showOrHideEmptyListView(shouldShowEmptyView: Boolean) {
        if (shouldShowEmptyView) {
            binding.rvTestimonials.visibility = View.INVISIBLE
            binding.actvEmptyView.visibility = View.VISIBLE
        } else {
            binding.rvTestimonials.visibility = View.VISIBLE
            binding.actvEmptyView.visibility = View.GONE
        }
    }

    private fun setAdapter() {
        adapter = StudentTestimonialAdapter(!mIsForDashboard)
        binding.rvTestimonials.adapter = adapter
        binding.rvTestimonials.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setVisibility(){
        if(mIsForDashboard){
            binding.viewAll.visibility = View.VISIBLE
        }else{
            binding.viewAll.visibility = View.GONE
        }
    }

    companion object {
        const val IS_FOR_DASHBOARD = "is_for_dashboard"

        @JvmStatic
        fun newInstance(isForDashBoard: Boolean): StudentTestimonialsFragment {
            val fragment = StudentTestimonialsFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_FOR_DASHBOARD, isForDashBoard)
            fragment.arguments = bundle
            return fragment
        }
    }
}