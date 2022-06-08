package com.kapilguru.trainer.myClassRoomDetails.studymaterial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.ItemActiveCourseBinding
import com.kapilguru.trainer.databinding.ItemPdfDocumentBinding
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentData
import java.util.LinkedHashMap

/*list is stored in Linked Hash Map where the key is course id*/
class PdfDocumentAdapter(val mListener : DocumentClickListerner) : RecyclerView.Adapter<PdfDocumentAdapter.ActiveBatchViewHolder>() {
    private val TAG = "ActiveCoursesAdapter"
    private var documentList = ArrayList<BatchDocumentData>()

    fun setData(documentList: ArrayList<BatchDocumentData>) {
            this.documentList = documentList
            notifyDataSetChanged()
    }

    class ActiveBatchViewHolder(val binding: ItemPdfDocumentBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveBatchViewHolder {
        val binding = ItemPdfDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveBatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActiveBatchViewHolder, position: Int) {
        holder.binding.model = documentList[position]
        holder.binding.btnPdfDownload.setOnClickListener {
            documentList[position].filename?.let { it1 -> mListener.onDocumentClicked(it1 ) }
        }
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    interface DocumentClickListerner{
        fun onDocumentClicked(fileName: String)
    }
}