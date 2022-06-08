package com.kapilguru.trainer.myClassroom.liveClasses

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemLiveUpcomingClassBinding
import com.kapilguru.trainer.datesDifferenceInMilli
import com.kapilguru.trainer.myClassroom.liveClasses.model.LiveUpComingClassData
import kotlin.collections.ArrayList


class LiveUpComingClassAdapter(val mListener: LiveUpComingClassClickListener) : RecyclerView.Adapter<LiveUpComingClassAdapter.ViewHolder>() {

    private var mLiveUpComingClassList = ArrayList<LiveUpComingClassData>()
    private val TAG = "LiveUpComingClassAdapte"

    fun setData(liveClassList: ArrayList<LiveUpComingClassData>) {
        mLiveUpComingClassList = liveClassList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemLiveUpcomingClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var timer: CountDownTimer? = null
            init {
                binding.actvOverview.setOnClickListener {
                    mListener.onOverViewClicked(mLiveUpComingClassList[bindingAdapterPosition])
                }

                binding.actvStudyMaterial.setOnClickListener {
                    mListener.onStudyMaterialClicked(mLiveUpComingClassList[bindingAdapterPosition])
                }

                binding.actvExam.setOnClickListener {
                    mListener.onExamClicked(mLiveUpComingClassList[bindingAdapterPosition])
                }

                binding.actvCompleteRequest.setOnClickListener {
                    mListener.onCompletionRequestClicked(mLiveUpComingClassList[bindingAdapterPosition])
                }

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLiveUpcomingClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mLiveUpComingClassList[position]
        holder.binding.root.setOnClickListener {
            mListener.onLiveUpComingClassClicked(mLiveUpComingClassList[position])
        }

       if (holder.timer != null)
           holder.timer!!.cancel()

            val hoursMinutesRead = datesDifferenceInMilli(mLiveUpComingClassList[position].startTime!!)
            val totalDiff = hoursMinutesRead
            holder.timer =  object : CountDownTimer ((totalDiff) + 1000 , 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var seconds = (millisUntilFinished / 1000).toInt()
                    val hours = seconds / (60 * 60)
                    val tempMint = seconds - hours * 60 * 60
                    val minutes = tempMint / 60
                    seconds = tempMint - minutes * 60
                    holder.binding.timer.text = "TIME : " + String.format(
                        "%02d",
                        hours
                    ) + ":" + kotlin.String.format("%02d", minutes) + ":" + kotlin.String.format(
                        "%02d",
                        seconds
                    )
                }
                override fun onFinish() {
                    holder.binding.timer.text = "Live"
                }
            }.start()

    }

    override fun getItemCount(): Int {
        return mLiveUpComingClassList.size
    }

    interface LiveUpComingClassClickListener {
        fun onLiveUpComingClassClicked(liveUpComingClass: LiveUpComingClassData)
        fun onOverViewClicked(liveUpComingClass: LiveUpComingClassData)
        fun onStudyMaterialClicked(liveUpComingClass: LiveUpComingClassData)
        fun onExamClicked(liveUpComingClass: LiveUpComingClassData)
        fun onCompletionRequestClicked(liveUpComingClass: LiveUpComingClassData)
    }

}