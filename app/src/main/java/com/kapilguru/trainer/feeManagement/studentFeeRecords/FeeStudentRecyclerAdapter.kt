package com.kapilguru.trainer.feeManagement.studentFeeRecords

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.OfflineStudentListItemBinding
import com.kapilguru.trainer.databinding.StudentFeeRecordItemBinding

class FeeStudentRecyclerAdapter(var onItemClick:OnItemClick) : RecyclerView.Adapter<FeeStudentRecyclerAdapter.Holder>() {

    var listItem: ArrayList<StudentFeeRecordsResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(courseListItem: StudentFeeRecordItemBinding) : RecyclerView.ViewHolder(courseListItem.root) {
        var binding = courseListItem
        init {
            binding.cardView.setOnClickListener {
                onItemClick.onCardClick(listItem[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = StudentFeeRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.viewModel = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size

    interface OnItemClick {
        fun onCardClick(studentFeeRecordsResponseApi:StudentFeeRecordsResponseApi)
    }

}