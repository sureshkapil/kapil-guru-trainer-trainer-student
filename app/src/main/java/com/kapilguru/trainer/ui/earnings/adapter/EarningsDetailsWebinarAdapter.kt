package com.kapilguru.trainer.ui.earnings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.WebinarEarningItemViewBinding
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsWebinar

class EarningsDetailsWebinarAdapter :
    RecyclerView.Adapter<EarningsDetailsWebinarAdapter.EarningsDetailsWebinarViewHolder>() {

    var earningsDetailsWebinarList = listOf<EarningsDetailsWebinar>()
    var mLastSelectedPosition = -1

    fun setDetailsWebinarList(earningsDetailsReferralList: ArrayList<EarningsDetailsWebinar>) {
        this.earningsDetailsWebinarList = earningsDetailsReferralList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EarningsDetailsWebinarViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = WebinarEarningItemViewBinding.inflate(inflater, parent, false)

        return EarningsDetailsWebinarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EarningsDetailsWebinarViewHolder, position: Int) {

        holder.view.earningsDetailsWebinar = earningsDetailsWebinarList[position]
        holder.parentCard.setOnClickListener {
            when (mLastSelectedPosition) {
                -1 -> {
                    earningsDetailsWebinarList[position].shouldShowChild = true
                    mLastSelectedPosition = position
                    notifyItemChanged(position)
                }
                position -> {
                    earningsDetailsWebinarList[position].shouldShowChild = !earningsDetailsWebinarList[position].shouldShowChild
                    notifyItemChanged(position)
                }
                else -> {
                    earningsDetailsWebinarList[mLastSelectedPosition].shouldShowChild = false
                    earningsDetailsWebinarList[position].shouldShowChild = true
                    notifyItemChanged(position)
                    notifyItemChanged(mLastSelectedPosition)
                    mLastSelectedPosition = position
                }
            }
        }
    }

    override fun getItemCount() = earningsDetailsWebinarList.size


    class EarningsDetailsWebinarViewHolder(val binding: WebinarEarningItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var view = binding
        var parentCard = binding.parentCard

    }
}