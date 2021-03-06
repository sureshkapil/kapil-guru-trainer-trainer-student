package com.kapilguru.trainer.feeManagement.paidRecords

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.FeePaidRecordItemBinding

class PaidRecordRecyclerAdapter(var onItemClick: OnItemClick) : RecyclerView.Adapter<PaidRecordRecyclerAdapter.Holder>() {

    var listItem: ArrayList<StudentFeePaidResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(courseListItem: FeePaidRecordItemBinding) : RecyclerView.ViewHolder(courseListItem.root) {
        var binding = courseListItem
        init {
            binding.cardView.setOnClickListener {
                onItemClick.onCardClick(listItem[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FeePaidRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.viewModel = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size

    interface OnItemClick {
        fun onCardClick(studentFeePaidResponseApi: StudentFeePaidResponseApi)
    }

}