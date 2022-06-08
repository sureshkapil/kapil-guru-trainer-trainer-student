package com.kapilguru.trainer.allSubscription.positionSubscription

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.R
import com.kapilguru.trainer.allSubscription.positionSubscription.model.TrainerCourseData
import com.kapilguru.trainer.databinding.ItemPositionSubscriptionBinding
import com.kapilguru.trainer.showAppToast

class PositionSubscriptionListAdapter(private val context: Context, val listener: PositionItemClickListener) :
    RecyclerView.Adapter<PositionSubscriptionListAdapter.PositionItemViewHolder>() {
    val mPositionSubscriptionList = ArrayList<TrainerCourseData>()

    fun setPositionSubscriptionList(positionSubscriptionList: ArrayList<TrainerCourseData>) {
        mPositionSubscriptionList.addAll(positionSubscriptionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionItemViewHolder {
        val binding = ItemPositionSubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PositionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PositionItemViewHolder, position: Int) {
        holder.bind(mPositionSubscriptionList[position])
    }


    override fun getItemCount(): Int {
        return mPositionSubscriptionList.size
    }

    inner class PositionItemViewHolder(val itemBinding: ItemPositionSubscriptionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.actvRenewOrSubsc.setOnClickListener {
                if (mPositionSubscriptionList[bindingAdapterPosition].isOwned) {
                    listener.onSubscribeOrRenewClicked(mPositionSubscriptionList[bindingAdapterPosition])
                } else {
                    listener.onSubscribeOrRenewClicked(mPositionSubscriptionList[bindingAdapterPosition])


                   /* if (mPositionSubscriptionList[bindingAdapterPosition].positionToSubscribe == -1) {
                        showAppToast(context, "Please select a position to subscribe")
                    } else {
                            listener.onSubscribeOrRenewClicked(mPositionSubscriptionList[bindingAdapterPosition])
                    }*/
                }
            }
           /* itemBinding.actv1.setOnClickListener {
                setCustomDataBG(it, bindingAdapterPosition, mPositionSubscriptionList[bindingAdapterPosition].isPosition_1_Occupied, 1)
            }
            itemBinding.actv2.setOnClickListener {
                setCustomDataBG(it, bindingAdapterPosition, mPositionSubscriptionList[bindingAdapterPosition].isPosition_2_Occupied, 2)
            }
            itemBinding.actv3.setOnClickListener {
                setCustomDataBG(it, bindingAdapterPosition, mPositionSubscriptionList[bindingAdapterPosition].isPosition_3_Occupied, 3)
            }
            itemBinding.actv4.setOnClickListener {
                setCustomDataBG(it, bindingAdapterPosition, mPositionSubscriptionList[bindingAdapterPosition].isPosition_4_Occupied, 4)
            }
            itemBinding.actv5.setOnClickListener {
                setCustomDataBG(it, bindingAdapterPosition, mPositionSubscriptionList[bindingAdapterPosition].isPosition_5_Occupied, 5)
            }*/
        }

      /*  private fun setEmptyBg(actv1: AppCompatTextView, actv2: AppCompatTextView, actv3: AppCompatTextView, actv4: AppCompatTextView) {
            actv1.background = null
            actv2.background = null
            actv3.background = null
            actv4.background = null
        }*/

       /* *
         * @view is the textVIew
         * @selectedPosition is index of selected card
         * @positionOccupied is the boolean value
         * @buttonType is the textViewPosition*/

      /*  private fun setCustomDataBG(v: View, selectedPosition: Int, positionOccupied: Boolean, buttonType: Int) {
            if (mPositionSubscriptionList[selectedPosition].isOwned &&
                mPositionSubscriptionList[selectedPosition].ownedPosition != buttonType
            ) {
                showAppToast(v.context, "You can renewal your current position only")
            } else {
                if (positionOccupied) {
                    showAppToast(v.context, "This position is occupied")
                } else {
                    mPositionSubscriptionList[selectedPosition].apply {
                        positionToSubscribe = buttonType
                    }
                    v.background = ContextCompat.getDrawable(v.context, R.drawable.selected_days_bg)
                    when (v.id) {
                        R.id.actv_1 -> setEmptyBg(itemBinding.actv2, itemBinding.actv3, itemBinding.actv4, itemBinding.actv5)
                        R.id.actv_2 -> setEmptyBg(itemBinding.actv1, itemBinding.actv3, itemBinding.actv4, itemBinding.actv5)
                        R.id.actv_3 -> setEmptyBg(itemBinding.actv2, itemBinding.actv1, itemBinding.actv4, itemBinding.actv5)
                        R.id.actv_4 -> setEmptyBg(itemBinding.actv2, itemBinding.actv3, itemBinding.actv1, itemBinding.actv5)
                        R.id.actv_5 -> setEmptyBg(itemBinding.actv2, itemBinding.actv3, itemBinding.actv4, itemBinding.actv1)
                    }
                }
            }
        }*/

        fun bind(trainerCourseData: TrainerCourseData) {
            itemBinding.model = trainerCourseData
//            setDefaultBG(trainerCourseData)
        }

      /*  private fun setDefaultBG(trainerCourseData: TrainerCourseData) {
            if (trainerCourseData.isOwned) {
                if (trainerCourseData.ownedPosition == 1) { itemBinding.actv1.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.selected_days_bg)
                } else {
                    itemBinding.actv1.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.unselected_days_bg)
                }
                if (trainerCourseData.ownedPosition == 2) {
                    itemBinding.actv2.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.selected_days_bg)
                } else {
                    itemBinding.actv2.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.unselected_days_bg)
                }
                if (trainerCourseData.ownedPosition == 3) {
                    itemBinding.actv3.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.selected_days_bg)
                } else {
                    itemBinding.actv3.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.unselected_days_bg)
                }
                if (trainerCourseData.ownedPosition == 4) {
                    itemBinding.actv4.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.selected_days_bg)
                } else {
                    itemBinding.actv4.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.unselected_days_bg)
                }
                if (trainerCourseData.ownedPosition == 5) {
                    itemBinding.actv5.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.selected_days_bg)
                } else {
                    itemBinding.actv5.background = ContextCompat.getDrawableitemBinding.actv1.context, R.drawable.unselected_days_bg)
                }

            } else {
                when (trainerCourseData.positionToSubscribe) {
                    1 -> {
                        itemBinding.actv1.background = ContextCompat.getDrawable(itemBinding.actv1.context, R.drawable.selected_days_bg)
                        setEmptyBg(itemBinding.actv3, itemBinding.actv2, itemBinding.actv4, itemBinding.actv5)
                    }
                    2 -> {
                        itemBinding.actv2.background = ContextCompat.getDrawable(itemBinding.actv2.context, R.drawable.selected_days_bg)
                        setEmptyBg(itemBinding.actv1, itemBinding.actv3, itemBinding.actv4, itemBinding.actv5)
                    }
                    3 -> {
                        itemBinding.actv3.background = ContextCompat.getDrawable(itemBinding.actv3.context, R.drawable.selected_days_bg)
                        setEmptyBg(itemBinding.actv1, itemBinding.actv2, itemBinding.actv4, itemBinding.actv5)
                    }
                    4 -> {
                        itemBinding.actv4.background = ContextCompat.getDrawable(itemBinding.actv4.context, R.drawable.selected_days_bg)
                        setEmptyBg(itemBinding.actv1, itemBinding.actv2, itemBinding.actv3, itemBinding.actv5)
                    }
                    5 -> {
                        itemBinding.actv5.background = ContextCompat.getDrawable(itemBinding.actv5.context, R.drawable.selected_days_bg)
                        setEmptyBg(itemBinding.actv1, itemBinding.actv2, itemBinding.actv4, itemBinding.actv3)
                    }
                    else -> {
                        itemBinding.actv1.background = null
                        itemBinding.actv2.background = null
                        itemBinding.actv3.background = null
                        itemBinding.actv4.background = null
                        itemBinding.actv5.background = null
                    }
                }
            }
        }*/

    }

    interface PositionItemClickListener {
        fun onSubscribeOrRenewClicked(trainerCourseData: TrainerCourseData)
    }

}