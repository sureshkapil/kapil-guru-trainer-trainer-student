package com.kapilguru.trainer.ui.earnings.amountDetails


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.AmountDetailsRecyclerBinding
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsCourse

class AmountDetailsRecycler : RecyclerView.Adapter<AmountDetailsRecycler.Holder>() {

    var listOFEarningsDetailsCourse = arrayListOf<EarningsDetailsCourse>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val view: AmountDetailsRecyclerBinding) : RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = AmountDetailsRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.model = listOFEarningsDetailsCourse[position]
    }

    override fun getItemCount(): Int = listOFEarningsDetailsCourse.size


}