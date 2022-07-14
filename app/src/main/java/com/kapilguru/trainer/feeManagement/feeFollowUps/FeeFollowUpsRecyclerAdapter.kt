package com.kapilguru.trainer.feeManagement.feeFollowUps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.CoursesListItemBinding
import com.kapilguru.trainer.databinding.FeeFollowListItemBinding

class FeeFollowUpsRecyclerAdapter : RecyclerView.Adapter<FeeFollowUpsRecyclerAdapter.Holder>() {

    var listItem: ArrayList<FeeFollowUpResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(courseListItem: FeeFollowListItemBinding) : RecyclerView.ViewHolder(courseListItem.root) {
        var binding = courseListItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FeeFollowListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.viewModel = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size


}