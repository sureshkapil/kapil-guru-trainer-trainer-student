package com.kapilguru.trainer.student.homeActivity.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.DashBoardStudentViewpagerItemBinding
import com.kapilguru.trainer.student.homeActivity.models.DashBoardViewPagerItem

class DashBoardViewPagerAdapter(val cardClickListener: CardClickListener) : RecyclerView.Adapter<DashBoardViewPagerAdapter.HomeViewPagerViewHolder>() {

    private var homeViewPagerItems = mutableListOf<DashBoardViewPagerItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewPagerViewHolder {
        val view = DashBoardStudentViewpagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewPagerViewHolder, position: Int) {
        holder.view.dataView = homeViewPagerItems[position]
    }

    override fun getItemCount(): Int {
        return homeViewPagerItems.size
    }

    fun setViewPagerData(homeViewPagerItems: MutableList<DashBoardViewPagerItem>) {
        this.homeViewPagerItems = homeViewPagerItems
        notifyDataSetChanged()
    }

    inner class HomeViewPagerViewHolder(itemView: DashBoardStudentViewpagerItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val view = itemView

        init {
            itemView.card.setOnClickListener {
                when (bindingAdapterPosition) {
                    0 -> {
                        cardClickListener.onPopularTrendingClick()
                    }
                    1 -> {
                        cardClickListener.onWebinarClick()

                    }
                    else -> {
                        cardClickListener.onDemoLectureClick()
                    }
                }
            }
        }
    }

}

interface CardClickListener {
    fun onPopularTrendingClick()
    fun onWebinarClick()
    fun onDemoLectureClick()
}