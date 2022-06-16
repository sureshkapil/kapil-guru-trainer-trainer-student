package com.kapilguru.trainer.trainerGallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.TrainerAllGalleryPicksRecyItemBinding

class TrainerAllGalleryPicksReyclerAdapter: RecyclerView.Adapter<TrainerAllGalleryPicksReyclerAdapter.Holder>() {

    var trainerGalleryImagesResponseApi = mutableListOf<TrainerGalleryImagesResponseApi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = TrainerAllGalleryPicksRecyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return trainerGalleryImagesResponseApi.size
    }

    fun setData(data: ArrayList<TrainerGalleryImagesResponseApi>) {
        trainerGalleryImagesResponseApi = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = trainerGalleryImagesResponseApi[position]
    }

    inner class Holder(var viewItem: TrainerAllGalleryPicksRecyItemBinding) : RecyclerView.ViewHolder(viewItem.root) {
        val view = viewItem
    }

}