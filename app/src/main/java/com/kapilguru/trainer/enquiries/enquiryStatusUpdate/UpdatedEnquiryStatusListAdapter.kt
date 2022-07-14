package com.kapilguru.trainer.enquiries.enquiryStatusUpdate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemUpdatedEnquiryListBinding
import com.kapilguru.trainer.enquiries.enquiryStatusUpdate.model.EnquiryUpdatedStatusListResData

class UpdatedEnquiryStatusListAdapter : RecyclerView.Adapter<UpdatedEnquiryStatusListAdapter.UpdatedEnquiryStatusViewHolder>() {
    private val TAG = "UpdatedEnquiryStatusListAdapter"
    private var updatedEnquiryStatusList = ArrayList<EnquiryUpdatedStatusListResData>()

    fun setUpdatedEnquiryStatusList(list: ArrayList<EnquiryUpdatedStatusListResData>) {
        updatedEnquiryStatusList = list
        notifyDataSetChanged()
    }

    inner class UpdatedEnquiryStatusViewHolder(val binding: ItemUpdatedEnquiryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatedEnquiryStatusViewHolder {
        val binding = ItemUpdatedEnquiryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpdatedEnquiryStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpdatedEnquiryStatusViewHolder, position: Int) {
        holder.binding.model = updatedEnquiryStatusList[position]
    }

    override fun getItemCount(): Int {
        return updatedEnquiryStatusList.size
    }
}