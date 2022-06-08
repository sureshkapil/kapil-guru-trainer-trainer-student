package com.kapilguru.trainer.ui.reports.webinar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemWebinarReportBinding
import com.kapilguru.trainer.ui.webiner.model.ActiveWebinarData

class WebinarReportAdapter() : RecyclerView.Adapter<WebinarReportAdapter.WebinarViewHolder>()  {
    private val mWebinarList = ArrayList<ActiveWebinarData>()
    private var mLastSelectedPosition = -1

    fun setData(webinarList : ArrayList<ActiveWebinarData>){
        mWebinarList.addAll(webinarList)
        notifyDataSetChanged()
    }
    class WebinarViewHolder(val binding : ItemWebinarReportBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebinarViewHolder {
        val binding = ItemWebinarReportBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WebinarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WebinarViewHolder, position: Int) {
        holder.binding.webinarModel = mWebinarList[position]
        holder.binding.root.setOnClickListener {
            if (mLastSelectedPosition == -1) {
                mWebinarList[position].shouldShowChild = true
                mLastSelectedPosition = position
                notifyItemChanged(position)
            } else if (mLastSelectedPosition == position) {
                mWebinarList[position].shouldShowChild = !mWebinarList[position].shouldShowChild
                notifyItemChanged(position)
            } else {
                mWebinarList[mLastSelectedPosition].shouldShowChild = false
                mWebinarList[position].shouldShowChild = true
                notifyItemChanged(position)
                notifyItemChanged(mLastSelectedPosition)
                mLastSelectedPosition = position
            }
        }
    }

    override fun getItemCount(): Int {
        return mWebinarList.size
    }
}