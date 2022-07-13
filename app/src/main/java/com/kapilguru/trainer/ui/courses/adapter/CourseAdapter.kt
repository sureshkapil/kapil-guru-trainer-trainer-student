package com.kapilguru.trainer.ui.courses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.AdapterCourseBinding
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse


class CourseAdapter(var onItemClicked: OnItemClicked) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    private  val TAG = "CourseAdapter"
    var courses = mutableListOf<CourseResponse>()

    fun setCourseList(courses: List<CourseResponse>) {
        this.courses = courses.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterCourseBinding.inflate(inflater, parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.view.viewModel = courses[position]
    }

    override fun getItemCount(): Int {
        return courses.size
    }

  inner  class CourseViewHolder(val binding: AdapterCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var view = binding
        var textView = binding.cardView
        var editIcon = binding.aCIVEditIcon
        var deleteIcon = binding.ivDeleteIcon
        var shareIcon = binding.ivShareIcon
        var studnetsIcon = binding.rlStudents
        var viewIcon = binding.ivViewIcon

        init {
            deleteIcon.setOnClickListener {
                onItemClicked.onDeleteClick(courses[absoluteAdapterPosition])
            }

            textView.setOnClickListener {
                courses[absoluteAdapterPosition].courseId?.let {  courseid ->
                    courses[absoluteAdapterPosition].courseTitle?.let { title ->
                        onItemClicked.onItemClick(title, courseid,courses[absoluteAdapterPosition])
                    }
                }
            }

            editIcon.setOnClickListener {
                onItemClicked.onEditCLick(courses[absoluteAdapterPosition])
            }

            shareIcon.setOnClickListener {
                onItemClicked.onShareClick(courses[absoluteAdapterPosition])
            }

            studnetsIcon.setOnClickListener {
                onItemClicked.onStudentsClick(courses[absoluteAdapterPosition])
            }

            viewIcon.setOnClickListener {
                onItemClicked.onViewClick(courses[absoluteAdapterPosition])
            }

        }

    }

    interface OnItemClicked {
        fun onItemClick(courseTitle: String, courseId: Int, courseResponse: CourseResponse)
        fun onEditCLick(selectedCourse: CourseResponse)
        fun onDeleteClick(selectedCourse: CourseResponse)
        fun onShareClick(selectedCourse: CourseResponse)
        fun onStudentsClick(selectedCourse: CourseResponse)
        fun onViewClick(selectedCourse: CourseResponse)
    }

}