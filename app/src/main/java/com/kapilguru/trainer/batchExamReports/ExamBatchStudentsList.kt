package com.kapilguru.trainer.batchExamReports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ExamBatchStudentsListBinding

class ExamBatchStudentsAdapter(val batchStudentsItem: ExamBatchStudentsAdapterCard) : RecyclerView.Adapter<ExamBatchStudentsAdapter.Holder>() {

    var batchExamStudentsList = listOf<BatchStudentsItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val binding: ExamBatchStudentsListBinding) : RecyclerView.ViewHolder(binding.root) {
        var view = binding

        init {
            view.cardView.setOnClickListener {
                batchStudentsItem.onCardClick(batchExamStudentsList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = DataBindingUtil.inflate<ExamBatchStudentsListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.exam_batch_students_list,
            parent, false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.model = batchExamStudentsList[position]

    }

    override fun getItemCount() = batchExamStudentsList.size


    interface ExamBatchStudentsAdapterCard {
        fun onCardClick(batchStudentsItem: BatchStudentsItem)
    }
}