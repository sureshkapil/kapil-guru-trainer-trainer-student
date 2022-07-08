package com.kapilguru.trainer.enquiries

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.KapilGuruEnquiriesFragment
import com.kapilguru.trainer.enquiries.offlineEnquiries.OfflineEnquiriesFragment
import com.kapilguru.trainer.enquiries.todaysFollowUp.TodayFollowUpFragment

class EnquiriesFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("Kapil Guru", "Offline", "Today ")
    var tabSubTitles = arrayListOf("Enquiries", "Enquiries", "Follow Ups ")

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> KapilGuruEnquiriesFragment()
            1 -> OfflineEnquiriesFragment()
            else -> TodayFollowUpFragment()
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