package com.kapilguru.trainer.coupons

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
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
    lateinit var adapter: CustomSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_coupons)
        viewModel = ViewModelProvider(this, CouponsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(CouponsViewModel::class.java)
        dialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        binding.model = viewModel
        setCustomActionBar()
        setClickListeners()
//        setCustomAdapterSpinner()
        observeViewModel()
    }

    private fun setClickListeners() {
        binding.calander.setOnClickListener {
            openCalendar()
        }

        binding.aCSpinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        setCategory(0)
                    }
                    1 -> {
                        setCategory(1)
                    }
                    2 -> {
                        setCategory(2)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

    }

    private fun openCalendar() {
        val newFragment: DialogFragment = CustomCalendar(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.coupons))
    }

    override fun onDateSet(calendarSelectedDate: Calendar) {
        val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
        Log.d(TAG, "onDateSet: $abc")
        var finaldate = abc.toDateFormatWithOutT()
        viewModel.varidUpto.value = finaldate
    }

    fun setCustomAdapterSpinner(response: List<CouponLiveCoursesResponseApi>) {
        couponLiveCoursesResponseApi = response as ArrayList<CouponLiveCoursesResponseApi>
        adapter = CustomSpinnerAdapter(this, couponLiveCoursesResponseApi)
        binding.aCSpinnerCategoryValue.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun observeViewModel() {
        setCategory(0)
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

}