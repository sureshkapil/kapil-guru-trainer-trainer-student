package com.kapilguru.trainer.student.myClassroom

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.student.myClassroom.liveClasses.StudentLiveClassFragment
import com.kapilguru.trainer.student.myClassroom.totalActiveBatches.StudentActiveCoursesFragment
import com.kapilguru.trainer.student.myClassroom.upComingClasses.StudentUpcomingClassFragment

class StudentMyClassroomFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    var titles = arrayListOf("Live", "Up coming", "Total Active", "Completed")
    var tabSubTitles = arrayListOf("Class", "Class", "Batches", "Courses")
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return StudentLiveClassFragment()
            1 -> return StudentUpcomingClassFragment()
            2 -> return StudentActiveCoursesFragment.newInstance(true)
            else -> return StudentActiveCoursesFragment.newInstance(false)
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