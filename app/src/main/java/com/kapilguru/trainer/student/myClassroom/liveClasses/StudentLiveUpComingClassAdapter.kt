package com.kapilguru.trainer.student.myClassroom.liveClasses

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.databinding.ItemStudentLiveUpcomingClassBinding
import com.kapilguru.trainer.datesDifferenceInMilli
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentLiveUpComingClassData


class StudentLiveUpComingClassAdapter(val mListener: StudentLiveUpComingClassClickListener) : RecyclerView.Adapter<StudentLiveUpComingClassAdapter.ViewHolder>() {

    private var mLiveUpComingClassList = ArrayList<StudentLiveUpComingClassData>()
    private val TAG = "LiveUpComingClassAdapter"

    fun setData(liveClassList: ArrayList<StudentLiveUpComingClassData>) {
        mLiveUpComingClassList = liveClassList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStudentLiveUpcomingClassBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.actvOverview.setOnClickListener {
                mListener.onOverViewClicked(mLiveUpComingClassList[bindingAdapterPosition])
            }
            binding.studentDetails.setOnClickListener {
                mListener.onOverViewClicked(mLiveUpComingClassList[bindingAdapterPosition])
            }
            binding.activeRecordings.setOnClickListener {
                mListener.onRecordingsClicked(mLiveUpComingClassList[bindingAdapterPosition])
            }

            binding.activeStudyMaterial.setOnClickListener {
                mListener.onStudyMaterialClicked(mLiveUpComingClassList[bindingAdapterPosition])
            }

            binding.activeExam.setOnClickListener {
                mListener.onExamClicked(mLiveUpComingClassList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentLiveUpcomingClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mLiveUpComingClassList[position]
        holder.binding.timer.setOnClickListener {
            mListener.onLiveUpComingClassClicked(mLiveUpComingClassList[position])
        }
        val hoursMinutesRead = datesDifferenceInMilli(mLiveUpComingClassList[position].startTime!!)
        val totalDiff = hoursMinutesRead

        object : CountDownTimer((totalDiff) + 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val hours = seconds / (60 * 60)
                val tempMint = seconds - hours * 60 * 60
                val minutes = tempMint / 60
                seconds = tempMint - minutes * 60
                holder.binding.timer.text = "TIME : " + String.format(
                    "%02d", hours
                ) + ":" + kotlin.String.format("%02d", minutes) + ":" + kotlin.String.format(
                    "%02d", seconds
                )
            }

            override fun onFinish() {
                holder.binding.timer.text = "Go Live"
            }
        }.start()

    }

    override fun getItemCount(): Int {
        return mLiveUpComingClassList.size
    }

    interface StudentLiveUpComingClassClickListener {
        fun onLiveUpComingClassClicked(liveUpComingClass: StudentLiveUpComingClassData)
        fun onOverViewClicked(liveUpComingClass: StudentLiveUpComingClassData)
        fun onRecordingsClicked(liveUpComingClass: StudentLiveUpComingClassData)
        fun onStudyMaterialClicked(liveUpComingClass: StudentLiveUpComingClassData)
        fun onExamClicked(liveUpComingClass: StudentLiveUpComingClassData)
    }
}