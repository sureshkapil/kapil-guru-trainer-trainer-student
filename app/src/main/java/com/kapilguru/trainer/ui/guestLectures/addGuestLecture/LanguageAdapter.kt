package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.LanguageItemBinding
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData

class LanguageAdapter(val languageList : ArrayList<LanguageData>) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {
    private val TAG = "LanguageAdapter"
    var mLanguageList = languageList

    class ViewHolder(val binding : LanguageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var view = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LanguageItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.languageData = mLanguageList[position]
    }

    override fun getItemCount(): Int {
        return mLanguageList.size
    }

    @JvmName("setLanguageList1")
    fun setLanguageList(languageList: ArrayList<LanguageData>) {
        mLanguageList = languageList
        notifyDataSetChanged()
    }
}