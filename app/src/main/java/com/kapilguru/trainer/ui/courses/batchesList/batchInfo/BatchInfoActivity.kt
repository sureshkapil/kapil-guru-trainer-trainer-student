package com.kapilguru.trainer.ui.courses.batchesList.batchInfo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.viewModel.BatchInfoViewModel
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityBatchInfoBinding


class BatchInfoActivity : AppCompatActivity() {


    lateinit var activityBatchInfoView: ActivityBatchInfoBinding
    lateinit var activityBatchInfoViewModel: BatchInfoViewModel
    lateinit var fragmentAdapter: FragmentAdapter

    private  val TAG = "BatchDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_batch_details)

        activityBatchInfoView = DataBindingUtil.setContentView(this,
            R.layout.activity_batch_info
        )

        activityBatchInfoViewModel = ViewModelProvider(this).get(BatchInfoViewModel::class.java)

        activityBatchInfoView.batchViewModel = activityBatchInfoViewModel
        activityBatchInfoView.lifecycleOwner = this


        val tabLayout = activityBatchInfoView.tabLayout
        val viewPager = activityBatchInfoView.viewPager


        val fragmentManager = supportFragmentManager
        fragmentAdapter =
            FragmentAdapter(
                fragmentManager,
                lifecycle
            )
        viewPager.adapter = fragmentAdapter

        tabLayout.addTab(tabLayout.newTab().setText("Syllabus"))
        tabLayout.addTab(tabLayout.newTab().setText("Student"))
        tabLayout.addTab(tabLayout.newTab().setText("Earnings"))
        tabLayout.addTab(tabLayout.newTab().setText("BatchUpdate"))
        tabLayout.addTab(tabLayout.newTab().setText("Chat"))

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.v(TAG,tab!!.position.toString())
                viewPager.currentItem =  tab.position
            }

        } )

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}