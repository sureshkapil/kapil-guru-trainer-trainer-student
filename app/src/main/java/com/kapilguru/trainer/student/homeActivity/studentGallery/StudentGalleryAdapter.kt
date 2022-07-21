package com.kapilguru.trainer.student.homeActivity.studentGallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentGalleryItemBinding
import com.kapilguru.trainer.databinding.StudentGalleryItemLargeBinding
import com.kapilguru.trainer.student.homeActivity.models.PopularAndTrendingApi
import com.kapilguru.trainer.student.homeActivity.studentGallery.model.ImageResData

class StudentGalleryAdapter(var onItemClick: CardItemClickListener, private val showLargeImage: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var imageList = listOf<ImageResData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class GalleryViewHolderSmall(val binding: StudentGalleryItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class GalleryViewHolderLarge(val binding: StudentGalleryItemLargeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = StudentGalleryItemLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GalleryViewHolderLarge(view)
        } else {
            val view = StudentGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GalleryViewHolderSmall(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as GalleryViewHolderSmall).binding.model = imageList[position]
        } else {
            (holder as GalleryViewHolderLarge).binding.model = imageList[position]
        }
    }

    override fun getItemCount(): Int = imageList.size

    override fun getItemViewType(position: Int): Int {
        return if (showLargeImage) {
            1
        } else {
            0
        }
    }

    interface CardItemClickListener {
        fun onCardClicked(popularAndTrendingApi: PopularAndTrendingApi)
    }
}