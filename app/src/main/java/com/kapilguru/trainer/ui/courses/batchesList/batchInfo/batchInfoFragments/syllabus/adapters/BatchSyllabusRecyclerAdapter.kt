package com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.BatchSyllabusRecyclerAdapterBinding
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.models.SyllabusContentItem

class BatchSyllabusRecyclerAdapter :
    RecyclerView.Adapter<BatchSyllabusRecyclerAdapter.ViewHolder>() {


     var dataList:ArrayList<SyllabusContentItem> = ArrayList()

    companion object{
        private const val TAG = "BatchSyllabusRecyclerAd"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DataBindingUtil.inflate<BatchSyllabusRecyclerAdapterBinding>(
            LayoutInflater.from(parent.context),
            R.layout.batch_syllabus_recycler_adapter,
            parent, false
        )
        view.lifecycleOwner = parent.context as LifecycleOwner
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = dataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.viewModel = dataList[position]
    }


   fun setBatchSyllabusRecyclerData(dataList:ArrayList<SyllabusContentItem>) {
       this.dataList.addAll(dataList)
       notifyDataSetChanged()
    }

    class ViewHolder(itemView: BatchSyllabusRecyclerAdapterBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var view = itemView

    }

}