package com.kapilguru.trainer.student.homeActivity.liveCourses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.FragmentLiveCoursesBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.LiveCourseListActivity
import com.kapilguru.trainer.student.courseDetails.StudentCourseDetailsActivity
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModel
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModelFactory
import com.kapilguru.trainer.student.homeActivity.liveCourses.model.LiveCourseResData

class LiveCoursesFragment : Fragment(), LiveCourseAdapter.ClickListener {
    private val TAG = "LiveCoursesFragment"
    lateinit var binding: FragmentLiveCoursesBinding
    lateinit var viewModel: StudentDashBoardViewModel
    lateinit var adapter: LiveCourseAdapter
    lateinit var progressDialog: CustomProgressDialog
    lateinit var mFragmentType: String
    private var mIsForDashBoard = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLiveCoursesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        getIntentData()
        return binding.root
    }

    private fun getIntentData() {
        mFragmentType = arguments?.getString(TYPE).toString()
        mIsForDashBoard = arguments?.getBoolean(IS_FOR_DASH_BOARD) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLateInitVariables()
        setVisibility()
        setClickListeners()
        observeViewModelData()
        getLiveCourses()
    }

    private fun initLateInitVariables() {
        viewModel = ViewModelProvider(this.requireActivity(), StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application)).get(
            StudentDashBoardViewModel::class.java
        )
        progressDialog = CustomProgressDialog(requireActivity())
        setAdapter()
    }

    private fun setAdapter() {
        if(mIsForDashBoard){
            adapter = LiveCourseAdapter(this, false)
        }else{
            adapter = LiveCourseAdapter(this, true)
        }
        binding.recy.adapter = adapter
    }

    private fun setVisibility(){
        if(!mIsForDashBoard){
            binding.viewAll.visibility = View.GONE
            binding.recy.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }else{
            binding.viewAll.visibility = View.VISIBLE
            binding.recy.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setClickListeners() {
        binding.viewAll.setOnClickListener {
            when (mFragmentType) {
                LIVE_COURSE -> {
                    Log.d(TAG, "setClickListeners: live course view all")
                    LiveCourseListActivity.launchActivity(requireActivity(), LiveCourseListActivity.LIVE_COURSE)
                }
                RECORDED_COURSE -> {
                    Log.d(TAG, "setClickListeners: recorded course view all")
                    LiveCourseListActivity.launchActivity(requireActivity(), LiveCourseListActivity.RECORDED_COURSE)
                }
                STUDY_MATERIAL -> {
                    Log.d(TAG, "setClickListeners: study material view all")
                    LiveCourseListActivity.launchActivity(requireActivity(), LiveCourseListActivity.STUDY_MATERIAL)
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
                when (mFragmentType) {
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
        const val IS_FOR_DASH_BOARD = "IS_FOR_DASH_BOARD"
        const val LIVE_COURSE = "LIVE_COURSE"
        const val RECORDED_COURSE = "RECORDED_COURSE"
        const val STUDY_MATERIAL = "STUDY_MATERIAL"

        fun newInstance(fragmentType: String, isForDashBoardFragment: Boolean): LiveCoursesFragment {
            val fragment = LiveCoursesFragment()
            val bundle = Bundle()
            bundle.putString(TYPE, fragmentType)
            bundle.putBoolean(IS_FOR_DASH_BOARD, isForDashBoardFragment)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewMoreClicked(liveCourse: LiveCourseResData) {
        when (liveCourse.isRecorded) {
            0 -> {
                StudentCourseDetailsActivity.launchActivity(requireActivity(), liveCourse.id.toString())
            }
            1 -> {

            }
            2 -> {

            }
        }
    }
}