package com.kapilguru.trainer.ui.earnings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ReferralEarningItemViewBinding
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsReferral

class EarningsDetailsReferralAdapter:RecyclerView.Adapter<EarningsDetailsReferralAdapter.EarningsDetailsReferralViewHolder>() {

    var earningsDetailsReferralList=listOf<EarningsDetailsReferral>()
    var mLastSelectedPosition = -1

    fun setDetailsReferralList(earningsDetailsReferralList: ArrayList<EarningsDetailsReferral>) {
        this.earningsDetailsReferralList=earningsDetailsReferralList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarningsDetailsReferralViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = ReferralEarningItemViewBinding.inflate(inflater, parent, false)

        return EarningsDetailsReferralViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EarningsDetailsReferralViewHolder, position: Int) {
        holder.view.referralData = earningsDetailsReferralList[position]
        holder.parentCard.setOnClickListener {
            when (mLastSelectedPosition) {
                -1 -> {
                    earningsDetailsReferralList[position].shouldShowChild = true
                    mLastSelectedPosition = position
                    notifyItemChanged(position)
                }
                position -> {
                    earningsDetailsReferralList[position].shouldShowChild = !earningsDetailsReferralList[position].shouldShowChild
                    notifyItemChanged(position)
                }
                else -> {
                    earningsDetailsReferralList[mLastSelectedPosition].shouldShowChild = false
                    earningsDetailsReferralList[position].shouldShowChild = true
                    notifyItemChanged(position)
                    notifyItemChanged(mLastSelectedPosition)
                    mLastSelectedPosition = position
                }
            }
        }
    }

    override fun getItemCount() = earningsDetailsReferralList.size

    class EarningsDetailsReferralViewHolder(val binding: ReferralEarningItemViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        var view = binding
        var parentCard = binding.parentCard

    }
}