package com.kapilguru.trainer.studyMaterial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudyMaterialListItemBinding

class StudyMaterialListAdapter: RecyclerView.Adapter<StudyMaterialListAdapter.Holder>() {


    var listOfItems = emptyList<StudyMaterialListResponseApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = StudyMaterialListItemBinding.inflate(
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




    class Holder(studyMaterialListItemBinding: StudyMaterialListItemBinding) : RecyclerView.ViewHolder(studyMaterialListItemBinding.root) {
        var view =studyMaterialListItemBinding
    }

}