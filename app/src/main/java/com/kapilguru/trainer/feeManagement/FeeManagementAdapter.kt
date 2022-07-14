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
import com.kapilguru.trainer.feeManagement.feeFollowUps.FeeFollowUpsFragment
import com.kapilguru.trainer.feeManagement.paidRecords.PaidRecordsFragment
import com.kapilguru.trainer.feeManagement.studentFeeRecords.FeeRecordsFragment

class FeeManagementAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val TAG = "AnnounceFragAdap"

    var titles = arrayListOf("Fee ", "Paid ", "Fee")
    var tabSubTitles = arrayListOf("Records", "Records", "FollowUps")

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeeRecordsFragment()
            1 -> PaidRecordsFragment()
            else -> FeeFollowUpsFragment()
        }
    }

    fun setCustomTabView(position: Int): View {
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val header = v.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        val tag = v.findViewById<View>(R.id.tv_sub_title) as TextView
        tag.text = tabSubTitles[position]
        return v
    }

}