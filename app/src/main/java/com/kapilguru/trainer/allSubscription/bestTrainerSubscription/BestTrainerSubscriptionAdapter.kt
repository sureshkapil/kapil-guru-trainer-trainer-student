package com.kapilguru.trainer.allSubscription.bestTrainerSubscription

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.BestTrainerCourseData
import com.kapilguru.trainer.databinding.ItemBestTrainerSubscriptionBinding

class BestTrainerSubscriptionAdapter(val listener: ItemClickListener) :
    RecyclerView.Adapter<BestTrainerSubscriptionAdapter.BestTrainerViewHolder>() {
    private val mBestTrainerCourseList = ArrayList<BestTrainerCourseData>()

    class BestTrainerViewHolder(val itemBinding: ItemBestTrainerSubscriptionBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    fun setBestTrainerCourseList(list: ArrayList<BestTrainerCourseData>) {
        mBestTrainerCourseList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestTrainerViewHolder {
        val binding = ItemBestTrainerSubscriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BestTrainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestTrainerViewHolder, position: Int) {
        holder.itemBinding.model = mBestTrainerCourseList[position]
        holder.itemBinding.actvRenewOrSubsc.setOnClickListener {
            if (mBestTrainerCourseList.get(position).isBadgeBought) {
                listener.onRenewClicked(mBestTrainerCourseList[position])
            } else {
                listener.onSubscribeClicked(mBestTrainerCourseList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return mBestTrainerCourseList.size
    }

    interface ItemClickListener {
        fun onRenewClicked(courseData: BestTrainerCourseData)
        fun onSubscribeClicked(courseData: BestTrainerCourseData)
    }
}