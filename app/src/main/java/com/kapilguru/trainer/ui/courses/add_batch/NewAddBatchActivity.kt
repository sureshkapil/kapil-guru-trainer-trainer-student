package com.kapilguru.trainer.ui.courses.add_batch


import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.cutomDialog.ClassDurationDialog
import com.kapilguru.trainer.cutomDialog.DurationModel
import com.kapilguru.trainer.databinding.NewActivityAddBatchBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.ui.courses.add_batch.viewModel.NewAddBatchViewModel
import com.kapilguru.trainer.ui.courses.add_batch.viewModel.NewAddBatchViewModelFactory
import com.kapilguru.trainer.ui.courses.tax.PriceModel
import com.kapilguru.trainer.ui.courses.tax.TaxCalculationFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_add_batch.*
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*

class NewAddBatchActivity : BaseActivity() {

    lateinit var addBatchViewModel: NewAddBatchViewModel
    lateinit var addBatchBinding: NewActivityAddBatchBinding
    lateinit var dialog: CustomProgressDialog
    private val TAG = "NewAddBatchActivity"
    var valuehour: Int = 0


    var previousSelected = -1
    private val durationList =  ArrayList<DurationModel>().apply {
        add(DurationModel("30 Minutes", 30, false))
        add(DurationModel("1 Hour", 60, false))
        add(DurationModel("1 Hour 30 Minutes", 90, false))
        add(DurationModel("2 Hours", 120, false))
        add(DurationModel("2 Hours 30 Minutes", 150, false))
        add(DurationModel("3 Hours", 180, false))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initiateUIAndViewModel()
        setUpActionBar()
        setAllClickListener()
        observeViewModel()

    }


    private fun initiateUIAndViewModel() {
        addBatchBinding = DataBindingUtil.setContentView(this, R.layout.new_activity_add_batch)
        addBatchViewModel = ViewModelProvider(
            this, NewAddBatchViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application
            )
        ).get(NewAddBatchViewModel::class.java)
        addBatchBinding.batchViewModel = addBatchViewModel
        addBatchBinding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
    }

    private fun setUpActionBar() {
        this.setActionbarBackListener(this, addBatchBinding.actionbar, getString(R.string.add_batch))
    }


    private fun setAllClickListener() {
        addBatchBinding.submit.setOnClickListener {
            addBatchViewModel.readInfo()
        }

        addBatchBinding.aCTVBatchDateValue.setOnClickListener {
            val datePickerDialog = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                myCalendar.set(Calendar.YEAR, year)
//                myCalendar.set(Calendar.MONTH, monthOfYear)
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setUserReadableDate(year,monthOfYear,dayOfMonth)
            }
            showDatePicker(datePickerDialog)
        }

        aCTVBatchStartTimeValue.setOnClickListener {
            showTimePicker()
        }

        addBatchBinding.aCSpinner.setOnClickListener {
            showClassDuration()
        }

        addBatchBinding.aCTVBatchCourseDurationValue.doOnTextChanged { text, start, before, count ->
            text?.let {
                if (it.toString().toInt() > 0) {
//                    addBatchViewModel.calculateEndDate()
                }
                Log.d(TAG, "setAllClickListener: $it")
            }
        }

    }





    private fun observeViewModel() {
        addBatchViewModel.addBatchRequest.observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "observeViewModel: ${it.noOfDays}")
        })
    }


    private fun setUserReadableDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        addBatchViewModel.userReadableStartDate.value = "$dayOfMonth-$monthOfYear-$year"
    }

    private fun setUpPricesFragment(priceModel: PriceModel?) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.prices_frame, TaxCalculationFragment.newInstance(priceModel))
        fm.commit()
    }


    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, id ->
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
            if ((dayOfWeek == Calendar.MONDAY && addBatchViewModel.addBatchRequest.value?.dayMon!= 1) ||
                (dayOfWeek == Calendar.TUESDAY && addBatchViewModel.addBatchRequest.value?.dayTue!=1) ||
                (dayOfWeek == Calendar.WEDNESDAY && addBatchViewModel.addBatchRequest.value?.dayWed!=1) ||
                (dayOfWeek == Calendar.THURSDAY && addBatchViewModel.addBatchRequest.value?.dayThu!=1) ||
                (dayOfWeek == Calendar.FRIDAY && addBatchViewModel.addBatchRequest.value?.dayFri!=1) ||
                (dayOfWeek == Calendar.SATURDAY && addBatchViewModel.addBatchRequest.value?.daySat!=1) ||
                (dayOfWeek == Calendar.SUNDAY && addBatchViewModel.addBatchRequest.value?.daySun!=1)
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

                val dateFromat = addBatchViewModel.getSimpleDateFormatAMPM()
                val timeCalendar = getTimeCalendar(hourOfDay, minutes)
                addBatchViewModel.startTimeCalendar.value = timeCalendar
                val todayStr = dateFromat.format(timeCalendar.time)
                addBatchViewModel.userReadableStartTime.value = todayStr
                addBatchViewModel.calculateEndTime()

            }, mCurrentHour, mCurrentMinute, false
        )

        mTimePicker.show()
    }


    private fun getTimeCalendar(hourOfDay: Int, minutes: Int): Calendar {
        val timeCalendar = Calendar.getInstance()
        timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        timeCalendar.set(Calendar.MINUTE, minutes)
        return timeCalendar
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
                addBatchViewModel.classDuration.value = durationList[previousSelected].value!!.toInt()
                addBatchViewModel.calculateEndTime()
            }
        }
    }

    private fun createData(): ArrayList<DurationModel> {
        return  durationList
    }
}