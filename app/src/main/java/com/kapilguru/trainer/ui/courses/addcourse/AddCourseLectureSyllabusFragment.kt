package com.kapilguru.trainer.ui.courses.addcourse

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentAddCourseLectureSyllabusBinding
import com.kapilguru.trainer.ui.courses.addcourse.models.LectureSyllabus
import com.kapilguru.trainer.ui.courses.addcourse.models.LectureSyllabusContent
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModel
import kotlinx.android.synthetic.main.add_syllabus.view.*
import kotlinx.android.synthetic.main.fragment_add_course_lecture_syllabus.*
import kotlinx.android.synthetic.main.fragment_add_course_lecture_syllabus.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AddCourseLectureSyllabusFragment : Fragment() {
    lateinit var viewBinding: FragmentAddCourseLectureSyllabusBinding
    val viewModel: AddCourseViewModel by activityViewModels()
    var pdfFileName: String? = null
    var downloadPdfUrl: String? = null
    private val TAG = "AddCourseLectureSyllabu"
    lateinit var appUri: Uri
    var filepath: File? = null
    val pickPdf = registerForActivityResult(ActivityResultContracts.GetContent()) { it ->
        it?.let { uri ->
            appUri = it
            pdfFileName = getFileName(context = requireContext(), uri = it)
            pdfFileName?.let { name ->
                fileName.text = name.split(".")[0]
                filepath = fetchFilePath(context = requireContext(), fileName.text.toString())
                copy(requireContext(), it, filepath)
                viewModel.isPdfUploaded = true
            } ?: run {
                Toast.makeText(requireContext(), "uploaded pdf is corrupted,Please try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_course_lecture_syllabus, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.clickHandler = this
        if (viewModel.isEdit) {
            if (viewModel.addCourseRequest.syllabusType == "json") {
                convertBase64ToReadableData(viewModel.addCourseRequest.syllabusTextContent)
                setTypeAndVisibilityOfRadioButtons(false)
                changeCheckedStatus(false)
            } else if (viewModel.addCourseRequest.syllabusType == "pdf") {
                downloadPDfText.visibility = View.VISIBLE
                viewModel.getPDF(viewModel.addCourseRequest.syllabusAttachment.toString())
                setTypeAndVisibilityOfRadioButtons(true)
                changeCheckedStatus(true)
            }
        }
        radioButtonListener()
        uploadPdfListener()
        downloadPdfClickListener()
        viewModelObserver()
    }

    private fun changeCheckedStatus(isPdfCheckd: Boolean) {
        rButtonUpload.isChecked = isPdfCheckd
        rButtonCreate.isChecked = !isPdfCheckd
    }

    private fun downloadPdfClickListener() {
        downloadPDfText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.BASE_URL + "courseFiles/files/" + downloadPdfUrl))
            startActivity(browserIntent)
        }
    }

    private fun viewModelObserver() {
        viewModel.pdfName.observe(viewLifecycleOwner, Observer {
            fileName.text = it
        })
        viewModel.downloadPdfUrl.observe(viewLifecycleOwner, Observer {
            downloadPdfUrl = it
        })
    }

    private fun uploadPdfListener() {
        viewBinding.uploadButton.setOnClickListener {
            pickPdf.launch("application/pdf")
        }
    }

    private fun radioButtonListener() {
        viewBinding.rGroupSyllabusType.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {

            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.rButtonUpload -> {
                        setTypeAndVisibilityOfRadioButtons(true)
                    }

                    R.id.rButtonCreate -> {
                        setTypeAndVisibilityOfRadioButtons(false)
                    }
                }
            }
        })
    }

    private fun setTypeAndVisibilityOfRadioButtons(showPdf: Boolean) {
        viewModel.isPdfSelected = showPdf
        uploadCreateSyllabusVisibility(showPdf)
    }

    private fun convertBase64ToReadableData(syllabusTextContent: String?) {
        val data = syllabusTextContent?.fromBase64ToString()
        data?.let {
            val abc = JSONArray(it)
            for (i in 0 until abc.length()) {
                val jsonObject: JSONObject = abc.getJSONObject(i)
                setEditDynamicLayout(
                    jsonObject.getString("syllabusHeading"), jsonObject.getString("syllabusSubHeading")
                )
            }
        }
    }

    fun onAddClick(view: View) {
        setDynamicLayout()
        viewBinding.scrollveiw.post {
            viewBinding.scrollveiw.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun setDynamicLayout() {
        val vi = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = vi.inflate(R.layout.add_syllabus, null)
        v.imageView.setOnClickListener {
            viewBinding.lLayoutParent.removeView(it.parent as View)
        }

        viewBinding.lLayoutParent.addView(v)
    }

    private fun uploadCreateSyllabusVisibility(isVisible: Boolean) {
        if (isVisible) {
            viewBinding.uploadLayout.visibility = View.VISIBLE
            viewBinding.scrollveiw.visibility = View.GONE
            viewBinding.addSyllabusButton.visibility = View.GONE
        } else {
            viewBinding.uploadLayout.visibility = View.GONE
            scrollveiw.scrollveiw.visibility = View.VISIBLE
            viewBinding.addSyllabusButton.visibility = View.VISIBLE
        }
    }

    private fun setEditDynamicLayout(Heading: String, subHeading: String) {
        val vi = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = vi.inflate(R.layout.add_syllabus, null)
        v.findViewById<TextView>(R.id.syllabus_title).text = Heading
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            v.findViewById<TextView>(R.id.syllabus_content).text = Html.fromHtml(subHeading, Html.FROM_HTML_MODE_LEGACY)
        } else {
            v.findViewById<TextView>(R.id.syllabus_content).text = Html.fromHtml(subHeading)
        }

        v.imageView.setOnClickListener {
            viewBinding.lLayoutParent.removeView(it.parent as View)

        }

        viewBinding.lLayoutParent.addView(v)
    }

    fun getEntireSyllabusAsBase64(): String? {
        var didBreakBetween = false
        val data = LectureSyllabusContent()
        var syllabusArray: JSONArray? = null
        var endDataTOBase4: String? = null
        for (i in 0 until viewBinding.lLayoutParent.childCount) {
            val child = viewBinding.lLayoutParent[i] as ConstraintLayout
            val syllabusTitle = child.findViewById<EditText>(R.id.syllabus_title)
            val syllabusSubTitle = child.findViewById<EditText>(R.id.syllabus_content)

            if (syllabusTitle.text.isNullOrBlank()) {
                showToastMessage("please fill information")
                syllabusTitle.requestFocus()
                didBreakBetween = true
                break
            }

            data.add(LectureSyllabus().apply {
                this.id = i
                this.syllabusHeading = syllabusTitle.text.toString().trim()
                this.syllabusSubHeading = syllabusSubTitle.text.toString().trim()
            })
        }

        if (viewBinding.lLayoutParent.childCount == 0) {
            showToastMessage("please add syllabus Information or attach syllabus document")
        } else {
            if (!didBreakBetween) {
                syllabusArray = data.toJSONConverter()
                endDataTOBase4 = syllabusArray.toString().toBase64()
            }
        }
        return endDataTOBase4

    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this.requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    fun getBase64File(): Pair<File?, String?> = Pair(first = filepath, second = fileName.text.toString())
}