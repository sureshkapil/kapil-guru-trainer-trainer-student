package com.kapilguru.trainer.student.announcement.inbox.view

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
import com.kapilguru.trainer.databinding.FragmentStudentInboxBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.announcement.inbox.InboxListTOAdapters
import com.kapilguru.trainer.student.announcement.inbox.StudentInboxRecyclerAdapter
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxItem
import com.kapilguru.trainer.student.announcement.viewModel.StudentAnnouncementViewModel

class InboxList : Fragment(), InboxListTOAdapters {
    val viewModel: StudentAnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentStudentInboxBinding
    lateinit var adapter: StudentInboxRecyclerAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        getInBoxResponse()
        observeViewModelData()
    }

    private fun getInBoxResponse() {
        val userId: String = StorePreferences(requireContext()).userId.toString()
        viewModel.getInboxResponce(userId)
    }

    private fun observeViewModelData() {
        viewModel.resultDat.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val response = it.data?.data
                    checkAndSetAdapterData(response)
                    if (response?.isNotEmpty() == true) response[0].id?.let { it1 -> viewModel.updateLastMessageId(it1) }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetAdapterData(inboxList : List<StudentInboxItem>?){
        inboxList?.let { inboxListNotNull ->
            if(inboxListNotNull.isNotEmpty()){
                adapter.setData(inboxListNotNull as ArrayList<StudentInboxItem>)
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
            binding.rvInbox.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.inbox_empty)
        }else{
            binding.rvInbox.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_inbox, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StudentInboxRecyclerAdapter(this as InboxListTOAdapters)
        binding.rvInbox.adapter = adapter
    }

    override fun getInboxList(studentInboxItem: StudentInboxItem) {

    }
}