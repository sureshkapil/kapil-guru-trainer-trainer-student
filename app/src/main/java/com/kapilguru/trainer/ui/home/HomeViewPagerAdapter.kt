package com.kapilguru.trainer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.HomeViewpagerItemBinding

class HomeViewPagerAdapter(val listener : CardClickListener) : RecyclerView.Adapter<HomeViewPagerAdapter.HomeViewPagerViewHolder>() {

    private var homeViewPagerItems = mutableListOf<HomeViewPagerItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewPagerViewHolder {
        val binding = HomeViewpagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewPagerViewHolder, position: Int) {
        holder.binding.dataView = homeViewPagerItems[position]
    }

    override fun getItemCount(): Int {
        return homeViewPagerItems.size
    }

    fun setViewPagerData(homeViewPagerItems: MutableList<HomeViewPagerItem>){
        this.homeViewPagerItems=homeViewPagerItems
        notifyDataSetChanged()

    }

    inner class HomeViewPagerViewHolder(val binding: HomeViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init{
            binding.btnSchedule.setOnClickListener {
                when(bindingAdapterPosition){
                    0->{
                        listener.onCourseClicked()
                    }
                    1->{
                        listener.onWebinarClicked()

                    }else->{
                        listener.onGuestLectureClicked()
                    }
                }
            }
        }
    }

    interface CardClickListener{
        fun onCourseClicked()
        fun onWebinarClicked()
        fun onGuestLectureClicked()
    }
}