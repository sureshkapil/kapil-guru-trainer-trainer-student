package com.kapilguru.trainer.student.homeActivity.liveCourses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentLiveCoursesBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.courseDetails.StudentCourseDetailsActivity
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModel
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModelFactory
import com.kapilguru.trainer.student.homeActivity.liveCourses.model.LiveCourseResData
import kotlin.math.log

class LiveCoursesFragment : Fragment(),LiveCourseAdapter.ClickListener {
    private val TAG = "LiveCoursesFragment"
    lateinit var binding: FragmentLiveCoursesBinding
    lateinit var viewModel: StudentDashBoardViewModel
    lateinit var adapter: LiveCourseAdapter
    lateinit var progressDialog: CustomProgressDialog
    lateinit var fragmentType: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLiveCoursesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        getIntentData()
        return binding.root
    }

    private fun getIntentData() {
        fragmentType = arguments?.getString(TYPE).toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLateInitVariables()
        setClickListeners()
        observeViewModelData()
        getLiveCourses()
    }

    private fun initLateInitVariables() {
        viewModel = ViewModelProvider(this.requireParentFragment(), StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application)).get(
            StudentDashBoardViewModel::class.java
        )
        progressDialog = CustomProgressDialog(requireActivity())
        setAdapter()
    }

    private fun setAdapter() {
        adapter = LiveCourseAdapter(this)
        binding.recy.adapter = adapter
    }

    private fun setClickListeners() {
        binding.viewAll.setOnClickListener {
            when (fragmentType) {
                LIVE_COURSE -> {
                    Log.d(TAG, "setClickListeners: live course view all")
                }
                RECORDED_COURSE -> {
                    Log.d(TAG, "setClickListeners: recorded course view all")
                }
                STUDY_MATERIAL -> {
                    Log.d(TAG, "setClickListeners: study material view all")
                }
            }
        }
    }

    private fun observeViewModelData() {
        viewModel.liveCourseApiResponse.observe(viewLifecycleOwner, Observer { liveCourseApiRes ->
            when (liveCourseApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    setAdapterData(liveCourseApiRes.data?.liveCourseList)
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setAdapterData(liveCourseList: ArrayList<LiveCourseResData>?) {
        liveCourseList?.let { liveCourseListNotNull ->
            if (liveCourseListNotNull.isNotEmpty()) {
                var listToShow = ArrayList<LiveCourseResData>()
                when (fragmentType) {
                    LIVE_COURSE -> {
                        listToShow = liveCourseListNotNull.filter {
                            it.isRecorded == 0
                        } as ArrayList<LiveCourseResData>
                    }

                    RECORDED_COURSE -> {
                        listToShow = liveCourseListNotNull.filter {
                            it.isRecorded == 1
                        } as ArrayList<LiveCourseResData>
                    }

                    STUDY_MATERIAL -> {
                        listToShow = liveCourseListNotNull.filter {
                            it.isRecorded == 2
                        } as ArrayList<LiveCourseResData>
                    }
                }
                adapter.setLiveCourseList(listToShow)
            }
        }
    }

    private fun getLiveCourses() {
        viewModel.getLiveCourses()
    }

    companion object {
        const val TYPE = "TYPE"
        const val LIVE_COURSE = "LIVE_COURSE"
        const val RECORDED_COURSE = "RECORDED_COURSE"
        const val STUDY_MATERIAL = "STUDY_MATERIAL"

        fun newInstance(fragmentType: String): LiveCoursesFragment {
            val fragment = LiveCoursesFragment()
            val bundle = Bundle()
            bundle.putString(TYPE, fragmentType)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewMoreClicked(liveCourse: LiveCourseResData) {
        StudentCourseDetailsActivity.launchActivity(requireActivity(),liveCourse.id.toString())
    }
}