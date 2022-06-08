package com.kapilguru.trainer.ui.reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityReportsBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.ui.reports.viewModel.ReportsViewModel
import com.kapilguru.trainer.ui.reports.viewModel.ReportsViewModelFactory

class ReportsActivity : AppCompatActivity() {
    private val TAG = "ReportsActivity"
    lateinit var viewModel: ReportsViewModel
    lateinit var binding: ActivityReportsBinding
    lateinit var fragmentAdapter: ReportsFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reports)
        val fragmentManager = supportFragmentManager
        fragmentAdapter = ReportsFragmentAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = fragmentAdapter
        viewModel = ViewModelProvider(this,ReportsViewModelFactory(
            ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application))
            .get(ReportsViewModel::class.java)
        setSupportActionBar()
        addTabs()
        addTabSelectListener()
        registerOnPageChangeCallBack()
    }

    private fun setSupportActionBar(){
        supportActionBar?.title = getString(R.string.reports)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Courses"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Webinars"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Guest Lectures"))
    }

    private fun addTabSelectListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }
        })
    }

    private fun registerOnPageChangeCallBack() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                true
            }else -> return super.onOptionsItemSelected(item)
        }
    }
}