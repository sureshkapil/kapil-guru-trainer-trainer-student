package com.kapilguru.trainer.ui.courses.add_batch.viewModel

import android.app.Application
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.APP_WEEKDAY
import com.kapilguru.trainer.R
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.add_batch.AddBatchRepository
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchApiResponse
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchRequest
import com.kapilguru.trainer.ui.courses.add_batch.models.EditBatchApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddBatchViewModel(
    private val addBatchRepository: AddBatchRepository,
    application: Application
) : AndroidViewModel(application) {


    var startCalendar: MutableLiveData<Calendar> = MutableLiveData()
    var startTimeCalendar: MutableLiveData<Calendar> = MutableLiveData()
    var endCalendar: MutableLiveData<Calendar> = MutableLiveData()
    var endTimeCalendar: MutableLiveData<Calendar> = MutableLiveData()
    var timeZone: MutableLiveData<String> = MutableLiveData("IST")
    var startDate: MutableLiveData<String> = MutableLiveData()
    var endDate: MutableLiveData<String> = MutableLiveData()
    var startTime: MutableLiveData<String> = MutableLiveData()
    var endTime: MutableLiveData<String> = MutableLiveData()
    var noOfDays: MutableLiveData<String> = MutableLiveData()
    var batchPrice: MutableLiveData<String> = MutableLiveData()
    var datesJson: MutableLiveData<String> = MutableLiveData()
    var discountedPrice: MutableLiveData<String> = MutableLiveData()
    var batchType: MutableLiveData<String> = MutableLiveData(APP_WEEKDAY)
    var classDuration: MutableLiveData<String> = MutableLiveData()
    var courseDuration: MutableLiveData<String> = MutableLiveData()
    var maxNoOfStudents: MutableLiveData<String> = MutableLiveData()
    var dayMon: MutableLiveData<Int> = MutableLiveData(1)
    var dayTue: MutableLiveData<Int> = MutableLiveData(1)
    var dayWed: MutableLiveData<Int> = MutableLiveData(1)
    var dayThu: MutableLiveData<Int> = MutableLiveData(1)
    var dayFri: MutableLiveData<Int> = MutableLiveData(1)
    var daySat: MutableLiveData<Int> = MutableLiveData(0)
    var daySun: MutableLiveData<Int> = MutableLiveData(0)
    var actualPrice: MutableLiveData<String> = MutableLiveData()
    var offerPrice: MutableLiveData<String> = MutableLiveData()
    var courseId: MutableLiveData<Int> = MutableLiveData()
    var batchId: MutableLiveData<Int> = MutableLiveData()
    var emptyFieldMessage: MutableLiveData<Int> = MutableLiveData()
    var isEveryFieldEntered = true


    var isWeekDay: MutableLiveData<Boolean> = MutableLiveData(true)

    var trainerId: Int

    var resultOfAddBatchApi: MutableLiveData<ApiResource<AddBatchApiResponse>> = MutableLiveData()

    var addBatchReq = AddBatchRequest()

    var getEditBatchRequest: MutableLiveData<ApiResource<EditBatchApiRequest>> = MutableLiveData()

    var shouldEdit: MutableLiveData<Boolean> = MutableLiveData(false)


    init {
        val pref = StorePreferences(application)
        trainerId = pref.userId
    }

    private val TAG = "AddBatchViewModel"

    fun onSaveBatchClick(v: View) {
        isEveryFieldEntered = true

        if (checkValidations()) {

            addBatchReq = AddBatchRequest().also {
                it.trainerId = trainerId.toString()
                it.courseId = courseId.value
                it.startDate = convertStartDate()
                it.endDate = convertEndDate()
                it.timeZone = timeZone.value.toString()
                it.noOfDays = noOfDays.value.toString()
                it.batchPrice = batchPrice.value?.toInt()
                it.discountedPrice = discountedPrice.value?.toInt()
                it.batchType = batchType.value.toString()
                it.classDuration = classDuration.value.toString()
                it.maxNoOfStudents = maxNoOfStudents.value.toString()
                it.datesJson = datesJson.value.toString()
                it.dayMon = dayMon.value
                it.dayTue = dayTue.value
                it.dayWed = dayWed.value
                it.dayThu = dayThu.value
                it.dayFri = dayFri.value
                it.daySat = daySat.value
                it.daySun = daySun.value
            }

        }

        if (isEveryFieldEntered) {
            addRequestApiForAddBatch()
        }
    }

    private fun checkValidations(): Boolean {
        when {
            startDate.value.toString().isEmpty() -> {
                emptyFieldMessage.postValue(1)
                isEveryFieldEntered = false
            }
            startTime.value.toString().isEmpty() -> {
                emptyFieldMessage.value = 2
                isEveryFieldEntered = false
            }

            classDuration.value.toString().isEmpty() -> {
                emptyFieldMessage.value = 3
                isEveryFieldEntered = false
            }

//            startCalendar.value!!.before(Calendar.getInstance()) -> {
//                emptyFieldMessage.postValue(9)
//                isEveryFieldEntered = false
//            }

//            Calendar.getInstance().after(startCalendar.value!!) -> {
//                emptyFieldMessage.postValue(9)
//                isEveryFieldEntered = false
//
//            }

            noOfDays.value.toString().isNotEmpty() -> {
                if (noOfDays.value!!.toInt() > 100) {
                    emptyFieldMessage.postValue(6)
                    isEveryFieldEntered = false
                }

            }

            noOfDays.value.toString().isEmpty() -> {
                emptyFieldMessage.value = 4
                isEveryFieldEntered = false
            }
            batchPrice.value.toString().isEmpty() -> {
                emptyFieldMessage.value = 5
                isEveryFieldEntered = false
            }

            maxNoOfStudents.value.toString().isEmpty() -> {
                emptyFieldMessage.value = 7
                isEveryFieldEntered = false
            }

            maxNoOfStudents.value.toString().isEmpty() -> {
                if (noOfDays.value!!.toInt() > 50) {
                    emptyFieldMessage.postValue(8)
                    isEveryFieldEntered = false
                }
            }

        }

        return isEveryFieldEntered
    }


    fun convertStartDate(): String {
        val calendar = Calendar.getInstance()
        var dateString: String = ""
        startCalendar.value?.let { startCalendar ->
            startTimeCalendar.value?.let { startTimeCalendar ->
                calendar.set(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DATE),
                    startTimeCalendar.get(Calendar.HOUR_OF_DAY),
                    startTimeCalendar.get(Calendar.MINUTE),
                    0
                )
                val outputFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                outputFmt.timeZone = TimeZone.getTimeZone("GMT")
//                calendar.timeZone = TimeZone.getTimeZone("UTC")
                val time = calendar.time
                dateString = outputFmt.format(time)
            }
        }
        return dateString
    }


    fun convertEndDate(): String {
        val calendar = Calendar.getInstance()
        var dateString: String = ""
        endCalendar.value?.let { endCalendar ->
            endTimeCalendar.value?.let { endTimeCalendar ->
                calendar.set(
                    endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH),
                    endCalendar.get(Calendar.DATE),
                    endTimeCalendar.get(Calendar.HOUR_OF_DAY),
                    endTimeCalendar.get(Calendar.MINUTE),
                    0
                )
                val outputFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                outputFmt.timeZone = TimeZone.getTimeZone("GMT")
                val time = calendar.time
                dateString = outputFmt.format(time)
            }
        }

        return dateString
    }


    private fun addRequestApiForAddBatch() {
        resultOfAddBatchApi.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultOfAddBatchApi.postValue(
                    ApiResource.success(
                        addBatchRepository.postAddBatch(
                            addBatchReq
                        )
                    )
                )
            } catch (exception: HttpException) {
                resultOfAddBatchApi.postValue(
                    ApiResource.error(
                        data = null, message = exception.message
                            ?: "Error Occurred!"
                    )
                )
            } catch (exception: IOException) {
                resultOfAddBatchApi.postValue(
                    ApiResource.error(
                        data = null, message = exception.message
                            ?: "Error Occurred!"
                    )
                )
            }
        }

    }


    fun editBatchRequestInfo() {
        getEditBatchRequest.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            batchId.value?.let { batchId ->
                try {
                    getEditBatchRequest.postValue(
                        ApiResource.success(
                            addBatchRepository.editBatch(
                                batchId = batchId
                            )
                        )
                    )
                } catch (exception: HttpException) {
                    resultOfAddBatchApi.postValue(
                        ApiResource.error(
                            data = null, message = exception.message
                                ?: "Error Occurred!"
                        )
                    )
                }catch (exception: IOException) {
                    resultOfAddBatchApi.postValue(
                        ApiResource.error(
                            data = null, message = exception.message
                                ?: "Error Occurred!"
                        )
                    )
                }
            }

        }
    }


    companion object bindingAdapterDays {


        @JvmStatic
        @BindingAdapter("custombackground")
        fun AppCompatTextView.setCustomBackGround(boolean: Int) {
            this.background = if (boolean == 1) {
                this.setTextColor(ContextCompat.getColor(context, R.color.white))
                ContextCompat.getDrawable(
                    this.context, R.drawable.selected_days_bg
                )
            } else {
                this.setTextColor(ContextCompat.getColor(context, R.color.black))
                ContextCompat.getDrawable(this.context, R.drawable.unselected_days_bg)
            }
        }

        @JvmStatic
        @BindingAdapter("weekDay")
        fun AppCompatTextView.disableButtonWeekButtons(weekDay: Boolean) {
            weekDay.let {
                this.isClickable = weekDay
            }

        }

    }
}