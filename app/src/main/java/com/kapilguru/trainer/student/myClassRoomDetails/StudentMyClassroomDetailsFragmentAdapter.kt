package com.kapilguru.trainer.student.myClassRoomDetails

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.student.myClassRoomDetails.exam.StudentExamListFragment
import com.kapilguru.trainer.student.myClassRoomDetails.overView.StudentOverViewFragment
import com.kapilguru.trainer.student.myClassRoomDetails.review.StudentReviewFragment
import com.kapilguru.trainer.student.myClassRoomDetails.studymaterial.StudentStudyMaterialFragment


class StudentMyClassroomDetailsFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("OverView", "Recordings", "Study\nMaterial", "Exam", "Review")
    var bacthId: String? = null

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StudentOverViewFragment()
            1 -> StudentStudyMaterialFragment()
            2 -> StudentStudyMaterialFragment.newInstance(bacthId)
            3 -> StudentExamListFragment()
            else -> StudentReviewFragment.newInstance()
        }
    }

    fun setCustomTabView(position: Int): View {
        val v: View = LayoutInflater.from(context).inflate(R.layout.class_details_custom_tab, null)
        val header = v.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return v
    }

    fun setBatchId(batchId: String?) {
        this.bacthId = batchId
    }

}