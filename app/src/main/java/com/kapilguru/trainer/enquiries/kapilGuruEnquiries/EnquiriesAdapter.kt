package com.kapilguru.trainer.enquiries.kapilGuruEnquiries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.EnquiriesItemBinding
import com.kapilguru.trainer.enquiries.enquiryStatusUpdate.EnquiryStatusUpdateActivity
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResData
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiryStatusUpdateRequest

//contact is not shown in Kapil Guru Enquiries, is shown in Offline Enquiries.
//contact is not shown in Kapil Guru Enquiries until user updated the status as viewed.
class EnquiriesAdapter(val shouldShowContactBeforeStatusUpdate : Boolean ,val mListener : ClickListeners) : RecyclerView.Adapter<EnquiriesAdapter.EnquiriesViewHolder>() {
    private var enquiriesList = ArrayList<EnquiriesResData>()

    fun setEnquiriesList(list: ArrayList<EnquiriesResData>) {
        enquiriesList = list
        notifyDataSetChanged()
    }

    fun updateIsContactSeen(position : Int){
        enquiriesList[position].isSeen = 1
        enquiriesList[position].status = EnquiryStatusUpdateRequest.ENQUIRY_STATUS_VIEWED
        notifyItemChanged(position)
    }

    inner class EnquiriesViewHolder(val binding: EnquiriesItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.shouldShowContactBeforeStatusUpdate = shouldShowContactBeforeStatusUpdate
            binding.btnShowContact.setOnClickListener {
                mListener.onViewContactClicked(enquiriesList[bindingAdapterPosition],bindingAdapterPosition)
            }
            binding.root.setOnClickListener {
                mListener.onLaunchEnquiryListClicked(enquiriesList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnquiriesViewHolder {
        val binding = EnquiriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EnquiriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EnquiriesViewHolder, position: Int) {
        holder.binding.model = enquiriesList[position]
    }

    override fun getItemCount(): Int {
        return enquiriesList.size
    }

    interface ClickListeners{
        fun onViewContactClicked(enquiry : EnquiriesResData,positionInList: Int)
        fun onLaunchEnquiryListClicked(enquiry : EnquiriesResData)
    }
}