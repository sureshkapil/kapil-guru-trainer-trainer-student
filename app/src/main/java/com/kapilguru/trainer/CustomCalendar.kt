package com.kapilguru.trainer

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class CustomCalendar(var calendarSelectionListener: CalendarSelectionListener) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var calendarSelectedDate: Calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        dialog.datePicker.minDate = c.timeInMillis
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