package com.kapilguru.trainer.announcement.newMessage

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.announcement.newMessage.data.*
import com.kapilguru.trainer.announcement.viewModel.AnnouncementViewModel
import com.kapilguru.trainer.convertStringToBase64
import com.kapilguru.trainer.databinding.FragmentNewMessageBinding
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class NewMessageFragment : Fragment() {
    val TAG = "NewMessageFragment"
    val viewModel: AnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentNewMessageBinding
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        getAdminList()
    }

    private fun observeViewModelData() {
        observeAdminListApiRes()
        observeBatchListApiRes()
        observeSendMessageApiResponse()
    }

    private fun observeBatchListApiRes() {
        viewModel.batchListMutLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateBatchSpinner(it)
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeAdminListApiRes() {
        viewModel.adminListMutLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    getBatchList()
                    populateAdminSpinner(it)
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeSendMessageApiResponse() {
        viewModel.sendNewMessageResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    clearInputFields()
                    getSentItemsResponse()
                    Toast.makeText(requireContext(), it.data!!.message, Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(requireContext(), it.data!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun populateBatchSpinner(it: ApiResource<NewMessageResponse>) {
        val list: List<NewMessageData>? = it.data?.data
        val batchAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_textview, list as MutableList<NewMessageData>)
        batchAdapter.setDropDownViewResource(R.layout.item_spinner_textview)
        binding.spinnerBatch.adapter = batchAdapter
        binding.spinnerBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectedBatchId.value = list[position].batchId.toString()
                System.out.println("BatchId: " + viewModel.selectedBatchId.value)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun populateAdminSpinner(adminListRes : ApiResource<GetAdminListRes>) {
        val adminList = adminListRes.data?.adminList
        adminList?.let { adminListNotNull->
            if(adminListNotNull.isNotEmpty()){
                val selectedAdminList = ArrayList<AdminMessageData>()
                selectedAdminList.add(adminListNotNull[0])
                val adminAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_textview, selectedAdminList as MutableList<AdminMessageData>)
                adminAdapter.setDropDownViewResource(R.layout.item_spinner_textview)
                binding.spinnerBatch.adapter = adminAdapter
                binding.spinnerBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        viewModel.selectedAdminId.value = selectedAdminList[position].userId.toString()
                        System.out.println("AdminId: " + viewModel.selectedAdminId.value)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }
        }
    }

    private fun getAdminList(){
        viewModel.getAdminList()
    }

    private fun getBatchList() {
        val userId = StorePreferences(requireContext()).trainerId.toString()
        viewModel.getBatchList(userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_message, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.tvSend.setOnClickListener { sendMessageRequest() }

        binding.tvCancel.setOnClickListener { clearInputFields() }

        binding.radioGroup.setOnCheckedChangeListener { checkedButton, checkedId ->
            if (checkedId == R.id.batch_checkBox) {
                viewModel.batchListMutLiveData.value?.let { populateBatchSpinner(it) }
            } else if (checkedId == R.id.admin_checkBox) {
                viewModel.adminListMutLiveData.value?.let { populateAdminSpinner(it) }
            }
        }
    }

    fun getSentItemsResponse() {
        val userId: String = StorePreferences(requireContext()).trainerId.toString()
        viewModel.getSentItemsResponse(userId)
    }

    private fun clearInputFields() {
        binding.etSubject.setText("")
        binding.etMessage.setText("")
    }

    private fun sendMessageRequest() {
        var receiverType = ""
        var userId = ""
        val senderId: String = StorePreferences(requireContext()).trainerId.toString()
        val receiverId = viewModel.selectedBatchId.value!!
        val subject = viewModel.subjectMutLiveData.value!!
        val message = viewModel.messageMutLiveData.value!!

        if (TextUtils.isEmpty(subject) && TextUtils.isEmpty(message)) {
            showToast("Please enter subject and message")
        } else if (TextUtils.isEmpty(subject)) {
            showToast("Please enter subject")
        } else if (TextUtils.isEmpty(message)) {
            showToast("Please enter message")
        } else {
            if (viewModel.isAdminChecked.value!!) { // A = Admin
                receiverType = "A"
                userId = "13"
                val adminRequest = SendAdminMessageRequest(userId, senderId, String.convertStringToBase64(subject), String.convertStringToBase64(message), receiverType, "U")
                viewModel.sendAdminRequest(adminRequest)
            } else { // B = batch
                receiverType = "B"
                val batchRequest = SendBatchMessageRequest(receiverId, senderId, String.convertStringToBase64(subject), String.convertStringToBase64(message), receiverType, "U")
                viewModel.sendBatchRequest(batchRequest)
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}