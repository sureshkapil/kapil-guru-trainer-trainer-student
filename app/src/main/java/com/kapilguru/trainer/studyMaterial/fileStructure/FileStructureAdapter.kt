package com.kapilguru.trainer.studyMaterial.fileStructure

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.FileItemBinding
import com.kapilguru.trainer.studyMaterial.StudyMaterialListResponseApi


class FileStructureAdapter(var fileItemClick: FileItemClick): RecyclerView.Adapter<FileStructureAdapter.Holder>() {


    var listOfItems = arrayListOf<FolderContentResponseApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FileItemBinding.inflate(
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




    inner class Holder(fileItemBinding: FileItemBinding) : RecyclerView.ViewHolder(fileItemBinding.root) {
        var view =fileItemBinding
        var cardView = view.apCTVFileName
        init {
            cardView.setOnClickListener {
                fileItemClick.onItemClickListener(listOfItems[absoluteAdapterPosition])
            }
        }

    }

    interface FileItemClick {
        fun onItemClickListener(folderContentResponseApi: FolderContentResponseApi)
    }

}