package com.kapilguru.trainer.cutomDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ClassDuratinAdapterBinding
import java.util.*

class ClassDurationAdapter : RecyclerView.Adapter<ClassDurationAdapter.Holder>() {

    var durationModel: ArrayList<DurationModel> = arrayListOf()
    lateinit var adapterToDialogListner: AdapterToDialogListner

    class Holder(val binding: ClassDuratinAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        var bindView = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ClassDuratinAdapterBinding.inflate(inflater, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
           holder.bindView.model = durationModel[position]
           holder.bindView.layout.setOnClickListener {
               adapterToDialogListner.getPosition(position)
           }
    }

    override fun getItemCount() = durationModel.size

    fun setData(durationModel: ArrayList<DurationModel>, adapterToDialogListner: AdapterToDialogListner) {
        this.durationModel = durationModel
        this.adapterToDialogListner = adapterToDialogListner
        notifyDataSetChanged()
    }

}

interface  AdapterToDialogListner {
    fun getPosition(position : Int)
}