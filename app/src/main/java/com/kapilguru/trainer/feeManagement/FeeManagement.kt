package com.kapilguru.trainer.feeManagement

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
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityFeeManagementBinding
import com.kapilguru.trainer.network.RetrofitNetwork

class FeeManagement : BaseActivity() {

    lateinit var binding: ActivityFeeManagementBinding
    lateinit var viewModel: FeeManagementViewModel
    lateinit var fragmentAdapter: FeeManagementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_fee_management)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee_management)
        viewModel = ViewModelProvider(this, FeeManagementViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application)).get(FeeManagementViewModel::class.java)
        binding.viewModel = viewModel
        setCustomActionBarListener()
        setClickListeners()
        setFragmentManager()
        addTabsAndSetDefault()
        tabListeners()
        registerOnPageChangeCallBacks()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun setFragmentManager() {
        val fragmentManager = supportFragmentManager
        fragmentAdapter = FeeManagementAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = fragmentAdapter
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.fee_management))
    }

    private fun setClickListeners() {
        binding.buttonAdd.setOnClickListener {
            navigateToAddFeeManagement()
        }
    }


    private fun addTabsAndSetDefault() {
        for (i in 0 until fragmentAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(fragmentAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
            if (i == 1) {
                binding.tabLayout.selectTab(tab)
            }
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

    private fun navigateToAddFeeManagement() {
        startActivity(Intent(this, AddFeeManagement::class.java))
    }

    private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
        val view = tab?.customView
        val varView = view?.findViewById<CardView>(R.id.cardView)
        val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
        view?.let {
            if (selected) {
                varView!!.background.setTint(Color.BLUE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))

            } else {
                varView!!.background.setTint(Color.WHITE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
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