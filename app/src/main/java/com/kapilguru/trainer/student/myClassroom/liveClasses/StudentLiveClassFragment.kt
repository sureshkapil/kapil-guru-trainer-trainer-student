package com.kapilguru.trainer.student.myClassroom.liveClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentStudentLiveClassBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.myClassRoomDetails.StudentMyClassDetailsActivity
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentActiveBatchData
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentLiveUpComingClassData
import com.kapilguru.trainer.student.myClassroom.viewModel.StudentMyClassroomViewModel
import com.kapilguru.trainer.videoCall.VideoCallInterfaceImplementation
import org.json.JSONArray

class StudentLiveClassFragment : Fragment(), StudentLiveUpComingClassAdapter.StudentLiveUpComingClassClickListener {
    private val TAG = "LiveClassFragment"
    lateinit var binding: FragmentStudentLiveClassBinding
    val viewModel: StudentMyClassroomViewModel by viewModels(ownerProducer = { requireParentFragment() })
    lateinit var adapter: StudentLiveUpComingClassAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStudentLiveClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        observeViewModelData()
        viewModel.getAllClasses()
    }

    private fun setAdapter() {
        adapter = StudentLiveUpComingClassAdapter(this)
        binding.rvLiveClasses.adapter = adapter
    }

    private fun observeViewModelData() {
        viewModel.studentAllClassesResponse.observe(viewLifecycleOwner, Observer { allClassesAPiRes ->
            when (allClassesAPiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    allClassesAPiRes.data?.let { allClassesResponse ->
                        val liveUpComingClassesString = allClassesResponse.allClassesData.liveUpComingClassesString
                        val activeClassesString = allClassesResponse.allClassesData.activeClassesString
                        parseLiveUpComingClassesString(liveUpComingClassesString)
                        parseActiveClassesString(activeClassesString)
                    }
                    addBatchDataToLiveClasses()
                    setAdapterData()
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (allClassesAPiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(requireContext(), getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })
    }

    private fun parseLiveUpComingClassesString(liveUpComingClasses: String?) {
        if (liveUpComingClasses == null) {
            return
        }
        val liveUpComingClassesList = ArrayList<StudentLiveUpComingClassData>()
        val jsonArray = JSONArray(liveUpComingClasses)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val studentLiveUpComingClassData = StudentLiveUpComingClassData().getObject(jsonObject)
            liveUpComingClassesList.add(studentLiveUpComingClassData)
        }
        viewModel.updateLiveUpComingClassList(liveUpComingClassesList)
    }

    private fun parseActiveClassesString(liveActiveClasses: String?) {
        if (liveActiveClasses == null) {
            return
        }
        val activeClassesList = ArrayList<StudentActiveBatchData>()
        val jsonArray = JSONArray(liveActiveClasses)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val studentActiveBatchData = StudentActiveBatchData().getObject(jsonObject)
            activeClassesList.add(studentActiveBatchData)
        }
        viewModel.activeBatchesList1.addAll(activeClassesList)
    }

    private fun addBatchDataToLiveClasses() {
        val liveUpcomingClassWithBatchData = ArrayList<StudentLiveUpComingClassData>()
        val liveClasses = viewModel.studentLiveUpComingClassDataList.value
        val activeBatches = viewModel.activeBatchesList1
        if (liveClasses != null) {
            for (liveClass in liveClasses) {
                for (activeBatch in activeBatches) {
                    if (liveClass.batchId == activeBatch.batchId) {
                        liveClass.batchData = activeBatch
                        liveUpcomingClassWithBatchData.add(liveClass)
                        break
                    }
                }
            }
            viewModel.updateLiveUpComingClassList(liveUpcomingClassWithBatchData)
        }
    }

    private fun setAdapterData() {
        val liveUpComingClassList = viewModel.studentLiveUpComingClassDataList.value
        val onlyLiveClassList = liveUpComingClassList?.filter { liveClassItem ->
            val differenceInMilliSeconds = datesDifferenceInMilli(liveClassItem.startTime!!)
            liveClassMinutesLimit(differenceInMilliSeconds)
        }
        onlyLiveClassList?.let { liveClassItem ->
            setViewVisibleHide(liveClassItem.isEmpty())
            if (liveClassItem.isNotEmpty()) {
                viewModel.liveClasses.value = liveClassItem
                adapter.setData(viewModel.liveClasses.value as ArrayList<StudentLiveUpComingClassData>)
            }
        } ?: kotlin.run {
            setViewVisibleHide(true)
        }
    }

    private fun setViewVisibleHide(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvLiveClasses.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_live_classes)
        } else {
            binding.rvLiveClasses.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    override fun onLiveUpComingClassClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        val roomName = liveUpComingClass.roomName ?: "Room Name"
        val participantName = StorePreferences(requireContext()).userName
        VideoCallInterfaceImplementation.launchStudentVideoCall(requireActivity(), roomName, participantName!!)
    }

    override fun onOverViewClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        StudentMyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 0)
    }

    override fun onRecordingsClicked(liveUpComingClass: StudentLiveUpComingClassData) {

    }

    override fun onStudyMaterialClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        StudentMyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 2)
    }

    override fun onExamClicked(liveUpComingClass: StudentLiveUpComingClassData) {
        StudentMyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 3)
    }
}