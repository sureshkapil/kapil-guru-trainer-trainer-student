package com.kapilguru.trainer.feeManagement.feeFollowUps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.CoursesListItemBinding
import com.kapilguru.trainer.databinding.FeeFollowListItemBinding
import com.kapilguru.trainer.feeManagement.paidRecords.PaidRecordRecyclerAdapter
import com.kapilguru.trainer.feeManagement.paidRecords.StudentFeePaidResponseApi

class FeeFollowUpsRecyclerAdapter(var onItemClick: OnItemClick) : RecyclerView.Adapter<FeeFollowUpsRecyclerAdapter.Holder>() {

    var listItem: ArrayList<FeeFollowUpResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(courseListItem: FeeFollowListItemBinding) : RecyclerView.ViewHolder(courseListItem.root) {
        var binding = courseListItem
        init {
            binding.cardView.setOnClickListener {
                onItemClick.onCardClick(listItem[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FeeFollowListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.viewModel = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size

    interface OnItemClick {
        fun onCardClick(feeFollowUpResponseApi: FeeFollowUpResponseApi)
    }

}