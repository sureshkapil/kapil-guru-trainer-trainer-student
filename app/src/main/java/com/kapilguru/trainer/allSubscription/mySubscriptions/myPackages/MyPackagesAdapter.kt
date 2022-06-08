package com.kapilguru.trainer.allSubscription.mySubscriptions.myPackages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MyPackageData
import com.kapilguru.trainer.databinding.ItemMyPackageSubscriptionBinding

class MyPackagesAdapter(val listener : RenewClickListener) : RecyclerView.Adapter<MyPackagesAdapter.MyPackageViewHolder>() {
    private val TAG = "MyPackagesAdapter"
    private var mMyPackageList = ArrayList<MyPackageData>()
    private val mListener = listener

    fun setData(myPackageList: ArrayList<MyPackageData>?) {
        myPackageList?.let {
            mMyPackageList = myPackageList
            notifyDataSetChanged()
        }
    }

    inner class MyPackageViewHolder(val itemBinding: ItemMyPackageSubscriptionBinding) : RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemBinding.actvRenewal.setOnClickListener {
                mListener.onRenewClicked(mMyPackageList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPackageViewHolder {
        val binding = ItemMyPackageSubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPackageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPackageViewHolder, position: Int) {
        holder.itemBinding.myPackageData = mMyPackageList[position]
    }

    override fun getItemCount(): Int {
        return mMyPackageList.size
    }

    interface RenewClickListener{
        fun onRenewClicked(myPackageData: MyPackageData)
    }
}