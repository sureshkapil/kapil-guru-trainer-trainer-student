package com.kapilguru.trainer.announcement.sentItems

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.announcement.sentItems.data.SentItemsData
import com.kapilguru.trainer.databinding.SentItemsItemBinding

class SentItemsAdapter(val sentItemsListTOAdapters: SentItemsToAdapter) :
    RecyclerView.Adapter<SentItemsAdapter.ItemViewHolder>() {
    private val TAG = "SentItemsAdapter"
    var sendItems = mutableListOf<SentItemsData>()
    var previousTappedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = SentItemsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.view.sendItem = sendItems[position]
        holder.view.position = position
        holder.view.handler = this
    }

    override fun getItemCount(): Int {
        return sendItems.size
    }

    fun setData(data: ArrayList<SentItemsData>) {
        sendItems = data
        notifyDataSetChanged()
    }

    fun dataVisibility(model: SentItemsData, tappedPosition: Int) {
        if (tappedPosition == previousTappedPosition) {
            model.shouldShow.value = sendItems[tappedPosition].shouldShow.value != true
//            notifyItemChanged(tappedPosition)
        } else {
            if (previousTappedPosition == -1) {
                previousTappedPosition = tappedPosition
            }
            sendItems[previousTappedPosition].shouldShow.value = false
//            notifyItemChanged(previousTappedPosition)
            previousTappedPosition = tappedPosition
            model.shouldShow.value = true
//            notifyItemChanged(tappedPosition)
        }
        notifyDataSetChanged()
    }


    class ItemViewHolder(binding: SentItemsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
       /* val TAG = "ItemViewHolder"
        val mBinding = binding
        var tappedPosition: Int = -1
        fun populateViews(data: SentItemsData,position : Int) {
            Log.d(TAG,"populateViews position : "+position)
            mBinding.sendItem = data
        }

        fun setVisibility() {
            val isVisible = mBinding.cvBottom.isVisible
            Log.d(TAG,"setVisibility  isVisible : "+isVisible)
            if (isVisible) {
                mBinding.cvBottom.visibility = View.GONE
                Log.d(TAG,"setVisibility gone applied")
            } else {
                mBinding.cvBottom.visibility = View.VISIBLE
                Log.d(TAG,"setVisibility visibile applied")
            }
        }*/
    }
}

interface SentItemsToAdapter {
    fun getSentItemsList(sentItem: SentItemsData)
}