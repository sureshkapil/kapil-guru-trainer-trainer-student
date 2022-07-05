package com.kapilguru.trainer.studyMaterial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.FreeContentHorizontalItemBinding
import com.kapilguru.trainer.databinding.FreeContentVerticalItemBinding

// isFrom false is HorizontalHolder,viewType-> Horizontal
// isFrom true from VerticalHolder, viewType-> Vertical

class FreeContentAdapter(var onItemClick: CardItem, var isFrom: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listItems = mutableListOf<FreeContent>()
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class HorizontalHolder(freeContentHorizontalItemBinding: FreeContentHorizontalItemBinding) : RecyclerView.ViewHolder(freeContentHorizontalItemBinding.root) {
        var view = freeContentHorizontalItemBinding

        init {
            view.card.setOnClickListener {
                onItemClick.onCardClick(listItems[absoluteAdapterPosition])
            }
        }
    }


    inner class VerticalHolder(freeContentVerticalItemBinding: FreeContentVerticalItemBinding) : RecyclerView.ViewHolder(freeContentVerticalItemBinding.root) {
        var view = freeContentVerticalItemBinding

        init {
            view.card.setOnClickListener {
                onItemClick.onCardClick(listItems[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = FreeContentHorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HorizontalHolder(view)
        } else {
            val view = FreeContentVerticalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            VerticalHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as HorizontalHolder).view.model = listItems[position]
        } else {
            (holder as VerticalHolder).view.model = listItems[position]
        }
    }

    override fun getItemCount(): Int = listItems.size


    override fun getItemViewType(position: Int): Int = if (isFrom) 1 else 0

    interface CardItem {
        fun onCardClick(freeContent: FreeContent)
    }
}