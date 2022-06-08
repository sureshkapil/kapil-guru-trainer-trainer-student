package com.kapilguru.trainer.ui.reports.batch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemBatchReportBinding
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse

class BatchReportAdapter() : RecyclerView.Adapter<BatchReportAdapter.BatchReportViewHolder>() {
    private var mBatchList = ArrayList<BatchListResponse>()

    fun setData(batchList : ArrayList<BatchListResponse>){
        mBatchList.addAll(batchList)
        notifyDataSetChanged()
    }

    class BatchReportViewHolder(val binding: ItemBatchReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchReportViewHolder {
        val binding = ItemBatchReportBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BatchReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchReportViewHolder, position: Int) {
        holder.binding.batchModel = mBatchList[position]
    }

    override fun getItemCount(): Int {
        return mBatchList.size
    }
}