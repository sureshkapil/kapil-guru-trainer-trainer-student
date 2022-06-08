package com.kapilguru.trainer.ui.reports.course

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.COURSE_INFO_PARAM
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentCourseBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import com.kapilguru.trainer.ui.reports.batch.BatchReportActivity
import com.kapilguru.trainer.ui.reports.viewModel.ReportsViewModel

class CourseFragment : Fragment(), CourseReportAdapter.OnCourseClickListener {
    private val TAG = "CourseFragment"
    val viewModel: ReportsViewModel by activityViewModels()
    lateinit var binding: FragmentCourseBinding
    lateinit var adapter: CourseReportAdapter
    lateinit var progressDialog: CustomProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        setAdapter()
        return binding.root
    }

    private fun observeViewModelData() {
        viewModel.courseList.observe(viewLifecycleOwner, Observer { courseResponse ->
            when (courseResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    courseResponse.data?.let { responseData ->
                        adapter.setData(responseData.courseResponse as ArrayList<CourseResponse>)
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
        adapter = CourseReportAdapter(this)
        binding.rvCourse.adapter = adapter
    }

    override fun onCourseClicked(course: CourseResponse) {
        val intent = Intent(requireContext(),BatchReportActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(COURSE_INFO_PARAM, course)
        intent.putExtras(bundle)
        activity?.startActivity(intent)
    }
}