package com.kapilguru.trainer.ui.courses.add_batch.viewModel

import android.app.Application
import android.util.Log
import android.widget.RadioButton
import android.widget.ToggleButton
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.kapilguru.trainer.APP_WEEKDAY
import com.kapilguru.trainer.APP_WEEKEND
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.add_batch.AddBatchRepository
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchRequest
import java.text.SimpleDateFormat
import java.util.*

class NewAddBatchViewModel(
    private val addBatchRepository: AddBatchRepository, application: Application
) : AndroidViewModel(application) {

    var trainerId: Int
    var addBatchRequest: MutableLiveData<AddBatchRequest> = MutableLiveData(AddBatchRequest())
    var userReadableStartDate: MutableLiveData<String> = MutableLiveData("")
    var userReadableStartTime: MutableLiveData<String> = MutableLiveData("")
    var startTimeCalendar: MutableLiveData<Calendar> = MutableLiveData()
    var classDuration: MutableLiveData<Int> = MutableLiveData(-1)
    var userReadableEndTime: MutableLiveData<String> = MutableLiveData("")

    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
    }



    fun calculateEndTime() {
        val startTimeCalendar = startTimeCalendar.value?: return
        val classDuration = classDuration.value?:return

        if (classDuration > 0) {
            val endTimeCalendar = Calendar.getInstance()
            endTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY))
            endTimeCalendar.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE))
            endTimeCalendar.add(Calendar.MINUTE, classDuration)
            val dateFromat = getSimpleDateFormatAMPM()
            val todayStr = dateFromat.format(endTimeCalendar.time)
            userReadableEndTime.value = todayStr
        }

    }


     fun getSimpleDateFormatAMPM(): SimpleDateFormat {
        val dateFromat = SimpleDateFormat("hh:mm  aa")
        return dateFromat
    }

    fun readInfo() {
        Log.d(TAG, "readInfo: ${addBatchRequest.value.toString()}")
    }


    companion object bindingAdapterDays {

        private val TAG = "NewAddBatchActivity"

        @JvmStatic
        @BindingAdapter(value = ["weekDaybatchType"])
        fun RadioButton.setBatchTypeWeekDay(batchType: String?) {
            this.isChecked = batchType == APP_WEEKDAY
            Log.d(TAG, "setBatchTypeWeekDay: i_am_first")
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "weekDaybatchType", event = "android:checkedAttrChanged")
        fun setInverseBatchTypeWeekDay(radioButton: RadioButton): String {
            return if (radioButton.isChecked) APP_WEEKDAY else APP_WEEKEND

        }


        @JvmStatic
        @BindingAdapter(value = ["weekEndbatchType"])
        fun RadioButton.setBatchTypeWeekEnd(batchType: String?) {
            this.isChecked = batchType == APP_WEEKEND
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "weekEndbatchType", event = "android:checkedAttrChanged")
        fun setInverseBatchTypeweekEndbatch(radioButton: RadioButton): String {
            return if (radioButton.isChecked) APP_WEEKEND else APP_WEEKDAY
        }


        @JvmStatic
        @BindingAdapter(value = ["setCustomToggleButtonBatchChecked"], requireAll = true)
        fun ToggleButton.setCutomToggle(isMarked: Int?) {
            isMarked?.let { it ->
                this.isChecked = it == 1
            } ?: run {
                this.isChecked = false
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "setCustomToggleButtonBatchChecked", event = "android:checkedAttrChanged")
        fun setCutomToggleInverse(toggleButton: ToggleButton): Int {
            return if (toggleButton.isChecked && toggleButton.isEnabled) return 1 else 0
        }


        @JvmStatic
        @BindingAdapter("setBatchStartDate")
        fun TextInputEditText.setCustomBatchDate(setBatchStartDate: String?) {
            setBatchStartDate?.let {
                this.setText(setBatchStartDate)
            }
        }

    }
}