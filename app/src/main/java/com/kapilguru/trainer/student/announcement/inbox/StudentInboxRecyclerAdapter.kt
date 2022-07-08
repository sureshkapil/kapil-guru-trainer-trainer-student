package com.kapilguru.trainer.student.announcement.inbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.StudentInboxItemBinding
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxItem

class StudentInboxRecyclerAdapter(val inboxListTOAdapters: InboxListTOAdapters) : RecyclerView.Adapter<StudentInboxRecyclerAdapter.Holder>() {
    val TAG = "InboxRecyclerAdapter"

    var studentInboxItems = mutableListOf<StudentInboxItem>()
    var previousTappedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = StudentInboxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = studentInboxItems[position]
        holder.view.position = position
        holder.view.handler = this
    }

    override fun getItemCount(): Int {
        return studentInboxItems.size
    }

    fun setData(data: ArrayList<StudentInboxItem>) {
        studentInboxItems = data
        notifyDataSetChanged()
    }

    class Holder(itemView: StudentInboxItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val view = itemView
    }

    fun dataVisibility(model: StudentInboxItem, tappedPosition: Int) {
        if (tappedPosition == previousTappedPosition) {
            model.shouldShow.value = studentInboxItems[tappedPosition].shouldShow.value != true
//            notifyItemChanged(tappedPosition)
        } else {
            if (previousTappedPosition == -1) {
                previousTappedPosition = tappedPosition
            }
            studentInboxItems[previousTappedPosition].shouldShow.value = false
//            notifyItemChanged(previousTappedPosition)
            previousTappedPosition = tappedPosition
            model.shouldShow.value = true
//            notifyItemChanged(tappedPosition)
        }
        notifyDataSetChanged()
    }

}

interface InboxListTOAdapters {
    fun getInboxList(studentInboxItem: StudentInboxItem)
}