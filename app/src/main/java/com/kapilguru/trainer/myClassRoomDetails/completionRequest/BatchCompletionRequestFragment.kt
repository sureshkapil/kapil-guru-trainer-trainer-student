package com.kapilguru.trainer.myClassRoomDetails.completionRequest

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.ActivityBatchCompletionRequestBinding
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionResBCReq
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqFactory
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqViewModel
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class BatchCompletionRequestFragment : Fragment() {
    private val TAG = "BatchCompletionRequestActivity"
    lateinit var binding: ActivityBatchCompletionRequestBinding
    lateinit var viewModel: BatchCompletionReqViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: BatchCompletionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityBatchCompletionRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, BatchCompletionReqFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(BatchCompletionReqViewModel::class.java)
        binding.lifecycleOwner = this
        adapter = BatchCompletionAdapter()
        binding.batchRecyclerView.adapter = adapter
        observeViewModelData()
        fetchBatchCompletionResponse()
        setClickListeners()
    }

    private fun getIntentData() {
        if (requireActivity().intent != null) {
            viewModel.batchCompletionReq = requireActivity().intent.getParcelableExtra(BATCH_COMPLETION_REQ_KEY)!!
            viewModel.batchData = requireActivity().intent.getParcelableExtra(BATCH_DATA)!!
        }
    }

    private fun observeViewModelData() {
        observeBatchCompletionApiResponse()
        observeBatchCompletionStatusList()
        observeSendBatchCompletionResponse()
    }

    private fun observeBatchCompletionApiResponse() {
        viewModel.batchCompletionResponse.observe(this.viewLifecycleOwner, Observer { batchCompletionApiRes ->
            when (batchCompletionApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    batchCompletionApiRes.data?.let { batchCompletionRes ->
                        val bcrReq = batchCompletionRes.batchCompletionResponseData.bcrReq
                        viewModel.batchCompletionStatusList.value = parseBatchCompletionResBatchReq(bcrReq)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }

        })
    }

    private fun observeBatchCompletionStatusList() {
        viewModel.batchCompletionStatusList.observe(this.viewLifecycleOwner, Observer { batchCompletionStatusList ->
            showData(batchCompletionStatusList)
        })
    }

    private fun showData(batchCompletionStatusList: ArrayList<BatchCompletionResBCReq>) {
        binding.viewModel = viewModel

        if (batchCompletionStatusList.isNotEmpty())
            setCountData(batchCompletionStatusList)
    }

    private fun setCountData(statusList: ArrayList<BatchCompletionResBCReq>) {
        val pendingList = statusList.filter { it.bcrReqRespStatus.equals("Pending") || it.bcrReqRespStatus.equals("null") }
        val acceptedList = statusList.filter { it.bcrReqRespStatus.toLowerCase(Locale.getDefault()).equals("accepted") }
        val rejectedList = statusList.filter { it.bcrReqRespStatus.toLowerCase(Locale.getDefault()).equals("rejected") }

        viewModel.acceptedPercentage.value = getAcceptedPercentge(acceptedList.size, statusList.size)
        viewModel.pendingCount.value = pendingList.size
        viewModel.acceptedCount.value = acceptedList.size
        viewModel.rejectedCount.value = rejectedList.size

        setAdapterData(pendingList)

        Log.d(TAG, "setCountData: Activity Filter Count: ${pendingList.size}, ${acceptedList.size}, ${rejectedList.size}" )
    }

    private fun getAcceptedPercentge(acceptedListSize: Int, totalListSize: Int): Int {
        return acceptedListSize.div(totalListSize).times(100)
    }


    private fun populatePendingStudents(batchCompletionStatusList: ArrayList<BatchCompletionResBCReq>) {
        val pendingList = batchCompletionStatusList.filter {
            it.bcrReqRespStatus.equals("Pending") || it.bcrReqRespStatus.equals("null")
        }
        viewModel.pendingCount.value = pendingList.size
        setAdapterData(pendingList)
    }

    private fun populateAcceptedStudents(batchCompletionStatusList: ArrayList<BatchCompletionResBCReq>) {
        val acceptedList = batchCompletionStatusList.filter {
            it.bcrReqRespStatus.toLowerCase(Locale.getDefault()).equals("accepted")
        }
        viewModel.acceptedCount.value = acceptedList.size
        setAdapterData(acceptedList)
    }

    private fun populateRejectedStudents(batchCompletionStatusList: ArrayList<BatchCompletionResBCReq>) {
        val rejectedList = batchCompletionStatusList.filter {
            it.bcrReqRespStatus.toLowerCase(Locale.getDefault()).equals("rejected")
        }
        viewModel.rejectedCount.value = rejectedList.size
        setAdapterData(rejectedList)
    }

    fun setAdapterData(list: List<BatchCompletionResBCReq>) {
        if(list.isEmpty()){
            binding.batchRecyclerView.visibility = View.GONE
            binding.noDataLayout.visibility = View.VISIBLE
        }else{
            binding.batchRecyclerView.visibility = View.VISIBLE
            binding.noDataLayout.visibility = View.GONE

            adapter.setData(ArrayList(list))
            binding.batchRecyclerView.adapter = adapter
        }

    }

    private fun observeSendBatchCompletionResponse() {
        viewModel.sendBatchCompletionResponse.observe(this.viewLifecycleOwner, Observer { sendBatchApiRes ->
            when (sendBatchApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    viewModel.fetchBatchCompletionRequest()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun parseBatchCompletionResBatchReq(bcrReq: String): ArrayList<BatchCompletionResBCReq> {
        val batchCompletionStatusList = ArrayList<BatchCompletionResBCReq>()

        if (bcrReq != null && !TextUtils.isEmpty(bcrReq)){
            val jsonArray = JSONArray(bcrReq)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val batchCompletionResStatus = parseBatchCompletionResBatchReq(jsonObject)
                batchCompletionStatusList.add(batchCompletionResStatus)
            }
        }
        return batchCompletionStatusList
    }

    fun parseBatchCompletionResBatchReq(jsonObject: JSONObject): BatchCompletionResBCReq {
        val batchCompletionResBCReq = BatchCompletionResBCReq()
        if (jsonObject.has("student_id")) {
            batchCompletionResBCReq.studentId = jsonObject.getInt("student_id")
        }
        if (jsonObject.has("student_name")) {
            batchCompletionResBCReq.studentName = jsonObject.getString("student_name")
        }
        if (jsonObject.has("batch_id")) {
            batchCompletionResBCReq.batchId = jsonObject.getInt("batch_id")
        }
        if (jsonObject.has("bcr_req_id")) {
            batchCompletionResBCReq.bcrReqId = jsonObject.getInt("bcr_req_id")
        }
        if (jsonObject.has("bcr_req_resp_date")) {
            batchCompletionResBCReq.bcrReqRespDate = jsonObject.getString("bcr_req_resp_date")
        }
        if (jsonObject.has("bcr_req_resp_status")) {
            batchCompletionResBCReq.bcrReqRespStatus = jsonObject.getString("bcr_req_resp_status")
        }
        if (jsonObject.has("bcr_req_resp_reason")) {
            batchCompletionResBCReq.bcrReqRespReason = jsonObject.getString("bcr_req_resp_reason")
        }
        return batchCompletionResBCReq
    }

    private fun fetchBatchCompletionResponse() {
        viewModel.fetchBatchCompletionRequest()
    }

    private fun setClickListeners() {
        val courseCompletionCount = viewModel.batchInfo.value?.courseCompletionCount ?: 0
        binding.btnSendRequest.isEnabled = courseCompletionCount > 90
        binding.btnSendRequest.setOnClickListener {
            confirmToSendRequest()
        }

        binding.pendingCountLayout.cardView.setOnClickListener {
            viewModel.batchCompletionStatusList.value?.let { it1 -> populatePendingStudents(it1) }
        }

        binding.acceptedCountLayout.cardView.setOnClickListener {
            viewModel.batchCompletionStatusList.value?.let { it1 -> populateAcceptedStudents(it1) }
        }

        binding.rejectedCountLayout.cardView.setOnClickListener {
            viewModel.batchCompletionStatusList.value?.let { it1 -> populateRejectedStudents(it1) }
        }
    }

    private fun confirmToSendRequest() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Do you want to send request ?")
            .setPositiveButton("Yes, Send", DialogInterface.OnClickListener { dialog, which ->
                if (which == AlertDialog.BUTTON_POSITIVE) {
                    viewModel.sendCompletionRequest()
                } else {
                    dialog.dismiss()
                }
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
        builder.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
//                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val BATCH_COMPLETION_REQ_KEY = "batch_completion_request"
        val BATCH_DATA = "batch_data"

        fun launchActivity(context: Context, batchData: NewMessageData?) {
            val intent = Intent(context, BatchCompletionRequestFragment::class.java)
            val bundle = Bundle()
            val batchId = batchData?.batchId ?: -1
            val courseId = batchData?.courseId ?: -1
            val trainerId = batchData?.trainerId ?: ""
            val batchCompletionRequest = BatchCompletionRequest(batchId, courseId, trainerId.toInt())
            bundle.putParcelable(BATCH_COMPLETION_REQ_KEY, batchCompletionRequest)
            bundle.putParcelable(BATCH_DATA, batchData)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}