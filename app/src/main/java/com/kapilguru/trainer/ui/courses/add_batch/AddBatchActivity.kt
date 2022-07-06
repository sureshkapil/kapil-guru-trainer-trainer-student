package com.kapilguru.trainer.ui.courses.add_batch


import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.cutomDialog.ClassDurationDialog
import com.kapilguru.trainer.cutomDialog.DurationModel
import com.kapilguru.trainer.databinding.ActivityAddBatchBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.add_batch.viewModel.AddBatchViewModel
import com.kapilguru.trainer.ui.courses.add_batch.viewModel.AddBatchViewModelFactory
import com.kapilguru.trainer.ui.courses.batchesList.BatchListDetailsActivity
import com.kapilguru.trainer.ui.courses.tax.PriceModel
import com.kapilguru.trainer.ui.courses.tax.TaxCalculationFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_add_batch.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddBatchActivity : BaseActivity(), View.OnClickListener {

    private var shouldEdit: Boolean= false
    lateinit var addBatchViewModel: AddBatchViewModel
    lateinit var addBatchBinding: ActivityAddBatchBinding
    lateinit var dialog : CustomProgressDialog
    var myCalendar: Calendar = Calendar.getInstance()
    var valuehour: Int = 0
    private val TAG = "AddBatchActivity"
    var startTimeCalendar: Calendar = Calendar.getInstance()
    var endTimeCalendar: Calendar = Calendar.getInstance()
    var courseId: Int = -1
    var batchId: Int = -1
    var startCalendar: Calendar = Calendar.getInstance()
    var previousSelected = -1
    private val durationList =  ArrayList<DurationModel>().apply {
        add(DurationModel("30 Minutes", 30, false))
        add(DurationModel("1 Hour", 60, false))
        add(DurationModel("1 Hour 30 Minutes", 90, false))
        add(DurationModel("2 Hours", 120, false))
        add(DurationModel("2 Hours 30 Minutes", 150, false))
        add(DurationModel("3 Hours", 180, false))
    }

     var priceModel:PriceModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addBatchBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_batch)
        addBatchViewModel = ViewModelProvider(
            this,
            AddBatchViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),
                application
            )
        ).get(AddBatchViewModel::class.java)

        addBatchBinding.batchViewModel = addBatchViewModel
        addBatchBinding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        this.setActionbarBackListener(this,addBatchBinding.actionbar,getString(R.string.add_batch))

        addBatchBinding.aCTVMonday.setOnClickListener(this)
        addBatchBinding.aCTVTuesday.setOnClickListener(this)
        addBatchBinding.aCTVWednesday.setOnClickListener(this)
        addBatchBinding.aCTVThursday.setOnClickListener(this)
        addBatchBinding.aCTVFriday.setOnClickListener(this)
        addBatchBinding.aCTVSaturday.setOnClickListener(this)
        addBatchBinding.aCTVSunday.setOnClickListener(this)

        shouldEnableOtherFields()


        setBatchTypeRadioGroupCheckListener()

        setOfferPriceTextChangeListener()

        setClassDurationClickListener()


        courseId = intent.getIntExtra("courseId", -1)
        addBatchViewModel.courseId.value = this.courseId

        batchId = intent.getIntExtra(EDIT_BATCH_ID_PARAM, -1)
        if(batchId >= 0) addBatchViewModel.batchId.value = this.batchId

        shouldEdit = intent.getBooleanExtra(IS_FROM_EDIT_PARAM, false)
        if(shouldEdit) {
            addBatchViewModel.shouldEdit.value = shouldEdit
            addBatchViewModel.editBatchRequestInfo()
        }


        observeViewModelData()

        val datePickerDialog = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        aCTVBatchDateValue.setOnClickListener {
            showDatePicker(datePickerDialog)
        }

        aCTVBatchStartTimeValue.setOnClickListener {
            showTimePicker()
        }

        aCTVBatchCourseDurationValue.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if(aCTVBatchCourseDurationValue.text.toString().toInt()>=1) {
                        addBatchViewModel.noOfDays.value = aCTVBatchCourseDurationValue.text.toString()
                        setTotalCourseDuration()
                    }
                }

            }

//        aCTVBatchCourseDurationValue.doOnTextChanged(text) =
//            View.OnFocusChangeListener { v, hasFocus ->
//                if (!hasFocus) {
//                    addBatchViewModel.noOfDays.value = aCTVBatchCourseDurationValue.text.toString()
//                    setTotalCourseDuration()
//                }
//
//            }


        aCTVBatchCourseDurationValue.doOnTextChanged { text, start, before, count ->

            if(!text.isNullOrEmpty() && text.toString().toInt()>100) {
                aCTVBatchCourseDurationValue.setText("100")
                addBatchViewModel.emptyFieldMessage.postValue(6)
            } else if (!text.isNullOrEmpty()) {
                if(aCTVBatchCourseDurationValue.text.toString().toInt()>=1) {
                    addBatchViewModel.noOfDays.value = aCTVBatchCourseDurationValue.text.toString()
                    setTotalCourseDuration()
                }
            }

        }

        addBatchBinding.aCTVBatchCourseNUmberOfStudentsValue.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty() && text.toString().toInt()>50) {
                aCTVBatchCourseNUmberOfStudentsValue.setText("50")
                addBatchViewModel.emptyFieldMessage.postValue(8)
            } else if (!text.isNullOrEmpty()) {
//                addBatchViewModel.maxNoOfStudents.value = aCTVBatchCourseNUmberOfStudentsValue.text.toString()
            }

        }

        addBatchBinding.saveBatch.setOnClickListener {
            val priceModel = getPricesInfo()
            addBatchViewModel.batchPrice.value =priceModel?.fee
            addBatchViewModel.discountedAmount.value =priceModel?.discountAmount
            addBatchViewModel.discountedPrice.value =priceModel?.actualFee
            addBatchViewModel.isTax.value =priceModel?.isTaxChargesAdded
            addBatchViewModel.internetCharges.value =priceModel?.internetCharges
            if(shouldEdit) {
                addBatchViewModel.onSaveBatchClick()
            } else {
                addBatchViewModel.onSaveBatchClick()
            }
        }
        if(!shouldEdit) setUpPricesFragment(priceModel)

    }

    private fun setUpPricesFragment(priceModel: PriceModel?) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.prices_frame, TaxCalculationFragment.newInstance(priceModel))
        fm.commit()
    }


    fun getPricesInfo() : PriceModel? {
        val listOfAllFragments = supportFragmentManager.fragments
        val fm = listOfAllFragments.first() as TaxCalculationFragment
        return  fm.getPriceData()
    }

    private fun setClassDurationClickListener() {
        addBatchBinding.aCSpinner.setOnClickListener {
            showClassDuration()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.aCTVMonday -> {
                addBatchViewModel.dayMon.value = if (addBatchViewModel.dayMon.value!! == 1) 0 else 1
            }
            R.id.aCTVTuesday -> {
                addBatchViewModel.dayTue.value = if (addBatchViewModel.dayTue.value!! == 1) 0 else 1
            }
            R.id.aCTVWednesday -> {
                addBatchViewModel.dayWed.value = if (addBatchViewModel.dayWed.value!! == 1) 0 else 1
            }
            R.id.aCTVThursday -> {
                addBatchViewModel.dayThu.value = if (addBatchViewModel.dayThu.value!! == 1) 0 else 1
            }
            R.id.aCTVFriday -> {
                addBatchViewModel.dayFri.value = if (addBatchViewModel.dayFri.value!! == 1) 0 else 1
            }
            R.id.aCTVSaturday -> {
                addBatchViewModel.daySat.value = if (addBatchViewModel.daySat.value!! == 1) 0 else 1
            }
            R.id.aCTVSunday -> {
                addBatchViewModel.daySun.value = if (addBatchViewModel.daySun.value!! == 1) 0 else 1
            }
        }

        if(addBatchViewModel.startDate.value.isNullOrEmpty()) {
            resetAllValues()
        }

    }

    private fun resetAllValues() {
//        myCalendar = Calendar.getInstance()
//        addBatchViewModel.startDate.value = ""
//        addBatchViewModel.startTime.value = ""
    }


    private fun shouldEnableOtherFields() {
        if(!addBatchViewModel.startDate.value.isNullOrBlank()) {
            aCTVBatchStartTimeValue.isEnabled = true
            aCSpinner.isEnabled = true
            aCTVBatchEndTimeValue.isEnabled = true
            aCTVBatchCourseDurationValue.isEnabled = true
        } else  {
            aCTVBatchStartTimeValue.isEnabled = false
            aCSpinner.isEnabled = false
            aCTVBatchEndTimeValue.isEnabled = false
            aCTVBatchCourseDurationValue.isEnabled = false
        }
    }

    private fun setBatchTypeRadioGroupCheckListener() {
        addBatchBinding.rGroupBatchType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rButtonWeekday) {
                addBatchViewModel.batchType.value = APP_WEEKDAY
                addBatchViewModel.dayMon.value = 1
                addBatchViewModel.dayTue.value = 1
                addBatchViewModel.dayWed.value = 1
                addBatchViewModel.dayThu.value = 1
                addBatchViewModel.dayFri.value = 1
                addBatchViewModel.daySat.value = 0
                addBatchViewModel.daySun.value = 0
                addBatchViewModel.isWeekDay.value = true

            } else {
                addBatchViewModel.batchType.value = APP_WEEKEND
                addBatchViewModel.dayMon.value = 0
                addBatchViewModel.dayTue.value = 0
                addBatchViewModel.dayWed.value = 0
                addBatchViewModel.dayThu.value = 0
                addBatchViewModel.dayFri.value = 0
                addBatchViewModel.daySat.value = 1
                addBatchViewModel.daySun.value = 1
                addBatchViewModel.isWeekDay.value = false
            }
        }
    }


    private fun setOfferPriceTextChangeListener() {
        addBatchBinding.aCTVBatchOfferPriceValue.doOnTextChanged { text, start, before, count ->
            text?.let { it->
                if (it.isNotEmpty()) {
                    if (it.toString().toInt() >
                        addBatchBinding.aCTVBatchActualFeeValue.text.toString().toInt()
                    ) {
                        addBatchBinding.aCTVBatchOfferPriceValue.setText(addBatchBinding.aCTVBatchActualFeeValue.text.toString())
                        Toast.makeText(
                            this,
                            "Offer Price cannot be grater than Course Price",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
        }
    }



    private fun setTotalCourseDuration() {

        addBatchViewModel.noOfDays.value?.let {

            val loopCalander = Calendar.getInstance()
            loopCalander.set(
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DATE)
            )
            if (it.isNotEmpty()) {
                val rotateCount = it.toInt() * it.toInt()
                val checkObject = JSONArray()
                var counter = 0
                for (i in 0..rotateCount) {
                    counter += 1
                    if (checkObject.length() == it.toInt()) {
                        break
                    }
                    var checkJSONObject = JSONObject()
                    if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && addBatchViewModel.dayMon.value == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && addBatchViewModel.dayTue.value == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY && addBatchViewModel.dayWed.value == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY && addBatchViewModel.dayThu.value == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && addBatchViewModel.dayFri.value == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && addBatchViewModel.daySat.value == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && addBatchViewModel.daySun.value == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    }

                    loopCalander.add(Calendar.DATE, 1)

                }

                addBatchViewModel.courseDuration.value = (counter - 1).toString()

                val info = checkObject.get(checkObject.length() - 1) as JSONObject
                addBatchViewModel.datesJson.value = checkObject.toBase64()
                val endate = info.getLong("end_time")
                Log.d(TAG, "setTotalCourseDuration: $endate")
                val formatter: DateFormat = SimpleDateFormat("MMM d, y, h:mm:ss a")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = endate
                val d = formatter.format(calendar.time)
                addBatchViewModel.endCalendar.value = calendar
                addBatchViewModel.endDate.value = d.toString()
            }
        }

    }

    private fun createJsonObjectOfAllSelectedDates(loopCalendar: Calendar): JSONObject {
        val jsonObject = JSONObject()
        loopCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY))
        loopCalendar.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE))
        loopCalendar.set(Calendar.AM_PM, isItAMPM(loopCalendar.get(Calendar.HOUR_OF_DAY)))
        jsonObject.put("start_time", loopCalendar.timeInMillis)
        loopCalendar.set(Calendar.HOUR_OF_DAY, endTimeCalendar.get(Calendar.HOUR_OF_DAY))
        loopCalendar.set(Calendar.MINUTE, endTimeCalendar.get(Calendar.MINUTE))
        loopCalendar.set(Calendar.AM_PM, isItAMPM(loopCalendar.get(Calendar.HOUR_OF_DAY)))
        jsonObject.put("end_time", loopCalendar.timeInMillis)
        return jsonObject
    }

    private fun showDatePicker(date: com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener) {

        val myCalendar: Calendar = Calendar.getInstance()

        val datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.minDate = myCalendar

        val todaysCalendar: Calendar = Calendar.getInstance()
        todaysCalendar.set(
            myCalendar.get(Calendar.YEAR) + 1,
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DATE)
        )
        datePickerDialog.maxDate = todaysCalendar
        var loopdate: Calendar = myCalendar
        val maxYear = myCalendar.get(Calendar.YEAR) + 2
        // this loop adds disable dates to the current calendar
        while (myCalendar.get(Calendar.YEAR) < maxYear) {
            val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]
            if ((dayOfWeek == Calendar.MONDAY && addBatchViewModel.dayMon.value != 1) ||
                (dayOfWeek == Calendar.TUESDAY && addBatchViewModel.dayTue.value!=1) ||
                (dayOfWeek == Calendar.WEDNESDAY && addBatchViewModel.dayWed.value!=1) ||
                (dayOfWeek == Calendar.THURSDAY && addBatchViewModel.dayThu.value!=1) ||
                (dayOfWeek == Calendar.FRIDAY && addBatchViewModel.dayFri.value!=1) ||
                (dayOfWeek == Calendar.SATURDAY && addBatchViewModel.daySat.value!=1) ||
                (dayOfWeek == Calendar.SUNDAY && addBatchViewModel.daySun.value!=1)
            ) {
                val disabledDays = arrayOfNulls<Calendar>(1)
                disabledDays[0] = loopdate
                datePickerDialog.disabledDays = disabledDays
            }
            myCalendar.add(Calendar.DATE, 1)
            loopdate = myCalendar

        }

        datePickerDialog.show(supportFragmentManager, "Datepickerdialog")

    }

    private fun showTimePicker() {

        val mcurrentTime = Calendar.getInstance()
        val mCurrentHour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val mCurrentMinute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        var amPm: Int

        mTimePicker = TimePickerDialog(
            this,
            { timePicker, hourOfDay, minutes ->
                amPm = isItAMPM(hourOfDay)

                startTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                startTimeCalendar.set(Calendar.MINUTE, minutes)

                val dateFromat = SimpleDateFormat("hh:mm  aa")
                val todayStr = dateFromat.format(startTimeCalendar.time)

                addBatchViewModel.startTime.value = todayStr

                if (previousSelected != -1) {
                    setEndTime(durationList[previousSelected].value!!.toInt())
                }

            }, mCurrentHour, mCurrentMinute, false
        )

        mTimePicker.show()
    }

    private fun createData(): ArrayList<DurationModel> {
        return  durationList
    }


    private fun showClassDuration() {
        ClassDurationDialog.newInstance(createData()).show(supportFragmentManager, "1")
        supportFragmentManager.setFragmentResultListener("abc", this) { key, bundle ->
            val result = bundle.getInt("resultKey")

            if (previousSelected >= 0) {
                durationList.apply {
                    this[previousSelected].isSelected = false
                }
            }
            durationList.apply {
                previousSelected = result
                this[result].isSelected = true
                addBatchBinding.aCSpinner.setText(durationList[result].key)
                setEndTime(durationList[result].value!!.toInt())
            }
        }
    }

    private fun isItAMPM(hourOfDay: Int): Int {
        when {
            hourOfDay > 12 -> {
                valuehour = hourOfDay
                valuehour -= 12
                return 1
            }
            hourOfDay == 0 -> {
                valuehour = hourOfDay
                valuehour += 12
                return 0
            }
            hourOfDay == 12 -> {
                valuehour = hourOfDay
                return 1
            }
            else -> {
                valuehour = hourOfDay
                return 0

            }
        }
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        addBatchViewModel.startDate.value = sdf.format(myCalendar.time)
        addBatchViewModel.startCalendar.value = myCalendar
        shouldEnableOtherFields()
        // check for other fields and set End Date
        changeEndDate()

    }


    private fun observeViewModelData() {
        addBatchViewModel.resultOfAddBatchApi.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    navigateToBatchListActivity()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })

        addBatchViewModel.emptyFieldMessage.observe(this, Observer {
            when (it) {
                1 -> {
                    showToastMessage("please fill the Course add Date")
                }
                2 -> {
                    showToastMessage("please fill the Start Time")
                }
                3 -> {
                    showToastMessage("please fill the Class Duration")
                }
                4 -> {
                    showToastMessage("please fill the Total Number of class")
                }
                5 -> {
                    showToastMessage("please fill the batch price")
                }
                6 -> {
                    showToastMessage("No of class can't Less than 1")
                }
                7 -> {
                    showToastMessage("Please fill total Number of Days")
                }
                8 -> {
                    showToastMessage("total No of students can't be greater than 50")
                }
                9 -> {
                    showToastMessage("Start date should not be less than current date")
                }

            }
        })


        addBatchViewModel.getEditBatchRequest.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it?.data?.editBatchList?.let { editBatch ->
                        Log.d(TAG, "observeViewModelData: ${editBatch[0]}")
                        addBatchViewModel.isKgMeeting.value = editBatch[0].isKgMeeting == 1
                        editBatch[0].startDate?.let { dateAndTime ->
                            setStartDateAndStartTime(dateAndTime)
                        }
//                        addBatchViewModel.maxNoOfStudents.value = editBatch[0].maxNoOfStudents
//                        addBatchViewModel.batchPrice.value = editBatch[0].batchPrice.toString()
//                        addBatchViewModel.offerPrice.value = editBatch[0].discountedPrice.toString()
                        addBatchViewModel.dayMon.value = editBatch[0].dayMon
                        addBatchViewModel.dayTue.value = editBatch[0].dayTue
                        addBatchViewModel.dayWed.value = editBatch[0].dayWed
                        addBatchViewModel.dayThu.value = editBatch[0].dayThu
                        addBatchViewModel.dayFri.value = editBatch[0].dayFri
                        addBatchViewModel.daySat.value = editBatch[0].daySat
                        addBatchViewModel.daySun.value = editBatch[0].daySun
                        addBatchViewModel.noOfDays.value = editBatch[0].noOfDays
                        editBatch[0].classDuration?.let { duration ->
                            val selectedPosition = durationList.withIndex()
                                .filter { map -> map.value.value == duration.toInt() }
                                .map { it.index }
                            Log.d(TAG, "observeViewModelData: $duration")
                            previousSelected = selectedPosition[0]
                            addBatchBinding.aCSpinner.setText(duration.toString())
                            setEndTime(duration.toInt())
                        }
                        editBatch[0].noOfDays?.let { it ->
                            setTotalCourseDuration()
                        }
//                        it[0].classDuration?.toInt()?.let { it1 ->
//                            getDurationOfWebinar(it1)
//                        }

                        val price = PriceModel().apply {
                            fee = editBatch[0].batchPrice.toString()
                            discountAmount = editBatch[0].discountAmount.toString()
                            actualFee = editBatch[0].discountedPrice.toString()
                            isTaxChargesAdded = editBatch[0].isTax == 1
                            internetCharges = editBatch[0].internetCharges
                        }
                            setUpPricesFragment(price)


                        shouldEnableOtherFields()
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        Log.v("showSomething", "readData_1 ${it.message.toString()}")
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        Log.v("showSomething", "readData_2")
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }

        })



        addBatchViewModel.editBatchResponse.observe(this,{
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    navigateToBatchListActivity()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }

        })
    }

    private fun setStartDateAndStartTime(dateAndTime: String) {
         dateAndTime.toDecodeUTCToDateAndTime()?.let {
          val splitArray = it.split(",")
             addBatchViewModel.startDate.value = splitArray[0]
             addBatchViewModel.startTime.value = splitArray[1]
             val splitDateArray = splitArray[0].split("/")
             val calendar = Calendar.getInstance()
             calendar.set(Calendar.YEAR,splitDateArray[0].toInt())
             calendar.set(Calendar.MONTH,splitDateArray[1].removePrefix("0").toInt())
             calendar.set(Calendar.DAY_OF_MONTH,splitDateArray[2].toInt())
             addBatchViewModel.startCalendar.value = calendar
             shouldEnableOtherFields()
         }
    }

    private fun navigateToBatchListActivity() {
        val intent = Intent(this, BatchListDetailsActivity::class.java)
        intent.putExtra("courseId", courseId)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        setCancelable(true)
                    })

                setMessage(message)

            }

            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()
    }

    private fun setEndTime(addMinutes: Int) {
        val endTimeCalendar = Calendar.getInstance()
        endTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY))
        endTimeCalendar.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE))
        endTimeCalendar.add(Calendar.MINUTE, addMinutes)

        val dateFromat = SimpleDateFormat("hh:mm  aa")
        val todayStr = dateFromat.format(endTimeCalendar.time)
        addBatchViewModel.endTime.value = todayStr

        this.endTimeCalendar = endTimeCalendar
        addBatchViewModel.startTimeCalendar.value = startTimeCalendar
        addBatchViewModel.endTimeCalendar.value = endTimeCalendar
        addBatchViewModel.classDuration.value = addMinutes.toString()

        // check for other fields and set End Date
        changeEndDate()
    }


    private fun getDurationOfWebinar(duration: Int){
//        addBatchBinding.aCSpinner.setSelection(getIndexForDuration(duration))
    }

    private fun changeEndDate() {
        when {
            (!addBatchViewModel.startDate.value.isNullOrEmpty() && !addBatchViewModel.startTime.value.isNullOrEmpty() && !addBatchViewModel.classDuration.value.isNullOrEmpty() && !addBatchViewModel.noOfDays.value.isNullOrEmpty()) -> {
                setTotalCourseDuration()
            }
        }
    }

}