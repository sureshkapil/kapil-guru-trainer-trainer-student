package com.kapilguru.trainer.announcement.sentItems

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
import com.kapilguru.trainer.announcement.viewModel.AnnouncementViewModel
import com.kapilguru.trainer.databinding.FragmentSentItemsBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxItem

class SentItemsFragment : Fragment(), SentItemsToAdapter {
    private val TAG = "SentItemsFragment"
    val viewModel: AnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentSentItemsBinding
    lateinit var adapter: SentItemsAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        getSentItemsResponce()
        observeViewModelData()
    }

    fun getSentItemsResponce() {
        val userId: String = StorePreferences(requireContext()).userId.toString()
        viewModel.getSentItemsResponse(userId)
    }

    fun observeViewModelData() {
        viewModel.sentItemsResponse.observe(this, Observer {
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

    private fun checkAndSetAdapterData(sentItems : List<SentItemsData>?){
        sentItems?.let { sentItemsNotNull ->
            if(sentItemsNotNull.isNotEmpty()){
                adapter.setData(sentItemsNotNull as ArrayList<SentItemsData>)
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
            binding.noDataAvailable.tvNoData.setTextColor(resources.getColor(R.color.black,null))
        }else{
            binding.rvSentItems.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sent_items, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SentItemsAdapter(this as SentItemsToAdapter)
        binding.rvSentItems.adapter = adapter
    }

    override fun getSentItemsList(sentItem: SentItemsData) {
        TODO("Not yet implemented")
    }
}