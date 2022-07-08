package com.kapilguru.trainer.enquiries.kapilGuruEnquiries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.EnquiriesItemBinding
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResData

class EnquiriesAdapter : RecyclerView.Adapter<EnquiriesAdapter.EnquiriesViewHolder>() {
    private var enquiriesList = ArrayList<EnquiriesResData>()

    fun setEnquiriesList(list: ArrayList<EnquiriesResData>) {
        enquiriesList = list
        notifyDataSetChanged()
    }

    inner class EnquiriesViewHolder(val binding: EnquiriesItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnquiriesViewHolder {
        val binding = EnquiriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EnquiriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EnquiriesViewHolder, position: Int) {
        holder.binding.model = enquiriesList.get(position)
    }

    override fun getItemCount(): Int {
        return enquiriesList.size
    }
}