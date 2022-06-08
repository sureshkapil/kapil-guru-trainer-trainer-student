package com.kapilguru.trainer.ui.earnings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemViewPaymentEarningsBinding
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsPayment

class EarningsDetailsPaymentAdapter : RecyclerView.Adapter<EarningsDetailsPaymentAdapter.EarningsDetailsPaymentViewHolder>() {
    var earningsDetailsPaymentList = listOf<EarningsDetailsPayment>()
    var mLastSelectedPosition = -1

    class EarningsDetailsPaymentViewHolder(val binding: ItemViewPaymentEarningsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var view = binding
    }

    fun setDetailsPaymentList(earningsDetailsPaymentList: ArrayList<EarningsDetailsPayment>) {
        this.earningsDetailsPaymentList = earningsDetailsPaymentList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EarningsDetailsPaymentViewHolder {
        val binding = ItemViewPaymentEarningsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EarningsDetailsPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EarningsDetailsPaymentViewHolder, position: Int) {
        holder.view.payment = earningsDetailsPaymentList[position]
        holder.view.root.setOnClickListener {
            when (mLastSelectedPosition) {
                -1 -> {
                    earningsDetailsPaymentList[position].shouldShowChild = true
                    mLastSelectedPosition = position
                    notifyItemChanged(position)
                }
                position -> {
                    earningsDetailsPaymentList[position].shouldShowChild = !earningsDetailsPaymentList[position].shouldShowChild
                    notifyItemChanged(position)
                }
                else -> {
                    earningsDetailsPaymentList[mLastSelectedPosition].shouldShowChild = false
                    earningsDetailsPaymentList[position].shouldShowChild = true
                    notifyItemChanged(position)
                    notifyItemChanged(mLastSelectedPosition)
                    mLastSelectedPosition = position
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return earningsDetailsPaymentList.size
    }
}