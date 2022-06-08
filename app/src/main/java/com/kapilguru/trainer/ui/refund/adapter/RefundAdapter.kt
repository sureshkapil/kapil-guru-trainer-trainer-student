package com.kapilguru.trainer.ui.refund.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.RefundListItemBinding
import com.kapilguru.trainer.ui.refund.model.RefundData

class RefundAdapter(var onItemClicked:onItemClickRefund): RecyclerView.Adapter<RefundAdapter.RefundViewHolder>() {

    var refundList = mutableListOf<RefundData>()

    @JvmName("setRefundList1")
    fun setRefundList(refundList: List<RefundData>){
        this.refundList=refundList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefundViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding =RefundListItemBinding.inflate(inflater, parent, false)

        return RefundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RefundViewHolder, position: Int) {
        holder.view.reViewModel = refundList[position]
        holder.textView.setOnClickListener {
            onItemClicked.onItemClick(refundList[position])
        }
    }

    override fun getItemCount(): Int {
       return refundList.size
    }

    class RefundViewHolder(val binding: RefundListItemBinding):
        RecyclerView.ViewHolder(binding.root){
        var view = binding
        var textView = binding.refundCardView
    }

    interface onItemClickRefund{
        fun onItemClick(refundData: RefundData)
    }
}
