package com.kapilguru.trainer.enquiries.enquiryStatusUpdate

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityEnquiryStatusUpdateBinding
import com.kapilguru.trainer.enquiries.enquiryStatusUpdate.viewModel.EnquiryStatusUpdateViewModel
import com.kapilguru.trainer.enquiries.enquiryStatusUpdate.viewModel.EnquiryStatusUpdateViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*

class EnquiryStatusUpdateActivity : BaseActivity() {
    private val TAG = "EnquiryStatusUpdateActivity"
    lateinit var binding: ActivityEnquiryStatusUpdateBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: EnquiryStatusUpdateViewModel
    lateinit var adapter: UpdatedEnquiryStatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBarListener()
        getIntentData()
        setClickListeners()
        observeViewModelData()
        viewModel.getUpdatedEnquiryStatusList()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enquiry_status_update)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        setSpinnerAdapter()
        viewModel = ViewModelProvider(this, EnquiryStatusUpdateViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(EnquiryStatusUpdateViewModel::class.java)
        binding.viewModel = viewModel
        adapter = UpdatedEnquiryStatusListAdapter()
        binding.recyclerview.adapter = adapter
    }

    private fun setSpinnerAdapter() {
        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.enquiry_status))
        binding.spinnerStatus.adapter = spinnerAdapter
        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    viewModel.updateEnquiryStatusRequest.value?.status = null
                } else {
                    viewModel.updateEnquiryStatusRequest.value?.status = resources.getStringArray(R.array.enquiry_status)[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.enquiries))
    }

    private fun getIntentData() {
        if (intent.hasExtra(ENQUIRY_ID)) {
            intent.getStringExtra(ENQUIRY_ID)?.let { enquiryIdNotNull ->
                viewModel.enquiryId = enquiryIdNotNull
            }
        }
    }

    private fun setClickListeners() {
        binding.tietFollowupDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.tietFollowupTime.setOnClickListener {
            showTimePickerDialog()
        }
        binding.btnUpdateEnquiry.setOnClickListener {
            onUpdateEnquiryClicked()
        }
    }

    private fun showDatePickerDialog() {
        val onDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val selectedMonth = monthOfYear + 1
            val dateToShow = "$dayOfMonth/$selectedMonth/$year"
            binding.tietFollowupDate.setText(dateToShow)
            viewModel.updateEnquiryStatusRequest.value?.selectedDate = dateToShow
        }
        val currentDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.newInstance(onDateSetListener, currentDate)
        datePickerDialog.minDate = currentDate
        datePickerDialog.isCancelable = false
        datePickerDialog.show(supportFragmentManager, "")
    }

    private fun showTimePickerDialog() {
        val onTimeSelectListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val timeToShow = get12HoursTime("$hourOfDay:$minute")
            binding.tietFollowupTime.setText(timeToShow)
            viewModel.updateEnquiryStatusRequest.value?.selectedTime = timeToShow
        }
        val currentTime = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this, onTimeSelectListener, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false)
        timePickerDialog.setCancelable(false)
        timePickerDialog.show()
    }

    private fun onUpdateEnquiryClicked() {
        setReqValues()
        viewModel.checkAndUpdateEnquiry()
    }

    /*enquiry id and created by are set here*/
    private fun setReqValues(){
        viewModel.updateEnquiryStatusRequest.value?.enquiryId = viewModel.enquiryId.toInt()
        viewModel.updateEnquiryStatusRequest.value?.createdBy = StorePreferences(this).userId
    }
    private fun observeViewModelData() {
        observeShowLoadingIndicator()
        observeInformUser()
        observeUpdatedEnquiryStatusList()
    }

    private fun observeShowLoadingIndicator() {
        viewModel.showLoadingIndicator.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                progressDialog.showLoadingDialog()
            } else {
                progressDialog.dismissLoadingDialog()
            }
        })
    }

    private fun observeInformUser() {
        viewModel.informUser.observe(this, Observer { message ->
            showAppToast(this, message)
        })
    }

    private fun observeUpdatedEnquiryStatusList() {
        viewModel.updatedStatusEnquiryList.observe(this, Observer { updatedEnquiryStatusList ->
            adapter.setUpdatedEnquiryStatusList(updatedEnquiryStatusList)
        })
    }

    companion object {
        const val ENQUIRY_ID = "ENQUIRY_ID"

        fun launchActivity(activity: Activity, enquiryId: String) {
            val intent = Intent(activity, EnquiryStatusUpdateActivity::class.java)
            intent.putExtra(ENQUIRY_ID, enquiryId)
            activity.startActivity(intent)
        }
    }
}