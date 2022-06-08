package com.kapilguru.trainer.myClassRoomDetails

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.PARAM_BATCH_ID
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityMyClassDetailsBinding
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqFactory
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqViewModel
import com.kapilguru.trainer.network.RetrofitNetwork

class MyClassDetails : BaseActivity() {

    lateinit var binding: ActivityMyClassDetailsBinding
    lateinit var viewModel: BatchCompletionReqViewModel
    lateinit var pageAdapter: MyClassroomDetailsFragmentAdapter
    private val TAG = "MyClassDetails"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_class_details)
        viewModel = ViewModelProvider(this, BatchCompletionReqFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(BatchCompletionReqViewModel::class.java)
        this.setActionbarBackListener(this, binding.actionbar, getString(R.string.add_batch))
        getIntentData()
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        setTabLayoutPosition()
        registerOnPageChangeCallBacks()
    }

    private fun getIntentData() {
        viewModel.batchId.value = intent.getStringExtra(PARAM_BATCH_ID)
    }

    private fun setFragmentAdapter() {
        pageAdapter = MyClassroomDetailsFragmentAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = pageAdapter
    }

    private fun addTabs() {
        for (i in 0 until pageAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(pageAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
            if (i == 0) {
                binding.tabLayout.selectTab(tab)
            }
        }
    }

    private fun setTabLayoutPosition() {
        val position = intent.getIntExtra("position", 0)
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
        tabBackGroundColors(binding.tabLayout.getTabAt(position), true)
        binding.viewPager.currentItem = position
    }

    private fun addTabSelectListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab, true)
                binding.viewPager.currentItem = tab!!.position
            }


        })
    }


    private fun registerOnPageChangeCallBacks() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
        val view = tab?.customView
        val varView = view?.findViewById<CardView>(R.id.cardView)
        val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
        view?.let {
            if (selected) {
                varView!!.background.setTint(ContextCompat.getColor(view.context, R.color.blue_3))
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            } else {
                varView!!.background.setTint(Color.WHITE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue_3))
            }
        }
    }


    companion object {
        fun launchActivity(batchData: String, activity: Activity, viewPagerPosition: Int) {
            val intent = Intent(activity, MyClassDetails::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_BATCH_ID, batchData)
            intent.putExtras(bundle)
            intent.putExtra("position", viewPagerPosition)
            activity.startActivity(intent)
        }
    }

}