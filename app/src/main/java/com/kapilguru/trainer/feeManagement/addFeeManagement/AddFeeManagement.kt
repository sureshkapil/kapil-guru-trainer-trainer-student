package com.kapilguru.trainer.feeManagement.addFeeManagement

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddFeeManagementBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel
import com.kapilguru.trainer.feeManagement.FeeManagementViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import java.util.*

class AddFeeManagement : BaseActivity() {
    lateinit var binding: ActivityAddFeeManagementBinding
    lateinit var viewModel: FeeManagementViewModel
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_fee_management)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_fee_management)
        viewModel = ViewModelProvider(this, FeeManagementViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(FeeManagementViewModel::class.java)
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        setClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorMessage.observe(this, androidx.lifecycle.Observer {
        it?.let { msg->
            showError(msg)
        }
        })


        viewModel.addFeeDetailsResponse.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                  /*  viewModel.profileMutLiveData.value = it?.data?.data?.get(0)
                    studentProfileData = viewModel.profileMutLiveData.value
                    populateViews()
                    viewModel.getCountryList()*/
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun showError(error: String) {
        showAppToast(this,error)
    }


    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.add_fee_management))
    }

    private fun setClickListeners() {
        binding.submit.setOnClickListener {
            if(viewModel.validateUserInfo()){
                viewModel.addFeeDetails()
            }
        }

        binding.aCTVtTrainerCourseAmount.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(it.trim().isNotEmpty()) {
                    viewModel.totalAmount = it.toString()
                    viewModel.calculateDueAmount()
                }
            }
        }

        binding.aCTVtTrainerPaidAmountValue.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(it.trim().isNotEmpty()) {
                    viewModel.paidAmount = it.toString()
                    viewModel.calculateDueAmount()
                }
            }
        }

        binding.joiningDate.setOnClickListener {
        val mycalendar =  CustomCalendar(object : CalendarSelectionListener {
                override fun onDateSet(calendarSelectedDate: Calendar) {
                    val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
                    val finalDate = abc.toDateFormatWithOutT()
                    viewModel.addFeeManagementRequest.value?.dateOfJoining = abc
                    binding.joiningDate.setText(finalDate)
                }
            },false)
            mycalendar.show(supportFragmentManager, "datePicker");
        }

        binding.dueDate.setOnClickListener {
        val myCalendar=  CustomCalendar(object : CalendarSelectionListener {
                override fun onDateSet(calendarSelectedDate: Calendar) {
                    val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
                    val finalDate = abc.toDateFormatWithOutT()
                    viewModel.addFeeManagementRequest.value?.dueDate = abc
                    binding.dueDate.setText(finalDate)
                }
            },false)
            myCalendar.show(supportFragmentManager, "datePicker",);
        }

    }

    private fun validateAndCalculate() {
//        if (viewModel.dueAmount.value!! > 0 && viewModel.numberOfInstallments.value!! > 0) {
//            setInstalmentLayout(calculateSplitAmount(viewModel.dueAmount.value, viewModel.numberOfInstallments.value), viewModel.numberOfInstallments.value!!)
//            navigateToBottom()
//        }
    }

    private fun navigateToBottom() {
//        binding.scrollView.post {
//            binding.scrollView.fullScroll(View.FOCUS_DOWN)
//        }
    }

    private fun calculateSplitAmount(dueAmount: Double?, numberOfInstallments: Int?): Double {
        return dueAmount!! / numberOfInstallments!!
    }

    private fun setInstalmentLayout(calculateSplitAmount: Double, numberOfInstallments: Int) {
//        for (i in 0 until numberOfInstallments) {
//            val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val v: View = layoutInflater.inflate(R.layout.add_instalment, null)
//            v.instalment_number.text = getString(R.string.instalment, i + 1)
//            v.imageView.setOnClickListener {
//                binding.lLayoutParent.removeView(it.parent as View)
//            }
//            v.aCTVtTrainerSpiltAmount.setText(calculateSplitAmount.toString())
//
//            v.aCTVTrainerDateCalendar.setOnClickListener {
//                val newFragment: DialogFragment = CustomCalendar(object : CalendarSelectionListener {
//                    override fun onDateSet(calendarSelectedDate: Calendar) {
//                        val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
//                        val finaldate = abc.toDateFormatWithOutT()
//                        v.aCTVTrainerDateCalendar.setText(finaldate)
//                    }
//
//                })
//                newFragment.show(supportFragmentManager, "datePicker");
//            }
//
//            binding.lLayoutParent.addView(v)
//            val params = v.layoutParams as LinearLayout.LayoutParams
//            params.setMargins(0, 3, 0, 3)
//            v.layoutParams = params
//        }
    }

}