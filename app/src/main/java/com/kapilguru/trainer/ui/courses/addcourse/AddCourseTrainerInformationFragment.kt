package com.kapilguru.trainer.ui.courses.addcourse

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.trainer.CustomDrawableClickListener
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentAddCourseTrainerInformationBinding
import com.kapilguru.trainer.fromBase64ToString
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModel
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.LanguageAdapter
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import kotlinx.android.synthetic.main.fragment_add_course_trainer_information.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCourseTrainerInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCourseTrainerInformationFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    val viewModel: AddCourseViewModel by activityViewModels()
    var isBoldSelected: Boolean = false
    var isItalicSelected : Boolean = false
    var isUnderLineSelected : Boolean = false
    var isBulletsSelected : Boolean = false
    var isNumberSelected : Boolean = false

    lateinit var viewBinding: FragmentAddCourseTrainerInformationBinding
    lateinit var dialog: CustomProgressDialog
    private val TAG = "AddCourseTrainerInforma"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_course_trainer_information,
            container,
            false)

        viewBinding.viewModel = viewModel

        viewBinding.clickHandlers = this

        viewBinding.lifecycleOwner = this

        if (viewModel.isEdit) {
            val aboutTrainer = viewModel.addCourseRequest.aboutTrainer.toString()
            val convertedInfo = aboutTrainer.fromBase64ToString()
            viewBinding.richEditor.html = convertedInfo
        }
            return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        setToolTipClickListener()
        viewModelObserver()
        viewBinding.tvSelectedLanguages.setOnClickListener {
            showDialogToSelectLanguages()
        }
    }

    private fun viewModelObserver() {
        viewModel.courseLanguages.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    viewModel.selectedLanguages = it.data?.languageData!!
                    checkAndSetLanguage(it.data.languageData)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetLanguage(languagesList: ArrayList<LanguageData>) {

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setToolTipClickListener() {
        viewBinding.aCETTrainerNameValue.setOnTouchListener(
            CustomDrawableClickListener( textInputEditText = aCETTrainerNameValue,
                title = "Details of trainer who conduct class",
                lifecycleOwner = this)
        )
        viewBinding.aCETTrainerYerasOfExperianceValue.setOnTouchListener(
            CustomDrawableClickListener( textInputEditText = aCETTrainerYerasOfExperianceValue,
                title = "Trainer Years of Experience",
                lifecycleOwner = this)
        )

        viewBinding.aCETtotalNumberOfStudentsTrainedValue.setOnTouchListener(
            CustomDrawableClickListener( textInputEditText = aCETtotalNumberOfStudentsTrainedValue,
                title = "Total Number of Students Trained by Trainer",
                lifecycleOwner = this)
        )

    }

    fun onBoldIconTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setBold()
        if(isBoldSelected) {
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
        if(isItalicSelected) {
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
        if(isUnderLineSelected) {
            isUnderLineSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
//            richEditor.removeFormat()
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
        if(isBulletsSelected) {
            isBulletsSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
//            richEditor.removeFormat()
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
        if(isNumberSelected) {
            isNumberSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context,
                    R.color.rich_txt_editor_in_active
                )
            )
//            richEditor.removeFormat()
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


    fun getAboutTrainer() : String {

        return if(!viewBinding.richEditor.html.isNullOrBlank()) {
            viewBinding.richEditor.html
        } else {
            ""
        }

//        return richEditor.html.toString().toBase64()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCourseTrainerInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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
        val languageList = viewModel.selectedLanguages;
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

}