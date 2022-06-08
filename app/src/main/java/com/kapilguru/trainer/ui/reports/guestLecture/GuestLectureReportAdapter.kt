package com.kapilguru.trainer.ui.reports.guestLecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemGuestlectreReportBinding
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData

class GuestLectureReportAdapter() : RecyclerView.Adapter<GuestLectureReportAdapter.GuestLectureViewHolder>(){
    private val TAG = "GuestLectureAdapter"
    private val mGuestLectureList = ArrayList<GuestLectureData>()
    private var mLastSelectedPosition = -1

    class GuestLectureViewHolder(val binding : ItemGuestlectreReportBinding) : RecyclerView.ViewHolder(binding.root){

    }

    fun setData(guestLectureList : ArrayList<GuestLectureData>){
        mGuestLectureList.addAll(guestLectureList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestLectureViewHolder {
        val binding = ItemGuestlectreReportBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GuestLectureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestLectureViewHolder, position: Int) {
        holder.binding.guestLectureModel = mGuestLectureList[position]
        holder.binding.root.setOnClickListener {
            if (mLastSelectedPosition == -1) {
                mGuestLectureList[position].shouldShowChild = true
                mLastSelectedPosition = position
                notifyItemChanged(position)
            } else if (mLastSelectedPosition == position) {
                mGuestLectureList[position].shouldShowChild = !mGuestLectureList[position].shouldShowChild
                notifyItemChanged(position)
            } else {
                mGuestLectureList[mLastSelectedPosition].shouldShowChild = false
                mGuestLectureList[position].shouldShowChild = true
                notifyItemChanged(position)
                notifyItemChanged(mLastSelectedPosition)
                mLastSelectedPosition = position
            }
        }
    }

    override fun getItemCount(): Int {
        return mGuestLectureList.size
    }

}