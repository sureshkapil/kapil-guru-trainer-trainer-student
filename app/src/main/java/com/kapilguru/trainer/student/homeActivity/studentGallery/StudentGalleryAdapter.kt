package com.kapilguru.trainer.student.homeActivity.studentGallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.AllTrendingItemBinding
import com.kapilguru.trainer.databinding.StudentGalleryItemBinding
import com.kapilguru.trainer.student.homeActivity.models.PopularAndTrendingApi

// isFrom false is DashBoard/PopularAndTrending,viewType-> 0
// isFrom true from AllPopularAndTrending, viewType-> 1
class StudentGalleryAdapter(var onItemClick: CardItem, var isFrom: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listItem = listOf<PopularAndTrendingApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val popularTrendingItemBinding: StudentGalleryItemBinding) : RecyclerView.ViewHolder(popularTrendingItemBinding.root) {
        var view = popularTrendingItemBinding
        var imageView = popularTrendingItemBinding.galleryImage
        init {

        }
    }

    inner class AllPopularAndTrendingHolder(val popularAndTrending: AllTrendingItemBinding) : RecyclerView.ViewHolder(popularAndTrending.root) {
        var view = popularAndTrending
        init {
            view.card.setOnClickListener {
                onItemClick.onCardClick(listItem[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = StudentGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return Holder(view)
        } else {
            val view = AllTrendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AllPopularAndTrendingHolder(view)
        }
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
//            (holder as Holder).view.model = listItem[position]
            when(position) {
//                0 -> (holder as Holder).imageView.setBackgroundResource(R.drawable.gallery_image_one)
//                1 -> (holder as Holder).imageView.setBackgroundResource(R.drawable.gallery_image_two)
//                2 -> (holder as Holder).imageView.setBackgroundResource(R.drawable.gallery_image_three)
//                3 -> (holder as Holder).imageView.setBackgroundResource(R.drawable.gallery_image_four)
//                4 -> (holder as Holder).imageView.setBackgroundResource(R.drawable.gallery_image_five)
            }
        } else {
            (holder as AllPopularAndTrendingHolder).view.model = listItem[position]
        }
    }

    override fun getItemViewType(position: Int): Int = if (isFrom) 1 else 0

    override fun getItemCount(): Int = 5

    interface CardItem {
        fun onCardClick(popularAndTrendingApi: PopularAndTrendingApi)
    }
}