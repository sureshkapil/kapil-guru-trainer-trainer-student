package com.kapilguru.trainer

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class CustomCalendar(var calendarSelectionListener: CalendarSelectionListener,var  dontSetMinmumDate:Boolean?=true) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var calendarSelectedDate: Calendar = Calendar.getInstance()
     var  ONE_YEAR_IN_MILLI_SECONDS =  31556926000

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        if(dontSetMinmumDate == true) {
            dialog.datePicker.minDate = c.timeInMillis
        }
        return dialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendarSelectedDate.set(Calendar.YEAR, year)
        calendarSelectedDate.set(Calendar.MONTH, month)
        calendarSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendarSelectedDate.set(Calendar.HOUR, 0)
        calendarSelectedDate.set(Calendar.MINUTE, 0)
        calendarSelectionListener.onDateSet(calendarSelectedDate)
    }

}

interface CalendarSelectionListener {
    fun onDateSet(calendarSelectedDate: Calendar)
}