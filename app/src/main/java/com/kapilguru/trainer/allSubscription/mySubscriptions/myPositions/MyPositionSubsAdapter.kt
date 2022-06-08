package com.kapilguru.trainer.allSubscription.mySubscriptions.myPositions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPositionData
import com.kapilguru.trainer.databinding.ItemMyPositionSubscriptionBinding

class MyPositionSubsAdapter(val listener: PositionRenewClickListener) : RecyclerView.Adapter<MyPositionSubsAdapter.MyPositionViewHolder>() {
    private val TAG = "MyPositionSubsAdapter"
    private var mMyPositionList = ArrayList<MyPositionData>()
    private val mListener = listener

    fun setData(positionList: ArrayList<MyPositionData>) {
        mMyPositionList = positionList
        notifyDataSetChanged()
    }

    inner class MyPositionViewHolder(val itemBinding: ItemMyPositionSubscriptionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.actvRenewal.setOnClickListener {
                mListener.onRenewClicked(mMyPositionList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPositionViewHolder {
        val binding = ItemMyPositionSubscriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyPositionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPositionViewHolder, position: Int) {
        holder.itemBinding.positionSubData = mMyPositionList[position]
    }

    override fun getItemCount(): Int {
        return mMyPositionList.size
    }

    interface PositionRenewClickListener {
        fun onRenewClicked(myPositionData: MyPositionData)
    }
}