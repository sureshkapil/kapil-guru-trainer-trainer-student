package com.kapilguru.trainer.enquiries

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
import com.kapilguru.trainer.PARAM_IS_FROM_TODAYS_FOLLOWUP
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityEnquiriesBinding
import com.kapilguru.trainer.enquiries.viewModel.EnquiriesViewModel
import com.kapilguru.trainer.enquiries.viewModel.EnquiriesViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork

class EnquiriesActivity : BaseActivity() {
    val TAG = "EnquiriesActivity"
    lateinit var fragmentAdapter: EnquiriesFragmentAdapter
    lateinit var binding: ActivityEnquiriesBinding
    lateinit var viewModel: EnquiriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setFragmentManager()
        setCustomActionBarListener()
        tabListeners()
        addTabsAndSetDefault()
        setTabLayoutPosition()
        registerOnPageChangeCallBacks()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enquiries)
        viewModel = ViewModelProvider(this, EnquiriesViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(EnquiriesViewModel::class.java)
        binding.lifecycleOwner = this
    }

    private fun setFragmentManager() {
        val fragmentManager = supportFragmentManager
        fragmentAdapter = EnquiriesFragmentAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = fragmentAdapter
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.enquiries))
    }

    private fun addTabsAndSetDefault() {
        for (i in 0 until fragmentAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(fragmentAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
        }
    }

    private fun setTabLayoutPosition() {
        val isfromTodaysFollowups = intent?.getBooleanExtra(PARAM_IS_FROM_TODAYS_FOLLOWUP,false)
        if(isfromTodaysFollowups!!){
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(2))
            tabBackGroundColors(binding.tabLayout.getTabAt(2), true)
            binding.viewPager.currentItem = 2
        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            tabBackGroundColors(binding.tabLayout.getTabAt(0), true)
            binding.viewPager.currentItem = 0
        }
    }

    private fun tabListeners() {
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

    private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
        val view = tab?.customView
        val varView = view?.findViewById<CardView>(R.id.cardView)
        val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
        val subTitle = view?.findViewById<TextView>(R.id.tv_sub_title)
        view?.let {
            if (selected) {
                varView!!.background.setTint(Color.BLUE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                subTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            } else {
                varView!!.background.setTint(Color.WHITE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
                subTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
            }
        }
    }

    private fun registerOnPageChangeCallBacks() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }
}