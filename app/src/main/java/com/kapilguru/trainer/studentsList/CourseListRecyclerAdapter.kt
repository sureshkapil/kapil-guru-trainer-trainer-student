package com.kapilguru.trainer.studentsList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.kapilguru.trainer.databinding.CourseListRecyclerAdapterBinding
import com.kapilguru.trainer.studentsList.model.StudentDetails

class CourseListRecyclerAdapter(val studentListActivityTOAdapters: StudentListActivityTOAdapters) :
    RecyclerView.Adapter<CourseListRecyclerAdapter.Holder>() {

    var studentDetails = mutableListOf<StudentDetails>()
    var previousTappedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = CourseListRecyclerAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return studentDetails.size
    }

    fun setData(data: ArrayList<StudentDetails>) {
        studentDetails = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = studentDetails[position]
        holder.view.position = position
        holder.view.handler = this
    }

    inner class Holder(itemView: CourseListRecyclerAdapterBinding) : RecyclerView.ViewHolder(itemView.root) {
        init {
            itemView.apCTVRaiseComplaint.setOnClickListener {
                raiseComplaint(studentDetails[bindingAdapterPosition].studentId)
            }
        }
        val view = itemView
    }

    fun dataVisibility(model: StudentDetails, tappedPosition: Int) {
        if (tappedPosition == previousTappedPosition) {
            model.shouldShow.value = studentDetails[tappedPosition].shouldShow.value != true
//            notifyItemChanged(tappedPosition)
        } else {
            if (previousTappedPosition == -1) {
                previousTappedPosition = tappedPosition
            }
            studentDetails[previousTappedPosition].shouldShow.value = false
//            notifyItemChanged(previousTappedPosition)
            previousTappedPosition = tappedPosition
            model.shouldShow.value = true
//            notifyItemChanged(tappedPosition)
        }
        notifyDataSetChanged()
    }

    fun raiseComplaint(studentId: Int) {
        studentListActivityTOAdapters.getStudentId(studentId)
    }
}

interface StudentListActivityTOAdapters {
    fun getStudentId(studentsInfo: Int)
}
