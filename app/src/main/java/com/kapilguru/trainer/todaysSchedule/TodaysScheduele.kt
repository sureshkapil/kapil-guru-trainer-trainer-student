package com.kapilguru.trainer.todaysSchedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityTodaysSchedueleBinding
import com.kapilguru.trainer.databinding.FragmentHomeScreenBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.home.HomeScreenViewModel
import com.kapilguru.trainer.ui.home.HomeScreenViewModelFactory
import com.kapilguru.trainer.ui.home.TodayScheduleAdapter
import com.kapilguru.trainer.ui.home.UpComingScheduleApi

class TodaysScheduele : BaseActivity(), TodayScheduleAdapter.OnItemClick {


    lateinit var todayScheduleAdapter: TodayScheduleAdapter
    lateinit var viewModel: HomeScreenViewModel
    lateinit var binding: ActivityTodaysSchedueleBinding
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todays_scheduele)
        viewModel = ViewModelProvider(this, HomeScreenViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(HomeScreenViewModel::class.java)
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        setTodaySchedule()
        setCustomActionBar()
        observerViewModel()
    }

    private fun setTodaySchedule() {
        todayScheduleAdapter = TodayScheduleAdapter(this)
        binding.todaysRecy.adapter = todayScheduleAdapter
        viewModel.fetchUpcomingSchedule()

    }

    fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.todays_schedule))
    }


    private fun observerViewModel() {
        viewModel.upcomingResponse.observe(HomeScreenFragment@ this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { upComingSchedule ->

                        todayScheduleAdapter.upComingScheduleApiList = upComingSchedule
//                      (this.requireActivity().application as MyApplication).initMaintenanceWorker()
//                      (this.requireActivity().application as MyApplication).getPendingIntent(upComingSchedule)
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    override fun onCardClick(upComingScheduleApi: UpComingScheduleApi) {

    }
}