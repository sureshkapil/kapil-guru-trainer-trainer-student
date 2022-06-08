package com.kapilguru.trainer.ui.courses.addcourse

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class
AddCoursePageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
  var courseListInfo: ArrayList<CourseResponse>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount() = 5


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddCourseTitleAndDescriptionFragment.newInstance(courseListInfo)
            1 -> AddCourseImageFragment()
            2 -> AddCourseDemoVideoFragment()
            3 -> AddCourseLectureSyllabusFragment()
            else -> AddCourseTrainerInformationFragment()
        }
    }


}