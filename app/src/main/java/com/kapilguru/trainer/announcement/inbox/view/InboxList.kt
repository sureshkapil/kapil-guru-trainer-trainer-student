package com.kapilguru.trainer.announcement.inbox.view

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
import com.kapilguru.trainer.announcement.inbox.InboxListTOAdapters
import com.kapilguru.trainer.announcement.inbox.InboxRecyclerAdapter
import com.kapilguru.trainer.announcement.inbox.data.InboxItem
import com.kapilguru.trainer.announcement.viewModel.AnnouncementViewModel
import com.kapilguru.trainer.databinding.FragmentInboxBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxItem

class InboxList : Fragment(), InboxListTOAdapters {

    val viewModel: AnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentInboxBinding
    lateinit var adapter: InboxRecyclerAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        getInboxResponce()
        observeViewModelData()
    }

    fun getInboxResponce() {
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

    private fun checkAndSetAdapterData(inboxList : List<InboxItem>?){
        inboxList?.let { inboxListNotNull ->
            if(inboxListNotNull.isNotEmpty()){
                adapter.setData(inboxListNotNull as ArrayList<InboxItem>)
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
            binding.noDataAvailable.tvNoData.setTextColor(resources.getColor(R.color.black,null))
        }else{
            binding.rvInbox.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = InboxRecyclerAdapter(this as InboxListTOAdapters)
        binding.rvInbox.adapter = adapter
    }

    override fun getInboxList(inboxItem: InboxItem) {
        TODO("Not yet implemented")
    }
}