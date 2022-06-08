package com.kapilguru.trainer.exams.assignExamToBatch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemBatchByCourseBinding
import com.kapilguru.trainer.exams.assignExamToBatch.model.BatchByCourseResData

class BatchByCourseListAdapter() : RecyclerView.Adapter<BatchByCourseListAdapter.BatchByCourseViewHolder>() {
    private var mBatchByCourseList = ArrayList<BatchByCourseResData>()
    private var mLastSelectedPosition = 0

    fun getSelectedBatch() = mBatchByCourseList[mLastSelectedPosition]

    fun setData(batchByCourseList : ArrayList<BatchByCourseResData>){
        mBatchByCourseList = batchByCourseList
        notifyDataSetChanged()
    }

    inner class BatchByCourseViewHolder(val binding : ItemBatchByCourseBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.cbBatch.setOnClickListener {
                selectOrDeSelectCheckBox(bindingAdapterPosition)
            }
        }

        private fun selectOrDeSelectCheckBox(position: Int){
            mBatchByCourseList[mLastSelectedPosition].isSelected = false
            notifyItemChanged(mLastSelectedPosition)
            mBatchByCourseList[position].isSelected = true
            notifyItemChanged(position)
            mLastSelectedPosition = position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchByCourseViewHolder {
        val binding = ItemBatchByCourseBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BatchByCourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchByCourseViewHolder, position: Int) {
        holder.binding.model = mBatchByCourseList[position]
    }

    override fun getItemCount(): Int {
       return mBatchByCourseList.size
    }
}