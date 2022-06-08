package com.kapilguru.trainer.ui.earnings.webinarAmount


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ReferralDetailsItemBinding
import com.kapilguru.trainer.databinding.WebinarAmountItemBinding

class WebinarAmountRecycler : RecyclerView.Adapter<WebinarAmountRecycler.Holder>() {

    var listOfItems = emptyList<WebinarAmountResponse>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val view: WebinarAmountItemBinding) : RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = WebinarAmountItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.model = listOfItems[position]
    }

    override fun getItemCount(): Int = listOfItems.size

}