package com.kapilguru.trainer.faculty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.AddFacultyItemBinding
import com.kapilguru.trainer.studyMaterial.StudyMaterialListResponseApi

class AddFacultyRecyAdapter(var studyMaterialItemClick: StudyMaterialItemClick): RecyclerView.Adapter<AddFacultyRecyAdapter.Holder>() {


    var listOfItems = arrayListOf<StudyMaterialListResponseApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AddFacultyItemBinding.inflate(
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




  inner class Holder(studyMaterialListItemBinding: AddFacultyItemBinding) : RecyclerView.ViewHolder(studyMaterialListItemBinding.root) {
        var view =studyMaterialListItemBinding
        var cardView = view.cardView
        init {
            cardView.setOnClickListener {
                studyMaterialItemClick.onItemClickListener(listOfItems[absoluteAdapterPosition])
            }
        }

    }

    interface StudyMaterialItemClick {
        fun onItemClickListener(studyMaterialListResponseApi:StudyMaterialListResponseApi)
    }

}