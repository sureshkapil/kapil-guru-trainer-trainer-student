package com.kapilguru.trainer.ui.webiner


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.WebinarItemViewBinding
import com.kapilguru.trainer.ui.webiner.model.ActiveWebinarData
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.WebinarDetailsActivity

class WebinarAdapter(var onItemClickedWeb: OnItemClickWebinar) : RecyclerView.Adapter<WebinarAdapter.WebinarViewHolder>() {
    var mWebinarList = ArrayList<ActiveWebinarData>()

    fun setWebinarList(webinarList: ArrayList<ActiveWebinarData>) {
        this.mWebinarList = webinarList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebinarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WebinarItemViewBinding.inflate(inflater, parent, false)
        return WebinarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WebinarViewHolder, position: Int) {
        holder.binding.webinarDataModel = mWebinarList[position]
        setGoLiveVisibility(holder,position)
    }

    private fun setGoLiveVisibility(holder: WebinarViewHolder,position: Int) {
        try {
            val isRejected = mWebinarList[position].isRejected
            val isVerified = mWebinarList[position].isVerified
            val shouldShow = WebinarDetailsActivity.checkAndSetGoLiveVisibility(isRejected,isVerified,mWebinarList[position].startDate,mWebinarList[position].endDate)
            Log.d("TAG", "setGoLiveVisibility: shouldShow : "+shouldShow)
            if (shouldShow) {
                holder.binding.llGoLive.visibility = View.VISIBLE
            } else {
                holder.binding.llGoLive.visibility = View.GONE
            }
        } catch (exception: ArrayIndexOutOfBoundsException) {
            exception.printStackTrace()
        }
    }

    override fun getItemCount() = mWebinarList.size

    inner class WebinarViewHolder(val binding: WebinarItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            setClickListeners()
        }

        private fun setClickListeners() {
            binding.webinarCardView.setOnClickListener {
                onItemClickedWeb.onItemClick(mWebinarList[bindingAdapterPosition])
            }
            binding.aCIVEditIcon.setOnClickListener {
                val webinarId = mWebinarList[bindingAdapterPosition].id
                onItemClickedWeb.onEditCLick(mWebinarList[bindingAdapterPosition], webinarId)
            }
            binding.aCIVDeleteIcon.setOnClickListener {
                val webinarId = mWebinarList[bindingAdapterPosition].id
                val webinarTitle = mWebinarList[bindingAdapterPosition].title
                val webinarStatus = mWebinarList[bindingAdapterPosition].status
                onItemClickedWeb.onDeleteClick(webinarId, webinarTitle!!, webinarStatus!!,mWebinarList[bindingAdapterPosition])
            }
            binding.acivShareIcon.setOnClickListener {
                onItemClickedWeb.onShareClick(mWebinarList[bindingAdapterPosition].code!!)
            }
            binding.rlMembers.setOnClickListener {
                onItemClickedWeb.onStudentClick(mWebinarList[bindingAdapterPosition].id, mWebinarList[bindingAdapterPosition].title!!)
            }
            binding.aCIVViewIcon.setOnClickListener {
                onItemClickedWeb.onStudentViewClicked(mWebinarList[bindingAdapterPosition].id)
            }
        }
    }

    interface OnItemClickWebinar {
        fun onItemClick(webinarData: ActiveWebinarData)
        fun onEditCLick(webinarData: ActiveWebinarData, webinarId: Int)
        fun onDeleteClick(webinarId: Int, webinarTitle: String, webinarStatus: String, webinarData: ActiveWebinarData)
        fun onShareClick(webinarCode: String)
        fun onStudentClick(webinarId: Int, title: String)
        fun onStudentViewClicked(webinarId: Int)
    }
}