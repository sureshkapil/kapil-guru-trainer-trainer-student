package com.kapilguru.trainer.ui.courses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.AdapterBatchlistBinding
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListResponse

class BatchListAdapter(var onItemClicked: OnItemClickedForBatch) : RecyclerView.Adapter<BatchListAdapter.BatchListViewHolder>() {
    var mBatchlist = mutableListOf<BatchListResponse>()
    private var mLastSelectedPosition = -1

    fun setBatchList(batchlist: List<BatchListResponse>) {
        this.mBatchlist = batchlist.toMutableList()
        notifyDataSetChanged()
    }

    fun updateAdapterPosition(position: Int) {
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterBatchlistBinding.inflate(inflater, parent, false)
        return BatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchListViewHolder, position: Int) {
        holder.view.batchViewModel = mBatchlist[position]
        setVisibility(holder, position)
    }

    private fun setVisibility(holder: BatchListViewHolder, position: Int) {
        holder.view.root.setOnClickListener {
            when (mLastSelectedPosition) {
                -1 -> {
                    mLastSelectedPosition = position
                    mBatchlist[position].shouldShowFooter = true
                    notifyItemChanged(position)
                }
                position -> {
                    mBatchlist[position].shouldShowFooter = !mBatchlist[position].shouldShowFooter
                    notifyItemChanged(position)
                }
                else -> {
                    mBatchlist[mLastSelectedPosition].shouldShowFooter = false
                    mBatchlist[position].shouldShowFooter = true
                    notifyItemChanged(mLastSelectedPosition)
                    notifyItemChanged(position)
                    mLastSelectedPosition = position
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return mBatchlist.size
    }

    inner class BatchListViewHolder(val binding: AdapterBatchlistBinding) : RecyclerView.ViewHolder(binding.root) {
        var view = binding
        init {
            setClickListeners()
        }

        private fun setClickListeners() {
            binding.layoutDropDown.acivEdit.setOnClickListener {
                onItemClicked.onEditItemClick(mBatchlist[absoluteAdapterPosition])
            }
            binding.rlStudentCount.setOnClickListener {
                onItemClicked.onStudentsClicked(mBatchlist[absoluteAdapterPosition])
            }
            binding.layoutDropDown.acivDelete.setOnClickListener {
                onItemClicked.onDeleteClick(mBatchlist[absoluteAdapterPosition].batchCode,
                    mBatchlist[absoluteAdapterPosition].batchId.toString(),
                    mBatchlist[absoluteAdapterPosition].studentsCount,absoluteAdapterPosition)
            }

            binding.layoutDropDown.acivShare.setOnClickListener {
                    onItemClicked.onShareClick(mBatchlist[absoluteAdapterPosition].courseCode)
            }
        }
    }

    interface OnItemClickedForBatch {
        fun onEditItemClick(batchDetails: BatchListResponse)
        fun onStudentsClicked(batchListResponse: BatchListResponse)
        fun onDeleteClick(batchCode:String, batchId:String, studentCount:Int, index: Int)
        fun onShareClick(shareText: String)
    }
}