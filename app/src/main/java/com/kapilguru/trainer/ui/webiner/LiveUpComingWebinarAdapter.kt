package com.kapilguru.trainer.ui.webiner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemLiveUpcomingWebinarBinding
import com.kapilguru.trainer.ui.webiner.model.LiveUpComingWebinarData
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.WebinarDetailsActivity

class LiveUpComingWebinarAdapter(var onItemClickedWeb: OnLiveUpcomingWebinarClickListener) : RecyclerView.Adapter<LiveUpComingWebinarAdapter.LiveUpComingWebinarViewHolder>() {
    var mLiveUpComingWebinarList = ArrayList<LiveUpComingWebinarData>()

    fun setWebinarList(liveUpComingWebinarList: ArrayList<LiveUpComingWebinarData>) {
        this.mLiveUpComingWebinarList = liveUpComingWebinarList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveUpComingWebinarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLiveUpcomingWebinarBinding.inflate(inflater, parent, false)
        return LiveUpComingWebinarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LiveUpComingWebinarViewHolder, position: Int) {
        holder.binding.webinarDataModel = mLiveUpComingWebinarList[position]
        setGoLiveVisibility(holder, position)
    }

    private fun setGoLiveVisibility(holder: LiveUpComingWebinarViewHolder, position: Int) {
        try {
            val isRejected = /*mLiveUpComingWebinarList[position].isRejected*/ 0 // Setting default value, as it is in live upcoming list
            val isVerified = /*mLiveUpComingWebinarList[position].isVerified*/ 1 // settting default value as it is in Live upcoming list
            val shouldShow = WebinarDetailsActivity.checkAndSetGoLiveVisibility(isRejected, isVerified, mLiveUpComingWebinarList[position].startTime, mLiveUpComingWebinarList[position].endTime)
            if (shouldShow) {
                holder.binding.llGoLive.visibility = View.VISIBLE
            } else {
                holder.binding.llGoLive.visibility = View.GONE
            }
        } catch (exception: ArrayIndexOutOfBoundsException) {
            exception.printStackTrace()
        }
    }

    override fun getItemCount() = mLiveUpComingWebinarList.size

    inner class LiveUpComingWebinarViewHolder(val binding: ItemLiveUpcomingWebinarBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            setClickListeners()
        }

        private fun setClickListeners() {
            binding.webinarCardView.setOnClickListener {
                onItemClickedWeb.onItemClick(mLiveUpComingWebinarList[bindingAdapterPosition])
            }
            binding.acivShareIcon.setOnClickListener {
                onItemClickedWeb.onShareClick(mLiveUpComingWebinarList[bindingAdapterPosition].webinarCode)
            }
            binding.rlMembers.setOnClickListener {
                onItemClickedWeb.onStudentClick(mLiveUpComingWebinarList[bindingAdapterPosition].webinarId, mLiveUpComingWebinarList[bindingAdapterPosition].webinarTitle)
            }
            binding.aCIVViewIcon.setOnClickListener {
                onItemClickedWeb.onStudentViewClicked(mLiveUpComingWebinarList[bindingAdapterPosition].webinarId)
            }
        }
    }

    interface OnLiveUpcomingWebinarClickListener {
        fun onItemClick(webinarData: LiveUpComingWebinarData)
        fun onShareClick(webinarCode: String)
        fun onStudentClick(webinarId: Int, title: String)
        fun onStudentViewClicked(webinarId : Int)
    }
}