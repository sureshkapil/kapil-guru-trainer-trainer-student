package com.kapilguru.trainer.feeManagement.addFeeManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.AdapterInstallmentFullDetailsListItemBinding
import com.kapilguru.trainer.databinding.AdapterInstallmentListItemBinding



class FeeInstallmentsDetailsAdapter(var onItemClick: OnItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listOf: ArrayList<InstallmentsListResponseApi> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // 0 just to add installemnts
    // 1 to add all installment details
    var type: Int = 0
        get() = field
        set(value) {
            field = value
        }

    inner class Holder(var singleItem: AdapterInstallmentListItemBinding) : RecyclerView.ViewHolder(singleItem.root) {
        var adapterView = singleItem

        init {
            adapterView.dueDate.setOnClickListener {
                onItemClick.onDueDateClick(absoluteAdapterPosition, listOf[absoluteAdapterPosition])
            }
        }
    }

    inner class AllDetailsHolder(var singleItem: AdapterInstallmentFullDetailsListItemBinding) : RecyclerView.ViewHolder(singleItem.root) {
        var adapterView = singleItem

        init {
            adapterView.dueDate.setOnClickListener {
                onItemClick.onDueDateClick(absoluteAdapterPosition, listOf[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = AdapterInstallmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Holder(binding)
        } else {
            val allBinding = AdapterInstallmentFullDetailsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AllDetailsHolder(allBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder as
        if (type == 0) {
            (holder as FeeInstallmentsDetailsAdapter.Holder).adapterView.model = listOf[position]
        } else {
            (holder as FeeInstallmentsDetailsAdapter.AllDetailsHolder).adapterView.model = listOf[position]
        }
    }

    override fun getItemCount(): Int = listOf.size

    override fun getItemViewType(position: Int): Int = type

    fun updateDueDate(installmentsListResponseApi: InstallmentsListResponseApi, position: Int) {
        notifyItemChanged(position, installmentsListResponseApi)
    }


    interface OnItemClick {
        fun onDueDateClick(position: Int, installmentsListResponseApi: InstallmentsListResponseApi)
        fun onPaidDateClick(position: Int, installmentsListResponseApi: InstallmentsListResponseApi)
        fun onUpdateClick(position: Int, installmentsListResponseApi: InstallmentsListResponseApi)
    }

}