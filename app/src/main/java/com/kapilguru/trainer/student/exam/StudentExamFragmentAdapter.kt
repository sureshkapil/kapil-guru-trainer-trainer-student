package com.kapilguru.trainer.student.exam

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.MyApplication.Companion.context
import com.kapilguru.trainer.R
import com.kapilguru.trainer.student.exam.model.StudentQuestionsRequest
import com.kapilguru.trainer.studentExamBatchResult.StudentReportRequest

class StudentExamFragmentAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle, val studentReportRequest: StudentReportRequest?, val studentQuestionsRequest: StudentQuestionsRequest?
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val TAG = "AnnounceFragAdap"

    var titles = arrayListOf("View Result", "View Answer Sheet")

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StudentViewResultFragment.newInstance(studentReportRequest)
            else -> StudentViewAnswerSheetFragment.newInstance(studentQuestionsRequest)
        }
    }

    fun setCustomTabView(position: Int): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.exams_custom_tab_student, null)
        val header = view.findViewById<View>(R.id.tv_title) as TextView
        header.text = titles[position]
        return view
    }

}