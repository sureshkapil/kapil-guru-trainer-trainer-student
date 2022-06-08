package com.kapilguru.trainer.ui.earnings.referralDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ReferralDetailsItemBinding

class ReferralAmountRecycler : RecyclerView.Adapter<ReferralAmountRecycler.Holder>() {

    var listOfItems = emptyList<ReferralAmount>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val view: ReferralDetailsItemBinding) : RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ReferralDetailsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.model = listOfItems[position]
    }

    override fun getItemCount(): Int = listOfItems.size

}