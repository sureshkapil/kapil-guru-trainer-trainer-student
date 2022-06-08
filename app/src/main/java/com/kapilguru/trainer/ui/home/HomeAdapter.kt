package com.kapilguru.trainer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.HomeItemViewBinding

class HomeAdapter(var onItemClickedForHome:OnItemClickedForHome) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

private var homeItems = mutableListOf<HomeItem>()

    override fun getItemCount(): Int {
       return homeItems.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = HomeItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.view.itemData = homeItems[position]
        holder.layoutView.setOnClickListener {
            onItemClickedForHome.onItemClick(position)
        }
    }

    fun setData(homeItems: MutableList<HomeItem>){
        this.homeItems=homeItems
        notifyDataSetChanged()
    }

    class HomeViewHolder(itemView:HomeItemViewBinding):
        RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
        var layoutView= itemView.lLayoutHome
    }

    interface OnItemClickedForHome{
        fun onItemClick(position: Int)

    }
}