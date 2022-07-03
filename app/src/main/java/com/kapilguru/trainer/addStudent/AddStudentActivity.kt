package com.kapilguru.trainer.addStudent

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityAddStudentBinding
import com.kapilguru.trainer.network.RetrofitNetwork

class AddStudentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddStudentBinding
    lateinit var viewModel: AddStudentViewModel
    lateinit var pageAdapter: AddStudentViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_student)
        viewModel = ViewModelProvider(this, AddStudentViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(AddStudentViewModel::class.java)
        binding.lifecycleOwner = this
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
    }

    private fun setFragmentAdapter() {
        pageAdapter = AddStudentViewAdapter(supportFragmentManager, lifecycle)
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

        })
    }


}