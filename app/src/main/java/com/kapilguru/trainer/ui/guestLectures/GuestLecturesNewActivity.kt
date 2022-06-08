package com.kapilguru.trainer.ui.guestLectures

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
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityGuestLecturesNewBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.AddGuestLectureActivity
import com.kapilguru.trainer.ui.guestLectures.viewModel.GuestLectureViewModel
import com.kapilguru.trainer.ui.guestLectures.viewModel.GuestLectureViewModelFactory
import com.kapilguru.trainer.ui.webiner.BottomSheetDialogFragmentInfo

class GuestLecturesNewActivity : BaseActivity() {
    private val TAG = "GuestLecturesNewActivity"

    lateinit var binding: ActivityGuestLecturesNewBinding
    lateinit var viewModel: GuestLectureViewModel
    lateinit var pageAdapter: GuestLecturePageAdapter
    lateinit var infoBottomSheet: BottomSheetDialogFragmentInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_lectures_new)
        viewModel = ViewModelProvider(this, GuestLectureViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(GuestLectureViewModel::class.java)
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        registerOnPageChangeCallBacks()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == ACTION_LECTURE_ADDED) {
            viewModel.fetchGuestLectureList()
        }
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.demo_lectures), true, object : ToolBarClickListener {
            override fun onShowInfoClicked() {
                showInfo()
            }
        })
    }

    private fun showInfo() {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(getString(R.string.demo_lecture_info_1))
        arrayList.add(getString(R.string.demo_lecture_info_2))
        arrayList.add(getString(R.string.demo_lecture_info_3))
        arrayList.add(getString(R.string.demo_lecture_info_4))
        arrayList.add(getString(R.string.demo_lecture_info_5))
        infoBottomSheet = BottomSheetDialogFragmentInfo.newInstance(arrayList, getString(R.string.schedule_demo_lecture))
        infoBottomSheet.show(supportFragmentManager, "")
    }

    private fun setFragmentAdapter() {
        val fragmentManager = supportFragmentManager
        pageAdapter = GuestLecturePageAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = pageAdapter
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
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.black))
                subTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.black_2))
            }
        }
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

    private fun registerOnPageChangeCallBacks() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    companion object {
        const val ACTION_LECTURE_ADDED = "LECTURE_ADDED"
    }
}