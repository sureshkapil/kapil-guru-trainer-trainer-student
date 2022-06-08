package com.kapilguru.trainer.ui.reports

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.ui.reports.course.CourseFragment
import com.kapilguru.trainer.ui.reports.guestLecture.GuestLectureFragment
import com.kapilguru.trainer.ui.reports.webinar.WebinarFragment

class ReportsFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle)  {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CourseFragment()
            1 -> WebinarFragment()
            else -> GuestLectureFragment()
        }
    }
}