package com.kapilguru.trainer.feeManagement.addFeeManagement

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddFeeManagementDetailsBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel
import com.kapilguru.trainer.feeManagement.FeeManagementViewModelFactory
import com.kapilguru.trainer.feeManagement.feeFollowUps.FeeFollowUpResponseApi
import com.kapilguru.trainer.feeManagement.paidRecords.StudentFeePaidResponseApi
import com.kapilguru.trainer.feeManagement.studentFeeRecords.StudentFeeRecordsResponseApi
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import java.util.*
import kotlin.collections.ArrayList

class ActivityAddFeeInstallmentsDetails : BaseActivity(), FeeInstallmentsDetailsAdapter.OnItemClick {
    lateinit var binding: ActivityAddFeeManagementDetailsBinding
    lateinit var adapter: FeeInstallmentsDetailsAdapter
    lateinit var viewModel: FeeManagementViewModel
    lateinit var dialog: CustomProgressDialog
    var insertId: String? = null
    var totalAmount: Double? = 0.0
    var paidAmount: Double? = 0.0
    var name: String? = ""
    var type: Int = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_fee_management_details)
        viewModel = ViewModelProvider(this, FeeManagementViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(FeeManagementViewModel::class.java)
        dialog = CustomProgressDialog(this)
        setCustomActionBar()
        getIntentData()
        setClickListeners()
        setRecycler()
        observeViewModel()
        bindViewModelData()
    }

    fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.add_fee_management))
    }


    fun setClickListeners() {

    }

    fun getIntentData() {


        when (intent?.getStringExtra(PARAM_IS_FROM)) {
            PARAM_FEE_RECORDS -> {
                totalAmount = intent?.getParcelableExtra<StudentFeeRecordsResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.totalFee
                paidAmount = intent?.getParcelableExtra<StudentFeeRecordsResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.paidFee
                name = intent?.getParcelableExtra<StudentFeeRecordsResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.name
                insertId = insertId ?: intent?.getParcelableExtra<StudentFeeRecordsResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.id.toString()
                type = 1
            }

            PARAM_PAID_RECORDS -> {
//                totalAmount = intent?.getParcelableExtra<StudentFeePaidResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.totalFee
                totalAmount = 0.0 // paid fee is missing from api
                paidAmount = intent?.getParcelableExtra<StudentFeePaidResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.paidFee
                name = intent?.getParcelableExtra<StudentFeePaidResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.name
                insertId = insertId ?: intent?.getParcelableExtra<StudentFeePaidResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.id.toString()
                type = 1
            }

            PARAM_FEE_FOLLOWUPS -> {
//                totalAmount = intent?.getParcelableExtra<FeeFollowUpResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.totalFee
//                paidAmount = intent?.getParcelableExtra<FeeFollowUpResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.paidFee
                totalAmount = 0.0
                paidAmount = 0.0
                name = intent?.getParcelableExtra<FeeFollowUpResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.name
                insertId = insertId ?: intent?.getParcelableExtra<FeeFollowUpResponseApi>(PARAM_FEE_STUDENT_DETAILS)?.id.toString()
                type = 1
            }

            PARAM_ADD_INSTALLMENTS -> {
                insertId = intent?.getStringExtra(PARAM_FEE_INSERTED_ID)
                type = 0
            }


        }


    }

    fun setRecycler() {
        adapter = FeeInstallmentsDetailsAdapter(this)
        binding.recyclerview.adapter = adapter
        adapter.type = type
    }

    private fun observeViewModel() {
        insertId?.let {
            viewModel.getInstallments(it)
        }
        viewModel.installmentListResponse.observe(this, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.installmentsListResponseApi?.let { response ->
                        if (response.isNotEmpty()) {
                            setAdapterData(response)
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun bindViewModelData() {
        binding.studentName = name
        binding.totalAmount = totalAmount
        binding.paidAmount = paidAmount
    }

    private fun setAdapterData(response: List<InstallmentsListResponseApi>) {
        adapter.listOf = response as ArrayList<InstallmentsListResponseApi>
    }

    override fun onDueDateClick(position: Int, installmentsListResponseApi: InstallmentsListResponseApi) {
        val mycalendar = CustomCalendar(object : CalendarSelectionListener {
            override fun onDateSet(calendarSelectedDate: Calendar) {
                val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
                val finalDate = abc.toDateFormatWithOutT()
                installmentsListResponseApi.dueDate = abc
                adapter.updateDueDate(installmentsListResponseApi, position)
            }
        }, false)
        mycalendar.show(supportFragmentManager, "datePicker");
    }

    override fun onPaidDateClick(position: Int, installmentsListResponseApi: InstallmentsListResponseApi) {
        val mycalendar = CustomCalendar(object : CalendarSelectionListener {
            override fun onDateSet(calendarSelectedDate: Calendar) {
                val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
                val finalDate = abc.toDateFormatWithOutT()
                installmentsListResponseApi.paidDate = abc
                adapter.updateDueDate(installmentsListResponseApi, position)
            }
        }, false)
        mycalendar.show(supportFragmentManager, "datePicker");
    }

    override fun onUpdateClick(position: Int, installmentsListResponseApi: InstallmentsListResponseApi) {

    }
}