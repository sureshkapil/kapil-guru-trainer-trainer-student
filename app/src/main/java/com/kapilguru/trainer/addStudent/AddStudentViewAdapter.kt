package com.kapilguru.trainer.addStudent

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.addStudent.AddofflineStudent.AddOfflineStudentFragment
import com.kapilguru.trainer.addStudent.addOnlineStudent.AddOnlineStudentFragment


class AddStudentViewAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("Add Student", "Add Student")
    var tabSubTitles = arrayListOf("ONLINE", "OFFLINE")

    override fun getItemCount(): Int = titles.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddOnlineStudentFragment()
            else -> AddOfflineStudentFragment()
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