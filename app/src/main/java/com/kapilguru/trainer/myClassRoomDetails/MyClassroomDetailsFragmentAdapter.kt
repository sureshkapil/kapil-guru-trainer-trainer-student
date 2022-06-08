package com.kapilguru.trainer.myClassRoomDetails

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.BatchCompletionRequestFragment
import com.kapilguru.trainer.myClassRoomDetails.exam.ExamFragment
import com.kapilguru.trainer.myClassRoomDetails.overView.OverViewFragment
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.StudyMaterialFragment


class MyClassroomDetailsFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("OverView", "STUDY\nMATERIAL", "Exams", "COMPLETION\nREQUEST")

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverViewFragment()
            1 -> StudyMaterialFragment()
            2 -> ExamFragment()
            else -> BatchCompletionRequestFragment()
        }
    }

     fun setCustomTabView(position: Int): View {
        val v: View = LayoutInflater.from(context).inflate(R.layout.class_details_custom_tab, null)
        val header = v.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return v
    }

}