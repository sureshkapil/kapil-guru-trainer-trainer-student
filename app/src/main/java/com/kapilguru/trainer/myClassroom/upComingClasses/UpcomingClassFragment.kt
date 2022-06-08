package com.kapilguru.trainer.myClassroom.upComingClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.databinding.FragmentUpcomingClassBinding
import com.kapilguru.trainer.datesDifferenceInMilli
import com.kapilguru.trainer.liveClassMinutesLimit
import com.kapilguru.trainer.myClassRoomDetails.MyClassDetails
import com.kapilguru.trainer.myClassroom.liveClasses.LiveUpComingClassAdapter
import com.kapilguru.trainer.myClassroom.liveClasses.model.LiveUpComingClassData
import com.kapilguru.trainer.myClassroom.viewModel.MyClassroomViewModel

class UpcomingClassFragment : Fragment(), LiveUpComingClassAdapter.LiveUpComingClassClickListener {
    private val TAG = "UpcomingClassFragment"
    lateinit var binding: FragmentUpcomingClassBinding
    val viewModel: MyClassroomViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    lateinit var adapter: LiveUpComingClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingClassBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setAdapterData()
    }

    private fun setAdapter(){
        adapter = LiveUpComingClassAdapter(this)
        binding.rvUpcoimgClasses.adapter = adapter
    }

    private fun setAdapterData() {
        val liveUpComingClassList = viewModel.liveUpComingClassDataList.value
        val upComingClassList = liveUpComingClassList?.filter { liveClassItem ->
            val differenceInMilliSeconds =
                datesDifferenceInMilli(liveClassItem.startTime!!)
            !liveClassMinutesLimit(differenceInMilliSeconds)
        }
        upComingClassList?.let { liveClassItem ->
            viewModel.upComingClasses.value = liveClassItem
            adapter.setData(viewModel.upComingClasses.value as ArrayList<LiveUpComingClassData>)
        }
    }

    override fun onLiveUpComingClassClicked(liveUpComingClass: LiveUpComingClassData) {

    }

    override fun onOverViewClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(),requireActivity(),0)
    }

    override fun onStudyMaterialClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(),requireActivity(),1)
    }

    override fun onExamClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(),requireActivity(),2)
    }

    override fun onCompletionRequestClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(),requireActivity(),3)
    }
}