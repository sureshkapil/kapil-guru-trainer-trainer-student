package com.kapilguru.trainer.myClassRoomDetails.overView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.ActivityOverViewBinding
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqViewModel
import com.kapilguru.trainer.network.Status

class OverViewFragment : Fragment() {

    lateinit var binding: ActivityOverViewBinding
    val viewModel: BatchCompletionReqViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityOverViewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewModel.batchApiResponse.observe(this.viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.model = it.data?.data?.get(0)
                    viewModel.courseId.value = it.data?.data?.get(0)?.courseId.toString()
                    progressDialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }
        })

//
//        viewModels.batchInfo.observe(this.viewLifecycleOwner, Observer {it->
//            it?.let {
//                binding.model = it
//            }
//        })

        //
        viewModel.batchId.observe(this.viewLifecycleOwner,{ it ->
            it?.let {
                viewModel.getBatchDetails()
            }
        })

    }

    companion object {
        val batch_data = "BATCH_DATA_KEY"

        fun launchActivity(batchData: NewMessageData?, activity: Activity) {
            val intent = Intent(activity, OverViewFragment::class.java)
            val bundle = Bundle()
//            bundle.putParcelable(batch_data, batchData)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }


}