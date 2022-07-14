package com.kapilguru.trainer.ui.earnings.earningsDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.CoursesListItemBinding
import com.kapilguru.trainer.databinding.EarningsDetailItemBinding

class TrainerEarningsRecyclerAdapter : RecyclerView.Adapter<TrainerEarningsRecyclerAdapter.Holder>() {

    var listItem: ArrayList<EarningsDetailsResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(courseListItem: EarningsDetailItemBinding) : RecyclerView.ViewHolder(courseListItem.root) {
        var binding = courseListItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = EarningsDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.viewModel = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size


}