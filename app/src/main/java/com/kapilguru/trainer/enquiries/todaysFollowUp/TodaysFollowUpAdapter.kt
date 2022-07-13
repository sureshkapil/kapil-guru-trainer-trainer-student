package com.kapilguru.trainer.enquiries.todaysFollowUp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.EnquiryTodayFollowupBinding
import com.kapilguru.trainer.enquiries.todaysFollowUp.model.TodaysFollowUpResData

class TodaysFollowUpAdapter : RecyclerView.Adapter<TodaysFollowUpAdapter.TodaysFollowUpViewHolder>() {
    private var mTodaysFollowUpList = ArrayList<TodaysFollowUpResData>()

    fun setTodaysFollowUpList(list: ArrayList<TodaysFollowUpResData>) {
        mTodaysFollowUpList = list
        notifyDataSetChanged()
    }

    inner class TodaysFollowUpViewHolder(val binding: EnquiryTodayFollowupBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodaysFollowUpViewHolder {
        val binding = EnquiryTodayFollowupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodaysFollowUpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodaysFollowUpViewHolder, position: Int) {
        holder.binding.model = mTodaysFollowUpList[position]
    }

    override fun getItemCount(): Int {
        return mTodaysFollowUpList.size
    }
}