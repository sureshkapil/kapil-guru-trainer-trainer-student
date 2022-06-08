package com.kapilguru.trainer.allSubscription.packageSubscription.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.databinding.ItemSubscriptionDataBinding

class PackageSubscriptionListAdapter(val listener : PackageItemClickListener) : RecyclerView.Adapter<PackageSubscriptionListAdapter.PackageViewHolder>() {
    val packageSubscriptionList : ArrayList<AllSubscriptionsData> = ArrayList<AllSubscriptionsData>()

    class PackageViewHolder(val binding : ItemSubscriptionDataBinding) : RecyclerView.ViewHolder(binding.root)

    fun setSubscriptionList(list : ArrayList<AllSubscriptionsData>){
        packageSubscriptionList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val packageItemBinding = ItemSubscriptionDataBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PackageViewHolder(packageItemBinding)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        holder.binding.model = packageSubscriptionList.get(position)
        holder.binding.root.setOnClickListener {
            listener.onPackageClicked(packageSubscriptionList[position])
        }
    }

    override fun getItemCount(): Int {
       return packageSubscriptionList.size
    }

    interface PackageItemClickListener{
        fun onPackageClicked(packageSubscription : AllSubscriptionsData)
    }
}