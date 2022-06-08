package com.kapilguru.trainer.exams.conductExams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemConductExamsBinding
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse

class ConductExamsAdapter() : RecyclerView.Adapter<ConductExamsAdapter.ConductExamsViewHolder>() {
    private var mCourseList = ArrayList<CourseResponse>()
    private var mLastSelectedPosition = 0

    inner class ConductExamsViewHolder(var binding: ItemConductExamsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rbCourse.setOnClickListener {
                selectOrDeSelectRadioButton(bindingAdapterPosition)
            }
        }

        private fun selectOrDeSelectRadioButton(position: Int) {
            mCourseList[mLastSelectedPosition].isSelected = false
            notifyItemChanged(mLastSelectedPosition)
            mCourseList[position].isSelected = true
            notifyItemChanged(position)
            mLastSelectedPosition = position
        }
    }

    fun setData(courseList: ArrayList<CourseResponse>) {
        mCourseList = courseList
        if (mCourseList.size > 0) {
            mCourseList[0].isSelected = true
        }
        notifyDataSetChanged()
    }

    fun getSelectedCourse(): CourseResponse {
        return mCourseList[mLastSelectedPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConductExamsViewHolder {
        val binding = ItemConductExamsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConductExamsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConductExamsViewHolder, position: Int) {
        holder.binding.course = mCourseList[position]
    }

    override fun getItemCount(): Int {
        return mCourseList.size
    }

    interface CourseClickListener {
        fun onCourseClicked(course: CourseResponse)
    }
}