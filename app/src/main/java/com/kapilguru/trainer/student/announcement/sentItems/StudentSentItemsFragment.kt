package com.kapilguru.trainer.student.announcement.sentItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.announcement.sentItems.data.SentItemsData
import com.kapilguru.trainer.databinding.FragmentSentItemsStudentBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.announcement.sentItems.data.StudentSentItemsData
import com.kapilguru.trainer.student.announcement.viewModel.StudentAnnouncementViewModel

class StudentSentItemsFragment : Fragment() {
    private val TAG = "SentItemsFragment"
    val viewModel: StudentAnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentSentItemsStudentBinding
    lateinit var adapter: StudentSentItemsAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        getSentItemsResponse()
        observeViewModelData()
    }

    fun getSentItemsResponse() {
        val userId: String = StorePreferences(requireContext()).userId.toString()
        viewModel.getSentItemsResponse(userId)
    }

    fun observeViewModelData() {
        viewModel.studentSentItemsResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val response = it.data?.data
                    checkAndSetAdapterData(response)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetAdapterData(sentItems : List<StudentSentItemsData>?){
        sentItems?.let { sentItemsNotNull ->
            if(sentItemsNotNull.isNotEmpty()){
                adapter.setData(sentItemsNotNull as ArrayList<StudentSentItemsData>)
                showOrHideEmptyView(false)
            }else{
                showOrHideEmptyView(true)
            }
        }?: kotlin.run {
            showOrHideEmptyView(true)
        }
    }

    private fun showOrHideEmptyView(shouldShowEmptyView : Boolean){
        if(shouldShowEmptyView){
            binding.rvSentItems.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_sent_messages)
        }else{
            binding.rvSentItems.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sent_items_student, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StudentSentItemsAdapter()
        binding.rvSentItems.adapter = adapter
    }
}