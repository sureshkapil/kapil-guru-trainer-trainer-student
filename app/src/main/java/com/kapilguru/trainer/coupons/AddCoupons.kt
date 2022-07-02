package com.kapilguru.trainer.coupons

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityAddCouponsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import java.util.*
import kotlin.collections.ArrayList

class AddCoupons : BaseActivity(), CalendarSelectionListener {

    lateinit var binding: ActivityAddCouponsBinding
    lateinit var viewModel: CouponsViewModel
    lateinit var dialog: CustomProgressDialog
    private val TAG = "AddCoupons"
    var couponLiveCoursesResponseApi = arrayListOf<CouponLiveCoursesResponseApi>()
    var studentModelResponseApi = arrayListOf<StudentModelResponseApi>()
    lateinit var adapter: CustomSpinnerAdapter
    lateinit var studentListAdapter: CustomStudentListSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_coupons)
        viewModel = ViewModelProvider(this, CouponsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(CouponsViewModel::class.java)
        dialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        binding.model = viewModel
        getIntentCouponsData()
        setCustomActionBar()
        setClickListeners()
        getStudentList()
        observeViewModel()
    }

    private fun getIntentCouponsData() {
        viewModel.allCouponsResponseListApi = intent.getParcelableArrayListExtra<AllCouponsResponseListApi>(COUPONS_DATA) as ArrayList<AllCouponsResponseListApi>
    }

    private fun setClickListeners() {

        binding.calander.setOnClickListener {
            openCalendar()
        }

        binding.aCSpinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {

                    }
                    1 -> {
                        setCategory(0)
                    }
                    2 -> {
                        setCategory(1)
                    }
                    3 -> {
                        setCategory(2)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.aCSpinnerCategoryValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.courseId.value = couponLiveCoursesResponseApi[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.aCSpinnerStudent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.studentId.value = studentModelResponseApi[position].studentId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.createCoupon.setOnClickListener {
            if(viewModel.validateCreateCoupon()) {
             viewModel.createCouponCode()
            }
        }

    }

    private fun openCalendar() {
        val newFragment: DialogFragment = CustomCalendar(this);
        newFragment.show(supportFragmentManager, "datePicker");
    }

    private fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.add_coupons))
    }

    override fun onDateSet(calendarSelectedDate: Calendar) {
        val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
        val finalDate = abc.toDateFormatWithOutT()
        viewModel.validUpto.value = finalDate
        viewModel.validUptoDateToApi.value = abc
    }

    fun setCustomAdapterSpinner(response: List<CouponLiveCoursesResponseApi>) {
        couponLiveCoursesResponseApi = response as ArrayList<CouponLiveCoursesResponseApi>
        adapter = CustomSpinnerAdapter(this, couponLiveCoursesResponseApi)
        binding.aCSpinnerCategoryValue.adapter = adapter
        adapter.notifyDataSetChanged()
        viewModel.courseId.value = couponLiveCoursesResponseApi[0].id
    }

    private fun observeViewModel() {
//        setCategory(0)
        viewModel.couponLiveCoursesResponse.observe(this, androidx.lifecycle.Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.couponLiveCoursesResponseApi?.let { response ->
                        if (response.isNotEmpty()) {
                            setCustomAdapterSpinner(response)
//                            setAdapterData(response)
                        }

                    }

                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.studentModelResponse.observe(this, androidx.lifecycle.Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.responseStudentModelApi?.let { response ->
                        if (response.isNotEmpty()) {
                            setStudentListCustomAdapterSpinner(response)
//                            setAdapterData(response)
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.createCouponResponse.observe(this, androidx.lifecycle.Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    navigateToCoupons()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun navigateToCoupons() {
        showAppToast(this,"Your Coupon is Successfully added")
        startActivity(Intent(this,CouponsActivity::class.java))
    }

    fun setStudentListCustomAdapterSpinner(response: List<StudentModelResponseApi>) {
        studentModelResponseApi = response as ArrayList<StudentModelResponseApi>
        studentListAdapter = CustomStudentListSpinnerAdapter(this, studentModelResponseApi)
        binding.aCSpinnerStudent.adapter = studentListAdapter
        studentListAdapter.notifyDataSetChanged()
        viewModel.studentId.value = studentModelResponseApi[0].studentId
    }

    private fun setAdapterData() {
//        adapter.couponLiveCoursesResponseApi = response as ArrayList<CouponLiveCoursesResponseApi>
//        adapter.notifyDataSetChanged()
    }


    fun setCategory(id: Int) {
        val trainerId = StorePreferences(this).userId
        val couponCourseCategoryRequest = CouponCourseCategoryRequest(isRecorded = id, trainerId = trainerId)
        viewModel.getCourseList(couponCourseCategoryRequest)
    }

    fun getStudentList() {
        val trainerId = StorePreferences(this).userId
        val tenantId = StorePreferences(this).tenantId
        val studentRequestModel = StudentRequestModel(tenantId,trainerId)
        viewModel.getStudentList(studentRequestModel)
    }

}