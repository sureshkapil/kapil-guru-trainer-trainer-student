package com.kapilguru.trainer.myClassroom.totalActiveBatches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.ItemActiveBatchesBinding

class ActiveBatchesAdapter(val mListener: BatchClickListener) :
    RecyclerView.Adapter<ActiveBatchesAdapter.BatchViewHolder>() {
    private var mBatchList = ArrayList<NewMessageData>()

    fun setData(batchList: ArrayList<NewMessageData>) {
        mBatchList = batchList
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(val binding: ItemActiveBatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.actvOverview.setOnClickListener {
                mListener.onOverViewClicked(mBatchList[bindingAdapterPosition])
            }

            binding.actvStudyMaterial.setOnClickListener {
                mListener.onStudyMaterialClicked(mBatchList[bindingAdapterPosition])
            }

            binding.actvExam.setOnClickListener {
                mListener.onExamClicked(mBatchList[bindingAdapterPosition])
            }

            binding.actvCompleteRequest.setOnClickListener {
                mListener.onCompletionRequestClicked(mBatchList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val binding =
            ItemActiveBatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.binding.batchModel = mBatchList[position]
        holder.binding.root.setOnClickListener {
            mListener.onBatchClicked(mBatchList[position])
        }
    }

    override fun getItemCount(): Int {
        return mBatchList.size
    }

    interface BatchClickListener {
        fun onBatchClicked(batchData: NewMessageData)
        fun onOverViewClicked(batchData: NewMessageData)
        fun onStudyMaterialClicked(batchData: NewMessageData)
        fun onExamClicked(batchData: NewMessageData)
        fun onCompletionRequestClicked(batchData: NewMessageData)
    }
}