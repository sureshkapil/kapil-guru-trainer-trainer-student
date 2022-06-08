package com.kapilguru.trainer.announcement

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.*
import com.kapilguru.trainer.announcement.viewModel.AnnouncementViewModel
import com.kapilguru.trainer.announcement.viewModel.AnnouncementViewModelFactory
import com.kapilguru.trainer.databinding.ActivityAnnouncementBinding
import com.kapilguru.trainer.network.RetrofitNetwork

class AnnouncementActivity : BaseActivity() {
    val TAG = "AnnouncementActivity"
    lateinit var fragmentAdapter: AnnouncementFragmentAdapter
    lateinit var binding : ActivityAnnouncementBinding
    lateinit var viewModel : AnnouncementViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setFragmentManager()
        setCustomActionBarListener()
        addTabsAndSetDefault()
        setTabLayoutPosition(intent.getStringExtra(PARAM_IS_FROM))
        tabListeners()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

    }

    // if is from bell icon naviagate to 1st position else 0 tab
    private fun setTabLayoutPosition(isFromBellIcon: String?) {
        isFromBellIcon?.let {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            tabBackGroundColors(binding.tabLayout.getTabAt(1),true)
            binding.viewPager.currentItem = 1
        } ?:run{
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            tabBackGroundColors(binding.tabLayout.getTabAt(0),true)
            binding.viewPager.currentItem = 0
        }
    }

    private fun setFragmentManager() {
        val fragmentManager = supportFragmentManager
        fragmentAdapter = AnnouncementFragmentAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = fragmentAdapter
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

    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_announcement)
        viewModel = ViewModelProvider(
            this, AnnouncementViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application)
        ).get(AnnouncementViewModel::class.java)
        binding.lifecycleOwner = this
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.messages))
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

    private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
        val view = tab?.customView
        val varView = view?.findViewById<CardView>(R.id.cardView)
        val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
        val tabIcon = view?.findViewById<ShapeableImageView>(R.id.img_icon)
        view?.let {
            if (selected) {
                varView!!.background.setTint(Color.BLUE)
                tabIcon!!.drawable.setTint(Color.WHITE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))

            } else {
                varView!!.background.setTint(Color.WHITE)
                tabIcon!!.drawable.setTint(Color.BLUE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
            }
        }
    }
}