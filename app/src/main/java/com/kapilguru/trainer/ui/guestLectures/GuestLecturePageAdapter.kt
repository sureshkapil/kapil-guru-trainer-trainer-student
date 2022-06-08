package com.kapilguru.trainer.ui.guestLectures

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication
import com.kapilguru.trainer.R

class GuestLecturePageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    var titles = arrayListOf("Live/Up coming", "Total Active")
    var tabSubTitles = arrayListOf("Demo Lectures", "Demo Lectures")

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LiveUpComingGuestLecturesFragment()
            else -> ActiveGuestLecturesFragment()
        }
    }

    fun setCustomTabView(position: Int): View {
        val v: View = LayoutInflater.from(MyApplication.context).inflate(R.layout.custom_tab, null)
        val header = v.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        val tag = v.findViewById<View>(R.id.tv_sub_title) as TextView
        tag.setTextSize(TypedValue.COMPLEX_UNIT_SP,8.0f)
        tag.text = tabSubTitles[position]
        return v
    }
}