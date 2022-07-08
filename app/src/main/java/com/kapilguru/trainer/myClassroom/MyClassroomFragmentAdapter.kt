package com.kapilguru.trainer.myClassroom

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.myClassroom.liveClasses.LiveClassFragment
import com.kapilguru.trainer.myClassroom.totalActiveBatches.ActiveCoursesFragment
import com.kapilguru.trainer.myClassroom.upComingClasses.UpcomingClassFragment


class MyClassroomFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("Live", "Up coming", "Total Active")
    var tabSubTitles = arrayListOf("Class", "Class", "Batches")

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LiveClassFragment()
            1 -> UpcomingClassFragment()
            else -> ActiveCoursesFragment()
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