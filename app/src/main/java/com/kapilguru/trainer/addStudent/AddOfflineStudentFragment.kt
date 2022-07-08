package com.kapilguru.trainer.addStudent

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentAddOfflineStudentBinding
import com.kapilguru.trainer.network.Status
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddOfflineStudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddOfflineStudentFragment : Fragment(), CalendarSelectionListener {

    lateinit var binding: FragmentAddOfflineStudentBinding
    val viewModel: AddStudentViewModel by viewModels({ requireActivity() })
    lateinit var progressDialog: CustomProgressDialog
    var valuehour: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_offline_student, container, false)

        binding = FragmentAddOfflineStudentBinding.inflate(inflater, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        clickListeners()
        observeViewModelData()
    }


    private fun observeViewModelData() {

        viewModel.errorOfflineText.observe(this.viewLifecycleOwner, { errorText ->
            showToast(errorText)
        })




        viewModel.addOfflineStudentResponse.observe(viewLifecycleOwner, { addOfflineStudentResponse ->
            when (addOfflineStudentResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    addOfflineStudentResponse?.data?.status?.let { it ->
                        if (it == 200) {
                            showToast("Student Added Successfully")
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun showToast(errorText: String) {
        showAppToast(this.requireContext(), errorText)
    }


    fun clickListeners() {

        binding.dateValue.setOnClickListener {
            openCalendar()
        }

        binding.timeValue.setOnClickListener {
            showTimePicker()
        }

        binding.addButton.setOnClickListener {
            viewModel.setBatchStartDate()
            if (viewModel.validateOfflineAddStudentUserData()) {
                viewModel.addOfflineStudent()
            }
        }
    }


    private fun openCalendar() {
        val newFragment: DialogFragment = CustomCalendar(this, false);
        newFragment.show(childFragmentManager, "datePicker");
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = AddOfflineStudentFragment()
    }

    override fun onDateSet(calendarSelectedDate: Calendar) {
        val abc = calendarSelectedDate.convertDateAndTimeToApiDataWithoutT()
        val finalDate = abc.toDateFormatWithOutT()
        binding.dateValue.setText(finalDate)
        viewModel.offlineStudentStartDate.value = calendarSelectedDate
        viewModel.startDate.value = finalDate
    }

    private fun showTimePicker() {

        val mcurrentTime = Calendar.getInstance()
        val mCurrentHour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val mCurrentMinute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        var amPm: Int

        mTimePicker = TimePickerDialog(
            this.requireContext(), { timePicker, hourOfDay, minutes ->
                amPm = isItAMPM(hourOfDay)

                val myCalendar = Calendar.getInstance()
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, minutes)

                val dateFromat = SimpleDateFormat("hh:mm  aa")
                val todayStr = dateFromat.format(myCalendar.time)

                binding.timeValue.setText(todayStr)
                viewModel.offlineStudentStartTime.value = myCalendar
                viewModel.startTime.value = todayStr

            }, mCurrentHour, mCurrentMinute, false
        )

        mTimePicker.show()
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
}