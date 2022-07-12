package com.kapilguru.trainer.feeManagement.paidRecords

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.FeePaidRecordItemBinding
import com.kapilguru.trainer.databinding.OfflineStudentListItemBinding
import com.kapilguru.trainer.databinding.StudentFeeRecordItemBinding

class PaidRecordRecyclerAdapter : RecyclerView.Adapter<PaidRecordRecyclerAdapter.Holder>() {

    var listItem: ArrayList<StudentFeePaidResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(courseListItem: FeePaidRecordItemBinding) : RecyclerView.ViewHolder(courseListItem.root) {
        var binding = courseListItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FeePaidRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.viewModel = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size


}