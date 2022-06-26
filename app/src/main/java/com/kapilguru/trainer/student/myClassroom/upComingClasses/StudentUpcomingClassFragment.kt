package com.kapilguru.trainer.student.myClassroom.upComingClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentStudentUpcomingClassBinding
import com.kapilguru.trainer.datesDifferenceInMilli
import com.kapilguru.trainer.liveClassMinutesLimit
import com.kapilguru.trainer.student.myClassRoomDetails.StudentMyClassDetailsActivity
import com.kapilguru.trainer.student.myClassroom.liveClasses.StudentLiveUpComingClassAdapter
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentLiveUpComingClassData
import com.kapilguru.trainer.student.myClassroom.viewModel.StudentMyClassroomViewModel

class StudentUpcomingClassFragment : Fragment(), StudentLiveUpComingClassAdapter.StudentLiveUpComingClassClickListener {
    private val TAG = "UpcomingClassFragment"
    lateinit var binding: FragmentStudentUpcomingClassBinding
    val viewModel: StudentMyClassroomViewModel by viewModels(ownerProducer = { requireParentFragment() })
    lateinit var adapter: StudentLiveUpComingClassAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStudentUpcomingClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setAdapterData()
    }

    private fun setAdapter() {
        adapter = StudentLiveUpComingClassAdapter(this)
        binding.rvUpcoimgClasses.adapter = adapter
    }

    private fun setAdapterData() {
        val liveUpComingClassList = viewModel.studentLiveUpComingClassDataList.value
        val upComingClassList = liveUpComingClassList?.filter { liveClassItem ->
            val differenceInMilliSeconds = datesDifferenceInMilli(liveClassItem.startTime!!)
            !liveClassMinutesLimit(differenceInMilliSeconds)
        }
        upComingClassList?.let { liveClassItem ->
            setViewVisibleHide(liveClassItem.isEmpty())
            if (liveClassItem.isNotEmpty()) {
                viewModel.upComingClasses.value = liveClassItem
                adapter.setData(viewModel.upComingClasses.value as ArrayList<StudentLiveUpComingClassData>)
            }
        } ?: kotlin.run {
            setViewVisibleHide(true)
        }
    }

    private fun setViewVisibleHide(isEmpty: Boolean) {
        Log.d(TAG, "setViewVisibleHide: isEmpty : " + isEmpty)
        if (isEmpty) {
            binding.rvUpcoimgClasses.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_upcoming_classes)
        } else {
            binding.rvUpcoimgClasses.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }


    override fun onLiveUpComingClassClicked(liveUpComingClass: StudentLiveUpComingClassData) {

    }

    override fun onOverViewClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        StudentMyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 0)
    }

    override fun onRecordingsClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        StudentMyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 1)
    }

    override fun onStudyMaterialClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        StudentMyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 2)
    }

    override fun onExamClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        StudentMyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 3)
    }
}