package com.kapilguru.trainer.demo_webinar_students

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.DemoStudentListBinding
import com.kapilguru.trainer.databinding.WebinarStudentListBinding

class DemoWebinarStudentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var batchExamStudentsList = listOf<DemoWebinarStudentsApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // set 0 for Webinar and 1 for Guest Lecture
    var type : Int = 0
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(type==0) {
            val inflater = LayoutInflater.from(parent.context)
            val binding = WebinarStudentListBinding.inflate(inflater, parent, false)
            Holder(binding)
        } else  {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DemoStudentListBinding.inflate(inflater, parent, false)
            DemoLectureHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if(type==0) {
           (holder as Holder).view.model = batchExamStudentsList[position]
       } else  {
           (holder as DemoLectureHolder).view.model = batchExamStudentsList[position]
       }
    }

    override fun getItemViewType(position: Int): Int =  type


    override fun getItemCount(): Int = batchExamStudentsList.size

    class Holder(itemView: WebinarStudentListBinding) : RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
    }


    class DemoLectureHolder(itemView: DemoStudentListBinding) : RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
    }

}
