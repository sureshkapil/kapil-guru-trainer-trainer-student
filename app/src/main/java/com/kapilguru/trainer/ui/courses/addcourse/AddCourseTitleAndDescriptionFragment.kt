package com.kapilguru.trainer.ui.courses.addcourse

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentAddCourseTitileAndDescriptionBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.adapter.CategoryBaseAdapter
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModel
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.android.synthetic.main.countryname_spinner_item.view.*
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.*


class AddCourseTitleAndDescriptionFragment : Fragment() {

    var courseResponse: ArrayList<CourseResponse>? = null
    lateinit var categoryBaseAdapter: CategoryBaseAdapter
    lateinit var viewBinding: FragmentAddCourseTitileAndDescriptionBinding
    var isBoldSelected: Boolean = false
    var isItalicSelected : Boolean = false
    var isUnderLineSelected : Boolean = false
    var isBulletsSelected : Boolean = false
    var isNumberSelected : Boolean = false
    val viewModel: AddCourseViewModel by activityViewModels()
    lateinit var dialog : CustomProgressDialog

    private  val TAG = "AddCourseTitleAndDescri"

    companion object {
        fun newInstance(courseListInfo: ArrayList<CourseResponse>) =
            AddCourseTitleAndDescriptionFragment().apply {
           arguments =  Bundle().apply {
                putParcelableArrayList("abc",courseListInfo)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentAddCourseTitileAndDescriptionBinding.inflate(inflater)
        viewBinding.viewModel = viewModel
        viewBinding.clickHandlers = this
        viewBinding.lifecycleOwner = this.requireActivity()
        if (viewModel.isEdit) {
            val aboutTrainer = viewModel.addCourseRequest.description.toString()
            val convertedInfo = aboutTrainer.fromBase64ToString()
            viewBinding.richEditor.html = convertedInfo
        }
        fetchCategoryApi()
        courseResponse = arguments?.getParcelableArrayList("abc")
        return viewBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setDrawableClickListner() {
        viewBinding.aCETCourseTitleValue.setOnTouchListener(CustomDrawableClickListener( textInputEditText =aCETCourseTitleValue,
            title = "Give a familiar Course Title",
            subTitle = "Course search depends on course title",
            lifecycleOwner = this))

        viewBinding.aCETCourseSubTitleValue.setOnTouchListener(CustomDrawableClickListener( textInputEditText =aCETCourseSubTitleValue,
            title = "What students will learn in your course",
            subTitle = "(Do not add your personal information, contact number, Mail id, Address, Company Name Etc)",
            lifecycleOwner = this))

        viewBinding.aCETCoursePriceValue.setOnTouchListener(CustomDrawableClickListener( textInputEditText =aCETCourseSubTitleValue,
            title = "What is the Course price?",
            subTitle = "(Price of the course)",
            lifecycleOwner = this))

        viewBinding.aCETCoursePriceValue.setOnTouchListener(CustomDrawableClickListener( textInputEditText =aCETCourseSubTitleValue,
            title = "What is the offered price ?",
            subTitle = "(Discounted Price)",
            lifecycleOwner = this))

        viewBinding.aCETCoursePriceValue.setOnTouchListener(CustomDrawableClickListener( textInputEditText =aCETCourseSubTitleValue,
            title = "What is the no. of sessions ?",
            subTitle = "(No.of Sessions not less than 10 and greater than 100.)",
            lifecycleOwner = this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrawableClickListner()
        categoryBaseAdapter = CategoryBaseAdapter(activity)
        viewBinding.aCSpinnerCategory.adapter = categoryBaseAdapter

        viewBinding.aCSpinnerCategory.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setSelectedCourseId(id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        };


        viewBinding.aCETCourseTitleValue.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                   val data = courseResponse?.indexOfFirst { it-> it.courseTitle.equals(viewBinding.aCETCourseTitleValue.text.toString()) }
                    if (data != -1) {
                        viewBinding.aCETCourseTitleValue.setText("")
                        showToastMessage("Mentioned Course Title already Exists, please choose another one")
                    }

                }

            }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this.requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    private fun setSelectedCourseId(id: Long) {
        val info = viewModel.categoryListApi.value?.data?.data?.get(id.toInt())
        viewModel.addCourseRequest.categoryID = info?.id.toString()
    }

    fun getDataFromRichTextEditor(): String {
        return if(!viewBinding.richEditor.html.isNullOrBlank()) {
            viewBinding.richEditor.html
        } else {
            ""
        }
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




    private fun fetchCategoryApi() {
        viewModel.categoryListApi.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.let { it1 ->
                        Log.v("it1", it1.data.toString())
                        categoryBaseAdapter.setCategoryList(it1.data)
                    }


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
        }
        )
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog? = this.let {
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        setCancelable(true)
                    })

                setMessage(message)

            }

            // Create the AlertDialog
            builder?.create()
        }
        alertDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}