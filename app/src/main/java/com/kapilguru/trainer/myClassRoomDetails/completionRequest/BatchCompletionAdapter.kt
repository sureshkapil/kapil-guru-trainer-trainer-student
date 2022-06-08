package com.kapilguru.trainer.myClassRoomDetails.completionRequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.ItemActiveBatchesBinding
import com.kapilguru.trainer.databinding.ItemBatchStudentsListBinding
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionResBCReq
import java.util.ArrayList

class BatchCompletionAdapter() :
    RecyclerView.Adapter<BatchCompletionAdapter.BatchViewHolder>() {
    private var mBatchList = ArrayList<BatchCompletionResBCReq>()

    fun setData(batchList: ArrayList<BatchCompletionResBCReq>) {
        mBatchList = batchList
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(val binding: ItemBatchStudentsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val binding = ItemBatchStudentsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.binding.batchModel = mBatchList[position]
    }

    override fun getItemCount(): Int {
        return mBatchList.size
    }

}