package com.kapilguru.trainer.feeManagement

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R

class FeeManagementAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val TAG = "AnnounceFragAdap"

    var titles = arrayListOf("Fee Followups", "Fee Records")

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeeFollowUps()
            else -> FeeRecords()
        }
    }

    fun setCustomTabView(position: Int): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fee_management_custom_tab, null)
        val header = view.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return view
    }

}