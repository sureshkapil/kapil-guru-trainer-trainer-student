package com.kapilguru.trainer.ui.webiner.addWebinar

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.*
import com.kapilguru.trainer.Companion.unWrapUTCToDate
import com.kapilguru.trainer.Companion.unWrapUTCToTime
import com.kapilguru.trainer.databinding.FragmentAddWebinarDetailsBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.LanguageAdapter
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.webiner.addWebinar.viewModel.AddWebinarViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AddWebinarDetailsFragment : Fragment() {
    private val TAG = "AddWebinarDetailsFragment"
    lateinit var binding: FragmentAddWebinarDetailsBinding
    val viewModel: AddWebinarViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    var startDateCalendar: Calendar = Calendar.getInstance()
    var endDateCalendar: Calendar = Calendar.getInstance()
    var isBoldSelected: Boolean = false
    var isItalicSelected: Boolean = false
    var isUnderLineSelected: Boolean = false
    var isBulletsSelected: Boolean = false
    var isNumberSelected: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddWebinarDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.addWebViewModel = viewModel
        progressDialog = CustomProgressDialog(requireContext())
        observeCourseLanguageData()
        observeFetchWebinarDataApiResponse()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun observeCourseLanguageData() {
        viewModel.webinarLanguages.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    viewModel.selectedLanguages = it.data?.languageData!!
                    checkAndSetLanguages(it.data.languageData)
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetLanguages(data: ArrayList<LanguageData>) {
        if (viewModel.getIsLaunchedForEdit()) {
            viewModel.languagesForWebinar.observe(viewLifecycleOwner, { languagesForWebinar ->
                languagesForWebinar?.let {
                    val listOfSelectedLanguageIds = getSelectedLanguagesIdList(it)
                    for (id in listOfSelectedLanguageIds) {
                        for (languageData in data) {
                            if (languageData.id == id) {
                                languageData.isSelected = true
                                break
                            }
                        }
                    }
                    viewModel.updateLanguageSelection(data)
                }
            })
        }
    }

    private fun getSelectedLanguagesIdList(selectedLanguages: String): List<Int> {
        val listOfSelectedLanguagesId = ArrayList<Int>()
        for (i in selectedLanguages.indices) {
            if (!i.equals(",")) {
                listOfSelectedLanguagesId.add(i)
            }
        }
        return listOfSelectedLanguagesId
    }

    private fun observeFetchWebinarDataApiResponse() {
        viewModel.webinarResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    apiResponse.data?.webinarData?.let { it ->
                        val webinarData = it[0]
                        viewModel.addWebinarRequest.value?.title = webinarData.title
                        viewModel.addWebinarRequest.value?.startDate = webinarData.startDate.apiDateFormatWithoutT()
                        viewModel.addWebinarRequest.value?.endDate = webinarData.endDate.apiDateFormatWithoutT()
                        viewModel.addWebinarRequest.value?.noOfAttendees = webinarData.noOfAttendees.toString()
                        viewModel.addWebinarRequest.value?.price = webinarData.price.toString()
                        viewModel.addWebinarRequest.value?.speakerName = webinarData.speakerName
                        webinarData.durationDays.let {
                            viewModel.addWebinarRequest.value?.durationDays = it.toString()

                        }
//                        viewModel.addWebinarRequest.value?.startTime = webinarData.startTime
//                        viewModel.addWebinarRequest.value?.endTime = webinarData.endTime
                        viewModel.languagesForWebinar.value = webinarData.languages
                        viewModel.addWebinarRequest.value?.about = webinarData.about
                        viewModel.addWebinarRequest.value?.durationHrs = webinarData.durationHrs.toString()
                        viewModel.addWebinarRequest.value?.image = webinarData.image
                    }
                    if (viewModel.getIsLaunchedForEdit()) {
                        showWebinarDetails()
                    } else {
                        setTrainerName()
                    }
                    progressDialog.dismissLoadingDialog()

                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    /*values which can not be set by two way data binding are set here
    ex : description, webinar Duration*/
    private fun showWebinarDetails() {
        val duration: Int = Integer.parseInt(viewModel.addWebinarRequest.value?.durationHrs!!)
        setWebinarDuration(duration)
        setDescription(viewModel.addWebinarRequest.value?.about?.fromBase64ToString())
        binding.addWebViewModel = viewModel //setting viewModel again as the ui is not updating when the data in mutable live data is changed
    }

    private fun setWebinarDuration(duration: Int) {
        val index = getIndexForDuration(duration)
        binding.aCWebinarSpinner.setSelection(index)
    }

    private fun getIndexForDuration(duration: Int): Int {
        return when (duration) {
            30 -> 1
            60 -> 2
            90 -> 3
            120 -> 4
            150 -> 5
            180 -> 6
            else -> 0
        }
    }

    private fun setDescription(it: String?) {
        binding.richEditorWebinar.html = it
    }

    private fun setTrainerName() {
        val trainerName = StorePreferences(requireContext()).userName
        viewModel.addWebinarRequest.value?.speakerName = trainerName
    }

    private fun setClickListeners() {
        setStartDateClickListener()
        setEndDateClickListener()
        setStartTimeClickListener()
        binding.aCWebinarSpinner.onItemSelectedListener = SpinnerSelectListener()
        binding.aCSpinnerLanguage.setOnClickListener {
            showDialogToSelectLanguages()
        }
    }

    private fun setStartDateClickListener() {
        val startDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            startDateCalendar.set(Calendar.YEAR, year)
            startDateCalendar.set(Calendar.MONTH, monthOfYear)
            startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            Log.d(TAG, "setStartDateClickListener: ${viewModel.addWebinarRequest.value?.startDate}")
            val updatedDate = updateDateAndTime(viewModel.addWebinarRequest.value?.startDate, year, monthOfYear, dayOfMonth, 0, 0, true)
            viewModel.addWebinarRequest.value?.startDate = updatedDate
            Log.d(TAG, "setStartDateClickListener: updatedDate : " + updatedDate)
            (binding.tietStartDate as TextView).unWrapUTCToDate(updatedDate) // TODO : value should not be set through text view, instead it should change as it is mutable live data. Need to check
            calculateAndSetDurationDays()
        }
        binding.tietStartDate.setOnClickListener {
            showStartDatePicker(startDateSetListener)
        }
    }

    /*@param currentDateAndTime is the viewModel date and time in api format
    @param year is the selected year to be replaced
    @param month is the selected month to be replaced
    @param day is the selected day to be replaced
    @param hour is the selected hour to be replaced
    @param minute is the selected minute to be replaced
    @param shouldUpdateDate is the boolean which tells to replace either date or time.
    * */
    private fun updateDateAndTime(currentDateAndTime: String?, year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, shouldUpdateDate: Boolean): String {
        val apiDateAndTimeFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T, Locale.US)
        apiDateAndTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance()
        currentDateAndTime?.let {
            calendar.time = apiDateAndTimeFormat.parse(it)!!
        } ?: kotlin.run {
            calendar.set(Calendar.YEAR, 0)
            calendar.set(Calendar.MONTH, 0)
            calendar.set(Calendar.DAY_OF_MONTH, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
        }
        if (shouldUpdateDate) {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
        }
        return apiDateAndTimeFormat.format(calendar.time)
    }

    private fun setEndDateClickListener() {
        val onEndDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            endDateCalendar.set(Calendar.YEAR, year)
            endDateCalendar.set(Calendar.MONTH, monthOfYear)
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val updatedDate = updateDateAndTime(viewModel.addWebinarRequest.value?.startDate, year, monthOfYear, dayOfMonth, 0, 0, true)
            viewModel.addWebinarRequest.value?.endDate = updatedDate
            (binding.tietEndDate as TextView).unWrapUTCToDate(updatedDate)
            calculateAndSetDurationDays()
        }
        binding.tietEndDate.setOnClickListener {
            showEndDatePicker(onEndDateSetListener)
        }
    }

    private fun setStartTimeClickListener() {
        binding.tietStartTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime[Calendar.HOUR_OF_DAY]
        val minute = currentTime[Calendar.MINUTE]
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, selectedHour, selectedMin ->
            val updatedTimeAndDate = updateDateAndTime(viewModel.addWebinarRequest.value?.startDate, 0, 0, 0, selectedHour, selectedMin, false)
            viewModel.addWebinarRequest.value?.startDate = updatedTimeAndDate
            (binding.tietStartTime as TextView).unWrapUTCToTime(updatedTimeAndDate)
            checkAndSetDuration()
        }
        val mTimePicker = TimePickerDialog(activity, timeSetListener, hour, minute, false)
        mTimePicker.show()
    }

    private fun checkAndSetDuration() {
        viewModel.addWebinarRequest.value?.durationHrs?.let { durationHours ->
            setWebinarDuration(Integer.parseInt(durationHours))
        }
    }

    private fun showStartDatePicker(date: DatePickerDialog.OnDateSetListener) {
        val calender: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.newInstance(date, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH))
        val minDateCalendar: Calendar = Calendar.getInstance()
        if(viewModel.isLaunchedForEdit){
            minDateCalendar.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE))
        }else{
            minDateCalendar.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE) + 3)
        }
        datePickerDialog.minDate = minDateCalendar
        datePickerDialog.show(childFragmentManager, "Datepickerdialog")
    }

    private fun showEndDatePicker(date: DatePickerDialog.OnDateSetListener) {
        val current = Calendar.getInstance()
        val defaultSelectedDate: Calendar = Calendar.getInstance()
            if (this.startDateCalendar.get(Calendar.DATE) == current.get(Calendar.DATE)) {
                current.add(Calendar.DATE, 3)
                defaultSelectedDate.time = current.time
        } else {
                defaultSelectedDate.time = startDateCalendar.time
        }
        val datePickerDialog = DatePickerDialog.newInstance(
            date, defaultSelectedDate.get(Calendar.YEAR), defaultSelectedDate.get(Calendar.MONTH), defaultSelectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.minDate = defaultSelectedDate
        defaultSelectedDate.add(Calendar.DAY_OF_MONTH, 6)
        datePickerDialog.maxDate = defaultSelectedDate
        datePickerDialog.show(childFragmentManager, "Datepickerdialog")
    }

    private fun calculateAndSetDurationDays() {
        val simpleDateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T, Locale.US)
        val startDateCalendar = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()
        val startTime = viewModel.addWebinarRequest.value?.startDate
        val endTime = viewModel.addWebinarRequest.value?.endDate
        startTime?.let { startTimeNotNull ->
            endTime?.let { endTimeNotNull ->
                startDateCalendar.time = simpleDateFormat.parse(startTimeNotNull)
                endDateCalendar.time = simpleDateFormat.parse(endTimeNotNull)
                val durationInMills = endDateCalendar.timeInMillis - startDateCalendar.timeInMillis
                val durationInDays = (TimeUnit.MILLISECONDS.toDays(Math.abs(durationInMills)) + 1).toString()
                viewModel.addWebinarRequest.value?.durationDays = durationInDays
                binding.aCETDays.setText(durationInDays)
            }
        }
    }

    inner class SpinnerSelectListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                1 -> {
                    setEndTime(30)
                }
                2 -> {
                    setEndTime(60)
                }
                3 -> {
                    setEndTime(90)
                }
                4 -> {
                    setEndTime(120)
                }
                5 -> {
                    setEndTime(150)
                }
                6 -> {
                    setEndTime(180)
                }
                else -> {
                }
            }
        }
    }

    /*This method adds duration of time to these values
    * Values are date in endDateAndTime, time in satrtDateAndTime. Thereby resulting in endDateAndTime  */
    private fun setEndTime(durationMinutes: Int) {
        if (TextUtils.isEmpty(binding.tietStartDate.text)) {
            showAppToast(requireContext(), "Please select Start Date")
            return
        }
        if (TextUtils.isEmpty(binding.tietStartTime.text)) {
            showAppToast(requireContext(), "Please select Start Time")
            return
        }

        // fetch start date to set endTime
        val startDateAndTimeCalendar = Calendar.getInstance()
        val startDateAndTimeString = viewModel.addWebinarRequest.value?.startDate

        // fetch endDate
        val endDateAndTimeCalendar = Calendar.getInstance()
        val endDateAndTimeString = viewModel.addWebinarRequest.value?.endDate

        // convert start Date to UTC
        val apiFormatDateAndTime = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITHOUT_T)
        apiFormatDateAndTime.timeZone = TimeZone.getTimeZone("UTC")

        //
        startDateAndTimeCalendar.time = apiFormatDateAndTime.parse(startDateAndTimeString)
        endDateAndTimeCalendar.time = apiFormatDateAndTime.parse(endDateAndTimeString)

        endDateAndTimeCalendar.set(Calendar.HOUR_OF_DAY, startDateAndTimeCalendar.get(Calendar.HOUR_OF_DAY))
        endDateAndTimeCalendar.set(Calendar.MINUTE, startDateAndTimeCalendar.get(Calendar.MINUTE))
        endDateAndTimeCalendar.add(Calendar.MINUTE, durationMinutes)

        //set duration
        viewModel.addWebinarRequest.value?.durationHrs = durationMinutes.toString()
        //set end date(date and time) in api format
        val endDateAndTime = apiFormatDateAndTime.format(endDateAndTimeCalendar.time)
        viewModel.addWebinarRequest.value?.endDate = endDateAndTime
        //set end time only
        val uiFormatTime = SimpleDateFormat(UI_FORMAT_TIME)
        val endTime = uiFormatTime.format(endDateAndTimeCalendar.time)
        viewModel.addWebinarRequest.value?.endTime = endTime
        (binding.tietEndTime as TextView).unWrapUTCToTime(endDateAndTime)
    }

    fun getDataFromRichTextEditor(): String {
        return if (!binding.richEditorWebinar.html.isNullOrBlank()) {
            binding.richEditorWebinar.html
        } else {
            ""
        }
    }

    fun onBoldIconTouch(view: View) {
        val imageButton = view as ImageButton
        richEditor.setBold()
        if (isBoldSelected) {
            isBoldSelected = false
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
        } else {
            isBoldSelected = true
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_active))
        }
    }

    fun onItalicTouch(view: View) {
        val imageButton = view as ImageButton
        richEditor.setItalic()
        if (isItalicSelected) {
            isItalicSelected = false
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
        } else {
            isItalicSelected = true
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_active))
        }
    }

    fun onUnderlineTouch(view: View) {
        val imageButton = view as ImageButton
        richEditor.setUnderline()
        if (isUnderLineSelected) {
            isUnderLineSelected = false
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_in_active))
        } else {
            isUnderLineSelected = true
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_active))
        }
    }

    fun onBulletsTouch(view: View) {
        val imageButton = view as ImageButton
        richEditor.setBullets()
        if (isBulletsSelected) {
            isBulletsSelected = false
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
        } else {
            isBulletsSelected = true
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_active))
        }
    }

    fun onNumbersTouch(view: View) {
        val imageButton = view as ImageButton
        richEditor.setNumbers()
        if (isNumberSelected) {
            isNumberSelected = false
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
        } else {
            isNumberSelected = true
            imageButton.setColorFilter(ContextCompat.getColor(view.context, R.color.rich_txt_editor_active))
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
        var languageAdapter = LanguageAdapter(viewModel.selectedLanguages)
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
//            languageAdapter = null
            recyclerView.adapter = null
            dialog.dismiss()
        }
        dialog.show()
    }
}