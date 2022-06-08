package com.kapilguru.trainer.ui.courses.batchesList.batchStudents.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.AdapterBatchStudentsBinding
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.models.BatchStudentListResponse

class BatchStudentsAdapter(var mContext: Context) :
    RecyclerView.Adapter<BatchStudentsAdapter.BatchStudentViewHolder>() {

    var batchStudentList = mutableListOf<BatchStudentListResponse>()
    var previousOpenedItemCount = 0

    fun setBatchStudentsList(batchStudentList: List<BatchStudentListResponse>) {
        this.batchStudentList = batchStudentList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchStudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterBatchStudentsBinding.inflate(inflater, parent, false)

        return BatchStudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchStudentViewHolder, position: Int) {
        holder.view.batchStudentsViewModel = batchStudentList[position]
        holder.studentsCardView.setOnClickListener {
            if (holder.studentsDetailsCardView.visibility == View.VISIBLE) {
                holder.studentsDetailsCardView.visibility = View.GONE
                holder.arrowImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)

            } else {
                holder.studentsDetailsCardView.visibility = View.VISIBLE
                holder.arrowImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_18)
            }
        }
    }

    override fun getItemCount(): Int {
        return batchStudentList.size
    }

    class BatchStudentViewHolder(val binding: AdapterBatchStudentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var view = binding
        val studentsCardView = binding.cardViewBatchStudents
        val studentsDetailsCardView = binding.cvStudentBatchDetails
        val arrowImage = binding.aCTVImageView
    }
}