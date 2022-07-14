package com.kapilguru.trainer.feeManagement.feeFollowUps

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentFeeFollowUpsBinding
import com.kapilguru.trainer.feeManagement.FeeManagementViewModel

class FeeFollowUpsFragment : Fragment() {

    lateinit var binding: FragmentFeeFollowUpsBinding
    val viewModel: FeeManagementViewModel by viewModels({ requireActivity() })
    lateinit var pageAdapter: FeeFollowUpsViewAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentFeeFollowUpsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        registerOnPageChangeCallBacks()

    }



    private fun setFragmentAdapter() {
        pageAdapter = FeeFollowUpsViewAdapter(parentFragmentManager, lifecycle)
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
        @JvmStatic
        fun newInstance() = FeeFollowUpsFragment()
    }

}