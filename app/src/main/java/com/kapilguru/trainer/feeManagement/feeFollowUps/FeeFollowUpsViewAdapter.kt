package com.kapilguru.trainer.feeManagement.feeFollowUps

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.addStudent.coursesStudentList.MyStudentsOnlineFragment
import com.kapilguru.trainer.addStudent.recordedStudentList.MyStudentsRecordedFragment
import com.kapilguru.trainer.addStudent.studyMaterialStudentsList.MyStudentMaterialFragment


class FeeFollowUpsViewAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var titles = arrayListOf("Today", "UpComing")
    var tabSubTitles = arrayListOf("Fee", "Fee")

    override fun getItemCount(): Int = titles.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFeeFollowUpFragment()
            else  -> UpComingFeeFollowUpFragment()
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