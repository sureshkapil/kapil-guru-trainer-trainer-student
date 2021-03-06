package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentAddGuestLectureDeatilsBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.toBase64
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.viewModel.AddGuestLectureViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddGuestLectureDeatilsFragment : Fragment() {
    private val TAG = "AddGuestLecDetailsFrag"
    lateinit var binding: FragmentAddGuestLectureDeatilsBinding
    val viewModel: AddGuestLectureViewModel by activityViewModels()
    lateinit var addGuestLectureCourseAdapter: AddGuestLectureCourseAdapter
    val myCalendar: Calendar = Calendar.getInstance()
    var startTimeCalendar: Calendar = Calendar.getInstance()
    var endTimeCalendar: Calendar = Calendar.getInstance()
    var valuehour: Int = 0
    lateinit var dialog: CustomProgressDialog

    var isBoldSelected: Boolean = false
    var isItalicSelected: Boolean = false
    var isUnderLineSelected: Boolean = false
    var isBulletsSelected: Boolean = false
    var isNumberSelected: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_guest_lecture_deatils, container, false)
        binding.viewModel = viewModel
        binding.clickHandlers = this
        dialog = CustomProgressDialog(requireContext())
        binding.lifecycleOwner = this.requireActivity()
        binding.tvSelectedLanguages.text = getString(R.string.select_languages)
        checkAndSetDefaultTrainerName()
        setCourseAdapter()
        checkAndSetAbout()
        observeViewModelData()
        setClickListeners()
        return binding.root
    }

    private fun checkAndSetDefaultTrainerName() {
        if (!viewModel.getIsLaunchedForEdit()) {
            val trainerName = StorePreferences(this.requireActivity()).userName
            binding.etTrainerName.setText(trainerName)
        }
    }

    private fun setCourseAdapter() {
        addGuestLectureCourseAdapter = AddGuestLectureCourseAdapter(requireActivity())
        binding.spinnerCourse.adapter = addGuestLectureCourseAdapter
    }

    private fun checkAndSetAbout() {
        if (viewModel.addGuestLectureRequest.about != null)
            binding.richEditor.html = viewModel.addGuestLectureRequest.about
    }

    private fun observeViewModelData() {
        observeCourseLanguageData()
        observeCoursesData()
    }

    private fun observeCourseLanguageData() {
        viewModel.courseLanguages.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    viewModel.selectedLanguages = it.data?.languageData!!
                    checkAndSetLanguage(it.data.languageData)
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetLanguage(languagesList: ArrayList<LanguageData>) {
        if (viewModel.getIsLaunchedForEdit()) {
            val selectedLanguages = viewModel.addGuestLectureRequest.languages
            val listOfSelectedLanguageIds = getSelectedLanguagesIdList(selectedLanguages!!)
            for (id in listOfSelectedLanguageIds) {
                for (languageData in languagesList) {
                    if (languageData.id == id) {
                        languageData.isSelected = true
                        break
                    }
                }
            }
            viewModel.updateLanguageSelection(languagesList)
        }
    }

    private fun getSelectedLanguagesIdList(selectedLanguages: String): List<Int> {
        val listOfSelectedLanguagesId = ArrayList<Int>()
        for (i in 0..selectedLanguages.length) {
            if (!i.equals(",")) {
                listOfSelectedLanguagesId.add(i)
            }
        }
        Log.d(TAG, "listOfSelectedLanguagesId : " + listOfSelectedLanguagesId)
        return listOfSelectedLanguagesId
    }

    private fun observeCoursesData() {
        viewModel.courses.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    addGuestLectureCourseAdapter.setCategoryList(it.data?.courseResponse!!)
                    checkAndSetCourse(it.data.courseResponse)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        }
    }

    private fun checkAndSetCourse(courseList: List<CourseResponse>) {
        if (viewModel.getIsLaunchedForEdit()) {
            val courseId = viewModel.addGuestLectureRequest.courseId
            for (course in courseList) {
                if (course.courseId == courseId) {
                    val index = courseList.indexOf(course)
                    binding.spinnerCourse.setSelection(courseList.indexOf(course))
                    return
                }
            }
        }
    }

    private fun setClickListeners() {
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        binding.etLectureDate.setOnClickListener {
            showDatePicker(date)
        }

        binding.etStartTime.setOnClickListener {
            showTimePicker()
        }
        binding.tvSelectedLanguages.setOnClickListener {
            showDialogToSelectLanguages()
        }

        binding.etNoOfAttendees.doOnTextChanged { text, start, before, count ->
            if(text.toString().trim().isNotEmpty()){
                validateAndCalculateEndDate()
            }
        }
        setSpinnerCourseItemSelectListener()
        setSpinnerDurationItemSelectListener()
    }

    private fun setSpinnerCourseItemSelectListener() {
        binding.spinnerCourse.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var selectedCourseId =
                        viewModel.courses.value?.data?.courseResponse?.get(position)?.courseId
                    viewModel.addGuestLectureRequest.courseId = selectedCourseId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun setSpinnerDurationItemSelectListener() {
        binding.spinnerDuration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {viewModel.addGuestLectureRequest.duration = 0
                            setEndTime(0)}
                        1 -> {viewModel.addGuestLectureRequest.duration = 30
                            setEndTime(30)}
                        2 -> {viewModel.addGuestLectureRequest.duration = 60
                            setEndTime(60)}
                        3 -> {viewModel.addGuestLectureRequest.duration = 90
                            setEndTime(90)}
                        4 -> {viewModel.addGuestLectureRequest.duration = 120
                            setEndTime(120)}
                        5 -> {viewModel.addGuestLectureRequest.duration = 150
                            setEndTime(150)}
                        6 -> {viewModel.addGuestLectureRequest.duration = 180
                            setEndTime(180)}
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        if (viewModel.getIsLaunchedForEdit()) {
            val duration = viewModel.addGuestLectureRequest.duration
            Log.d(TAG, "duration : " + duration)
            val index = getIndexForDuration(duration!!)
            binding.spinnerDuration.setSelection(index)
            setEndTime(180)
        }
    }

    private fun getIndexForDuration(duration: Int): Int {
        var toReturn = 0
        when (duration) {
            30 -> toReturn = 1
            60 -> toReturn = 2
            90 -> toReturn = 3
            120 -> toReturn = 4
            150 -> toReturn = 5
            180 -> toReturn = 6
        }
        Log.d(TAG, "getIndexForDuration toReturn : " + toReturn)
        return toReturn
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etLectureDate.setText(sdf.format(myCalendar.time))
        validateAndCalculateEndDate()
    }

    private fun showDatePicker(date: DatePickerDialog.OnDateSetListener) {
/*        val todayCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.newInstance(date, todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DAY_OF_MONTH))
        val minDateCalender = Calendar.getInstance()
        if(viewModel.getIsLaunchedForEdit()){
            val previouslySelectedDateString = viewModel.addGuestLectureRequest.lectureDate
            Log.d(TAG, "showDatePicker: previouslySelectedDate : "+previouslySelectedDateString)
//            val formatter: DateFormat = SimpleDateFormat("MM/dd/yy")
//            val previouslySelectedDate : Date? = formatter.parse(previouslySelectedDateString)
//            previouslySelectedDate?.let { previouslySelectedDateNotNull ->
//                minDateCalender.time = previouslySelectedDateNotNull
//            }
            minDateCalender.set(todayCalendar.get(Calendar.YEAR), minDateCalender.get(Calendar.MONTH), minDateCalender.get(Calendar.DATE))
        }else{
            minDateCalender.set(todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DATE) + 3)
        }
        datePickerDialog.minDate = minDateCalender
        val maxDateCalendar: Calendar = Calendar.getInstance()
        maxDateCalendar.set(todayCalendar.get(Calendar.YEAR) + 1, todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DATE))
        datePickerDialog.maxDate = maxDateCalendar
        datePickerDialog.show(childFragmentManager, "Datepickerdialog")*/

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
            if ((dayOfWeek == Calendar.MONDAY && viewModel.addGuestLectureRequest.dayMon!= 1) ||
                (dayOfWeek == Calendar.TUESDAY && viewModel.addGuestLectureRequest.dayTue!=1) ||
                (dayOfWeek == Calendar.WEDNESDAY && viewModel.addGuestLectureRequest.dayWed!=1) ||
                (dayOfWeek == Calendar.THURSDAY && viewModel.addGuestLectureRequest.dayThu!=1) ||
                (dayOfWeek == Calendar.FRIDAY && viewModel.addGuestLectureRequest.dayFri!=1) ||
                (dayOfWeek == Calendar.SATURDAY && viewModel.addGuestLectureRequest.daySat!=1) ||
                (dayOfWeek == Calendar.SUNDAY && viewModel.addGuestLectureRequest.daySun!=1)
            ) {
                val disabledDays = arrayOfNulls<Calendar>(1)
                disabledDays[0] = loopdate
                datePickerDialog.disabledDays = disabledDays
            }
            myCalendar.add(Calendar.DATE, 1)
            loopdate = myCalendar

        }
        datePickerDialog.show(childFragmentManager, "Datepickerdialog")
    }

    private fun showTimePicker() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        var amPm: Int
        mTimePicker = TimePickerDialog(
            activity,
            { timePicker, hourOfDay, minutes ->
                if (hourOfDay > 12) {
                    valuehour = hourOfDay
                    valuehour -= 12
                    amPm = 1
                } else if (hourOfDay == 0) {
                    valuehour = hourOfDay
                    valuehour += 12
                    amPm = 0
                } else if (hourOfDay == 12) {
                    valuehour = hourOfDay
                    amPm = 1
                } else {
                    valuehour = hourOfDay
                    amPm = 0
                }
                startTimeCalendar.set(Calendar.HOUR, hourOfDay)
                startTimeCalendar.set(Calendar.MINUTE, minutes)
                if (amPm == 0) {
                    startTimeCalendar.set(Calendar.AM_PM, Calendar.AM)
                    Log.d(TAG, "showTimePicker: ${startTimeCalendar.get(Calendar.AM_PM)}")
                } else {
                    startTimeCalendar.set(Calendar.AM_PM, Calendar.PM)
                    Log.d(TAG, "showTimePicker: ${startTimeCalendar.get(Calendar.AM_PM)}")
                }
                val amPm: String = if (amPm == 1) "PM" else "AM"
                val selectedTime =
                    "${startTimeCalendar.get(java.util.Calendar.HOUR)}:${startTimeCalendar.get(java.util.Calendar.MINUTE)} $amPm"
                binding.etStartTime.setText(selectedTime)
                validateAndCalculateEndDate()
            }, hour, minute, false
        )
        mTimePicker.show()
    }

    fun getDataFromRichTextEditor(): String? {
        return binding.richEditor.html
    }

    fun onBoldIconTouch(view: View) {
        val imageButton = view as ImageButton
        richEditor.setBold()
        if (isBoldSelected) {
            isBoldSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
//            richEditor.removeFormat()
        } else {
            isBoldSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onItalicTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setItalic()
        if (isItalicSelected) {
            isItalicSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
//            richEditor.removeFormat()
        } else {
            isItalicSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onUnderlineTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setUnderline()
        if (isUnderLineSelected) {
            isUnderLineSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
        } else {
            isUnderLineSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onBulletsTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setBullets()
        if (isBulletsSelected) {
            isBulletsSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
        } else {
            isBulletsSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onNumbersTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setNumbers()
        if (isNumberSelected) {
            isNumberSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
        } else {
            isNumberSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_active
                )
            )
        }
    }

    private fun showDialogToSelectLanguages() {
        Log.d(TAG, "showDialog clicked")
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.language_multi_selector)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rv_language)
        Log.d(TAG, "selectedLanguages : " + viewModel.selectedLanguages.toString())
        val languageList = viewModel.selectedLanguages
        val languageAdapter = LanguageAdapter(languageList)
        recyclerView.adapter = languageAdapter
        val submitText = dialog.findViewById<AppCompatTextView>(R.id.tv_submit)
        val cancelText = dialog.findViewById<AppCompatTextView>(R.id.tv_cancel)
        submitText.setOnClickListener {
            viewModel.updateLanguageSelection(languageAdapter.mLanguageList)
            recyclerView.adapter = null
            dialog.dismiss()
        }
        cancelText.setOnClickListener {
            Log.d(TAG, "onCancel click  adapter list : " + languageAdapter.mLanguageList.toString())
            Log.d(TAG, "onCancel click  viewModel list : " + viewModel.selectedLanguages.toString())
            recyclerView.adapter = null
            dialog.dismiss()
        }
        dialog.show()
    }




    private fun setTotalCourseDuration() {
        viewModel.noOfAttendeesMutLiveData.value?.let {it->
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
                    if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && viewModel.addGuestLectureRequest.dayMon == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && viewModel.addGuestLectureRequest.dayTue == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY && viewModel.addGuestLectureRequest.dayWed == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY && viewModel.addGuestLectureRequest.dayThu == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && viewModel.addGuestLectureRequest.dayFri == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && viewModel.addGuestLectureRequest.daySat == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    } else if (loopCalander.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && viewModel.addGuestLectureRequest.daySun == 1) {
                        checkJSONObject = createJsonObjectOfAllSelectedDates(loopCalander)
                        checkObject.put(checkJSONObject)
                    }

                    loopCalander.add(Calendar.DATE, 1)

                }
                val info = checkObject.get(checkObject.length() - 1) as JSONObject
                viewModel.datesJson.value = checkObject.toBase64()
                val endate = info.getLong("end_time")
                val formatter: DateFormat = SimpleDateFormat("MMM d, y, h:mm:ss a")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = endate
                val d = formatter.format(calendar.time)
                viewModel.endCalendar.value = calendar
                viewModel.endDate.value = d.toString()
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

    private fun setEndTime(addMinutes: Int) {
        val endTimeCalendar = Calendar.getInstance()
        endTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY))
        endTimeCalendar.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE))
        endTimeCalendar.add(Calendar.MINUTE, addMinutes)

        val dateFromat = SimpleDateFormat("hh:mm  aa")
        val todayStr = dateFromat.format(endTimeCalendar.time)
        viewModel.endTime.value = todayStr

        this.endTimeCalendar = endTimeCalendar
        viewModel.endTimeCalendar.value = endTimeCalendar


        // check for other fields and set End Date
        validateAndCalculateEndDate()
    }

    private fun validateAndCalculateEndDate() {
        if(!viewModel.noOfAttendeesMutLiveData.value.isNullOrEmpty()) setTotalCourseDuration()
    }
}