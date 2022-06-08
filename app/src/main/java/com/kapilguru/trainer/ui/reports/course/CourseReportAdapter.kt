package com.kapilguru.trainer.ui.reports.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemCourseReportBinding
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class CourseReportAdapter(private val mListener : OnCourseClickListener) : RecyclerView.Adapter<CourseReportAdapter.CourseViewHolder>() {
    private val TAG = "CourseReportAdapter"
    private val mCourseList = ArrayList<CourseResponse>()

    class CourseViewHolder(val binding: ItemCourseReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    fun setData(courseList: ArrayList<CourseResponse>) {
        mCourseList.addAll(courseList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding =
            ItemCourseReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.binding.course = mCourseList[position]
        holder.binding.mcvCourse.setOnClickListener {
            mListener.onCourseClicked(mCourseList[position])
        }
    }

    override fun getItemCount(): Int {
        return mCourseList.size
    }

    interface OnCourseClickListener {
        fun onCourseClicked(course: CourseResponse)
    }
}