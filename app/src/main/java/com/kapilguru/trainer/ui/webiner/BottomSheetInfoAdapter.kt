package com.kapilguru.trainer.ui.webiner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemBottomSheetDialogFragmnetInfoBinding

class BottomSheetInfoAdapter : RecyclerView.Adapter<BottomSheetInfoAdapter.ItemViewHolder>() {
    private val TAG = "BottomSheetInfoAdapter"
    private var mInfoList = ArrayList<String>()

    fun setData(infoList: ArrayList<String>) {
        mInfoList = infoList
        notifyDataSetChanged()
    }

    class ItemViewHolder(val binding: ItemBottomSheetDialogFragmnetInfoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemBottomSheetDialogFragmnetInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.actvInfo.text = mInfoList[position]
    }

    override fun getItemCount(): Int {
        return mInfoList.size
    }
}