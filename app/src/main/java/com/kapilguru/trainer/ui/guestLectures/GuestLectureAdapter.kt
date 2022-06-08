package com.kapilguru.trainer.ui.guestLectures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.GuestLectureItemViewBinding
import com.kapilguru.trainer.ui.guestLectures.guestLectureDetails.GuestLectureDetailsActivity
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData

class GuestLectureAdapter(var isLiveOrUpComing: Boolean, var onItemClickedGuestLecture: OnItemClickGuestLecture) : RecyclerView.Adapter<GuestLectureAdapter.GuestLectureViewHolder>() {
    var guestLectureList = mutableListOf<GuestLectureData>()

    @JvmName("setGuestLectureList1")
    fun setGuestLectureList(guestLectureList: List<GuestLectureData>) {
        this.guestLectureList = guestLectureList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestLectureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GuestLectureItemViewBinding.inflate(inflater, parent, false)
        return GuestLectureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestLectureViewHolder, position: Int) {
        val guestLectureData = guestLectureList[position]
        holder.binding.guestLectureDataModel = guestLectureData
        val isRejected = guestLectureData.isRejected
        val isVerified = guestLectureData.isVerified
        if (GuestLectureDetailsActivity.checkGoLiveVisibility(isRejected, isVerified, guestLectureData.lectureDate, guestLectureData.duration)) {
            holder.binding.actvGoLive.visibility = View.VISIBLE
        } else {
            holder.binding.actvGoLive.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return guestLectureList.size
    }

    inner class GuestLectureViewHolder(val binding: GuestLectureItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            if(isLiveOrUpComing){
                binding.aCIVEditIcon.visibility = View.GONE
                binding.aCIVDeleteIcon.visibility = View.GONE
            }
            setClickListeners()
        }

        private fun setClickListeners() {
            binding.root.setOnClickListener {
                onItemClickedGuestLecture.onItemClick(guestLectureList[bindingAdapterPosition])
            }
            binding.aCIVEditIcon.setOnClickListener {
                onItemClickedGuestLecture.onEditClick(guestLectureList[bindingAdapterPosition])
            }
            binding.aCIVDeleteIcon.setOnClickListener {
                onItemClickedGuestLecture.onDeleteClick(guestLectureList[bindingAdapterPosition])
            }
            binding.acivShareIcon.setOnClickListener {
                onItemClickedGuestLecture.onShareClick(guestLectureList[bindingAdapterPosition].code!!)
            }
            binding.rlMembers.setOnClickListener {
                onItemClickedGuestLecture.onStudentClick(guestLectureList[bindingAdapterPosition].id!!)
            }
            binding.aCIVViewIcon.setOnClickListener {
                onItemClickedGuestLecture.onStudentViewClick(guestLectureList[bindingAdapterPosition].id!!)
            }
        }
    }

    interface OnItemClickGuestLecture {
        fun onItemClick(guestLectureData: GuestLectureData)
        fun onEditClick(guestLectureData: GuestLectureData)
        fun onDeleteClick(guestLectureData: GuestLectureData)
        fun onShareClick(guestLectureCode: String)
        fun onStudentClick(guestLectureId: Int)
        fun onStudentViewClick(guestLectureId: Int)
    }
}