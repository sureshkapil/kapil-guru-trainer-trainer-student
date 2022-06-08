package com.kapilguru.trainer.allSubscription.subscriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.databinding.ItemSubscriptionsBinding

class SubscriptionsListAdapter(var listener: RegisterClickListener) : RecyclerView.Adapter<SubscriptionsListAdapter.SubscriptionViewHolder>() {
    private val TAG = "SubscriptionsListAdapter"
    val mListener = listener
    private var mLastSelectedPosition = -1
    private var mSubscriptionsList = ArrayList<AllSubscriptionsData>()

    fun setData(subscriptionList: ArrayList<AllSubscriptionsData>) {
        mSubscriptionsList = subscriptionList
        notifyDataSetChanged()
    }

    inner class SubscriptionViewHolder(var binding: ItemSubscriptionsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clHeader.setOnClickListener {
                if (mLastSelectedPosition == -1) {
                    mSubscriptionsList[bindingAdapterPosition].shouldShow = true
                    mLastSelectedPosition = bindingAdapterPosition
                    notifyItemChanged(bindingAdapterPosition)
                } else if (mLastSelectedPosition == bindingAdapterPosition) {
                    mSubscriptionsList[bindingAdapterPosition].shouldShow = !mSubscriptionsList[bindingAdapterPosition].shouldShow
                    if (mSubscriptionsList[bindingAdapterPosition].shouldShow) {
                        mLastSelectedPosition = bindingAdapterPosition
                    } else {
                        mLastSelectedPosition = -1
                    }
                    notifyItemChanged(bindingAdapterPosition)
                } else {
                    mSubscriptionsList[mLastSelectedPosition].shouldShow = false
                    notifyItemChanged(mLastSelectedPosition)
                    mLastSelectedPosition = bindingAdapterPosition
                    mSubscriptionsList[mLastSelectedPosition].shouldShow = true
                    notifyItemChanged(mLastSelectedPosition)
                }
            }
            binding.actvRegister.setOnClickListener {
                mListener.onRegisterClicked(mSubscriptionsList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding = ItemSubscriptionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.binding.model = mSubscriptionsList[position]
    }

    override fun getItemCount() = mSubscriptionsList.size

    interface RegisterClickListener {
        fun onRegisterClicked(subscriptionsData: AllSubscriptionsData)
    }
}