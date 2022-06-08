package com.kapilguru.trainer.myClassroom.liveClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageResponse
import com.kapilguru.trainer.databinding.FragmentLiveClassBinding
import com.kapilguru.trainer.datesDifferenceInMilli
import com.kapilguru.trainer.liveClassMinutesLimit
import com.kapilguru.trainer.myClassRoomDetails.MyClassDetails
import com.kapilguru.trainer.myClassroom.liveClasses.model.LiveUpComingClassData
import com.kapilguru.trainer.myClassroom.viewModel.MyClassroomViewModel
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.Status

class LiveClassFragment : Fragment(), LiveUpComingClassAdapter.LiveUpComingClassClickListener {
    private val TAG = "LiveClassFragment"
    lateinit var binding: FragmentLiveClassBinding
    val viewModel: MyClassroomViewModel by viewModels({ requireParentFragment() })
    lateinit var adapter: LiveUpComingClassAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLiveClassBinding.inflate(inflater, container, false)
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
        adapter = LiveUpComingClassAdapter(this)
        binding.rvLiveClasses.adapter = adapter
    }

    private fun observeViewModelData() {
        viewModel.liveUpComingClassResponse.observe(
            this.viewLifecycleOwner,
            Observer { liveUpComingClassRes ->
                when (liveUpComingClassRes.status) {
                    Status.LOADING -> {
                        progressDialog.showLoadingDialog()
                    }
                    Status.SUCCESS -> {
                        fetchLiveUpComingClassList()
                        progressDialog.dismissLoadingDialog()
                    }
                    Status.ERROR -> {
                        progressDialog.dismissLoadingDialog()
                    }
                }
            })

        viewModel.activeBatchListResponse.observe(this.viewLifecycleOwner, { activeBatchListRes ->
            when (activeBatchListRes.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    fetchActiveBatchList(activeBatchListRes)
                    addBatchDataToLiveClasses()
                    setAdapterData()
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun fetchLiveUpComingClassList() {
        viewModel.liveUpComingClassResponse.value?.let { liveUpComingClassApiRes ->
            liveUpComingClassApiRes.data?.let { liveUpComingClassRes ->
                liveUpComingClassRes.liveUpComingClassData?.let { liveUpComingClassList ->
                   viewModel.liveUpComingClassDataList.value = liveUpComingClassList
                }
            }
        }
    }

    private fun fetchActiveBatchList(activeBatchListApiRes: ApiResource<NewMessageResponse>) {
        activeBatchListApiRes.data?.let { batchListRes ->
            batchListRes.data?.let { batchList ->
                viewModel.activeBatchesList.value = batchList
            }
        }
    }

    private fun addBatchDataToLiveClasses() {
        val liveUpcomingClassWithBatchData = ArrayList<LiveUpComingClassData>()
        val liveClasses = viewModel.liveUpComingClassDataList.value
        val activeBatches = viewModel.activeBatchesList.value
        if (liveClasses != null && activeBatches != null) {
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
        val liveUpComingClassList = viewModel.liveUpComingClassDataList.value
        val onlyLiveClassList = liveUpComingClassList?.filter { liveClassItem ->
            val differenceInMilliSeconds =
                datesDifferenceInMilli(liveClassItem.startTime!!)
            liveClassMinutesLimit(differenceInMilliSeconds)
        }
        onlyLiveClassList?.let { liveClassItem ->
            viewModel.liveClasses.value = liveClassItem
            adapter.setData(viewModel.liveClasses.value as ArrayList<LiveUpComingClassData>)
        }
    }

    override fun onLiveUpComingClassClicked(liveUpComingClass: LiveUpComingClassData) {
    }

    override fun onOverViewClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(), requireActivity(), 0)
    }

    override fun onStudyMaterialClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(), requireActivity(),1)
    }

    override fun onExamClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(), requireActivity(),2)
    }

    override fun onCompletionRequestClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetails.launchActivity(liveUpComingClass.batchData?.batchId.toString(), requireActivity(),3)
    }

}