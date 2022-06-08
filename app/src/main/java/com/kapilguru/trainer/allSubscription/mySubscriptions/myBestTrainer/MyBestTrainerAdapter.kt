package com.kapilguru.trainer.allSubscription.mySubscriptions.myBestTrainer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyBestTrainerData
import com.kapilguru.trainer.databinding.ItemMyBestTrainerSubscriptionBinding

class MyBestTrainerAdapter(val listerner : RenewMyBestTrainerClickListener) : RecyclerView.Adapter<MyBestTrainerAdapter.MyBestTrainerViewHolder>() {
    private val TAG = "MyBestTrainerAdapter"
    val mMyBestTrainerList = ArrayList<MyBestTrainerData>()
    val mListener = listerner
    var mLastSelectedPosition = -1

    inner class MyBestTrainerViewHolder(val itemBinding: ItemMyBestTrainerSubscriptionBinding) : RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemBinding.actvRenewal.setOnClickListener {
                mListener.onRenewBestTrainerClicked(mMyBestTrainerList[bindingAdapterPosition])
            }
        }
    }

    fun setData(myBestTrainerList: ArrayList<MyBestTrainerData>) {
        mMyBestTrainerList.addAll(myBestTrainerList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyBestTrainerViewHolder {
        val binding = ItemMyBestTrainerSubscriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return MyBestTrainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBestTrainerViewHolder, position: Int) {
        holder.itemBinding.bestTrainer = mMyBestTrainerList.get(position)
        holder.itemBinding.root.setOnClickListener {
            if (mLastSelectedPosition == -1) {
                mMyBestTrainerList.get(position).shouldShowFooter = true
                mLastSelectedPosition = position
                notifyItemChanged(position)
            } else if (mLastSelectedPosition == position) {
                mMyBestTrainerList.get(position).shouldShowFooter = !mMyBestTrainerList.get(position).shouldShowFooter
                notifyItemChanged(position)
            } else {
                mMyBestTrainerList.get(mLastSelectedPosition).shouldShowFooter = false
                mMyBestTrainerList.get(position).shouldShowFooter = true
                notifyItemChanged(position)
                notifyItemChanged(mLastSelectedPosition)
                mLastSelectedPosition = position
            }
        }
    }

    override fun getItemCount(): Int {
        return mMyBestTrainerList.size
    }

    interface RenewMyBestTrainerClickListener{
        fun onRenewBestTrainerClicked(myBestTrainerData : MyBestTrainerData)
    }
}