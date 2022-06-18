package com.kapilguru.trainer.testimonials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.TrainerTestimonialRecyclerItemItemBinding

class TestimonialReyclerAdapter : RecyclerView.Adapter<TestimonialReyclerAdapter.Holder>() {

    var fetchTestimonialsResponseApi = mutableListOf<FetchTestimonialsResponseApi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = TrainerTestimonialRecyclerItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return fetchTestimonialsResponseApi.size
    }

    fun setData(data: ArrayList<FetchTestimonialsResponseApi>) {
        fetchTestimonialsResponseApi = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = fetchTestimonialsResponseApi[position]
    }

    inner class Holder(var viewItem: TrainerTestimonialRecyclerItemItemBinding) : RecyclerView.ViewHolder(viewItem.root) {
        val view = viewItem
    }

}