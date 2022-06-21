package com.kapilguru.trainer.coupons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.AdapterCouponListItemBinding


class CouponsAdapter: RecyclerView.Adapter<CouponsAdapter.Holder>() {

    var couponsList: ArrayList<AllCouponsResponseListApi> = ArrayList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class Holder(var singleItem: AdapterCouponListItemBinding) : RecyclerView.ViewHolder(singleItem.root){
    var adapterView = singleItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AdapterCouponListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.singleItem.couponInfo = couponsList[position]
    }

    override fun getItemCount(): Int = couponsList.size

}