package com.kapilguru.trainer.faculty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.FacultyItemBinding

class FacultyRecyAdapter(var studyMaterialItemClick: FacultyItemClick): RecyclerView.Adapter<FacultyRecyAdapter.Holder>() {


    var listOfItems = arrayListOf<FacultyListResponseApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FacultyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = listOfItems[position]
    }

    override fun getItemCount(): Int = listOfItems.size




  inner class Holder(studyMaterialListItemBinding: FacultyItemBinding) : RecyclerView.ViewHolder(studyMaterialListItemBinding.root) {
        var view =studyMaterialListItemBinding
        var editIcon = view.edit
        init {
            editIcon.setOnClickListener {
                studyMaterialItemClick.onItemClickListener(listOfItems[absoluteAdapterPosition])
            }
        }

    }

    interface FacultyItemClick {
        fun onItemClickListener(studyMaterialListResponseApi:FacultyListResponseApi)
    }

}