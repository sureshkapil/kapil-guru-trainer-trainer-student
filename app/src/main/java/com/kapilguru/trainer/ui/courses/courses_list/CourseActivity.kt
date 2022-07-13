package com.kapilguru.trainer.ui.courses.courses_list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityCourseBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.studentsList.view.StudentList
import com.kapilguru.trainer.ui.courses.adapter.CourseAdapter
import com.kapilguru.trainer.ui.courses.addcourse.AddCourseActivity
import com.kapilguru.trainer.ui.courses.batchesList.BatchListDetailsActivity
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import com.kapilguru.trainer.ui.courses.courses_list.viewModel.CourseViewModel
import com.kapilguru.trainer.ui.courses.courses_list.viewModel.CourseViewModelFactory
import com.kapilguru.trainer.ui.courses.view_course.ViewCourse
import kotlinx.android.synthetic.main.activity_course.*

class CourseActivity : BaseActivity(), CourseAdapter.OnItemClicked, TwoButtonDialogInteractor {
    private val TAG = "CourseActivity"
    lateinit var viewModel: CourseViewModel
    lateinit var binding: ActivityCourseBinding
    lateinit var adapter: CourseAdapter
    lateinit var dialog: CustomProgressDialog
    var courseListInfo: List<CourseResponse> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course)
        viewModel = ViewModelProvider(this, CourseViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(CourseViewModel::class.java)
        setCustomActionBarListener()
        binding.courseViewModel = viewModel
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        viewModel.getCourseList()
        setAdapter()
        setClickListener()
        observeViewModelData()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.my_courses))
    }

    private fun setAdapter() {
        adapter = CourseAdapter(this as CourseAdapter.OnItemClicked)
        binding.recyclerview.adapter = adapter
    }

    private fun setClickListener() {
        buttonAddCourse.setOnClickListener {
            launchAddCourseActivity()
        }
    }

    private fun launchAddCourseActivity() {
        val intent = Intent(this, AddCourseActivity::class.java)
        intent.putParcelableArrayListExtra(COURSE_INFO_PARAM, courseListInfo as ArrayList<CourseResponse>)
        intent.putExtra(IS_FROM_EDIT_PARAM, false)
        startActivity(intent)
    }

    private fun observeViewModelData() {
        // course List API
        viewModel.courseList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.let { it1 ->
                        it1.courseResponse.let { it2 ->
                            if (it2.isEmpty()) {
                                showCourseEmptyView()
                            } else {
                                adapter.setCourseList(it2)
                                courseListInfo = it2
                            }
                            viewModel.totalCourses.value = it2.size.toString()
                        }
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
        })

        viewModel.deleteResponse.observe(this, Observer {
            when (it.status) {

                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    viewModel.getCourseList()
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

    private fun showCourseEmptyView() {
        binding.coureseNonEmptyView.visibility = View.GONE
        binding.courseEmptyView.root.visibility = View.VISIBLE
        binding.courseEmptyView.actvHeading.text = getString(R.string.this_is_your_space)
        binding.courseEmptyView.actvDescription.text = getString(R.string.get_started_by_adding_courses)
        binding.courseEmptyView.actvAddCourse.text = getString(R.string.add_course_all_caps)
        binding.courseEmptyView.llAddCourse.setOnClickListener {
            launchAddCourseActivity()
        }
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
            builder.create()
        }
        alertDialog.show()
    }

    override fun onItemClick(courseTitle: String, courseId: Int, courseResponse: CourseResponse) {
        val intent = Intent(this, BatchListDetailsActivity::class.java)
        intent.putExtra("courseId", courseId)
        intent.putExtra(COURSE_NAME_PARAM, courseTitle)
        intent.putExtra(IS_SUBMITTED_PARAM, courseResponse.isSubmitted)
        startActivity(intent)
    }

    override fun onEditCLick(selectedCourse: CourseResponse) {
            selectedCourse.batchesCount?.let { batchCount ->
                if(selectedCourse.studentsCount == null) {
                    // enter into edit
                    navigateToAddCourse(selectedCourse)
                } else {
                    showSingleButtonErrorDialog(this,message = "There are already students enrolled for  ${selectedCourse.courseTitle}")
                }
            } ?: run {
                navigateToAddCourse(selectedCourse)
            }
    }

    override fun onDeleteClick(selectedCourse: CourseResponse) = selectedCourse.batchesCount?.let {
        showSingleButtonErrorDialog(this,message = String.format(getString(R.string.delete_batch_negative_msg), selectedCourse.courseTitle!!.toUpperCase()))
    }?: kotlin.run {
        viewModel.deleteCourseId = selectedCourse.courseId.toString()
        showTwoButtonDialog(selectedCourse.courseTitle!!.toUpperCase())
    }

    override fun onShareClick(selectedCourse: CourseResponse) {
        shareIntent(shareText = BuildConfig.SHARE_URL + COURSE_DETAILS + selectedCourse.courseCode, context = this)
    }

    override fun onStudentsClick(selectedCourse: CourseResponse) {
        selectedCourse.studentsCount?.let {
     val bundle =  bundleOf(PARAM_IS_FROM to PARAM_IS_FROM_COURSE, PARAM_COURSE_ID to selectedCourse.courseId.toString())
        val intent =Intent(this,StudentList::class.java).apply {
            putExtras(bundle) }
        startActivity(intent)
        }
    }

    override fun onViewClick(selectedCourse: CourseResponse) {
        val bundle = bundleOf(PARAM_IS_FROM to PARAM_IS_FROM_COURSE, PARAM_COURSE_ID to selectedCourse.courseId.toString())
        val intent = Intent(this, ViewCourse::class.java).apply {
            putExtras(bundle)
        }
        startActivity(intent)
    }

    private fun showTwoButtonDialog(courseTitle: String) {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: TwoButtonDialog = TwoButtonDialog.newInstance(
            String.format(getString(R.string.you_want_to_delete),courseTitle),this)
        editNameDialogFragment.show(fm, "two_button_dialog")
    }

    private fun navigateToAddCourse(selectedCourse: CourseResponse) {
        val intent = Intent(this, AddCourseActivity::class.java)
        intent.putParcelableArrayListExtra(COURSE_INFO_PARAM, courseListInfo as ArrayList<CourseResponse>)
        intent.putExtra(IS_FROM_EDIT_PARAM, true)
        intent.putExtra(COURSE_ID_PARAM, selectedCourse.courseId)
        startActivity(intent)
    }

    override fun onNegativeClick() {
        // Nothing
    }

    override fun onPositiveClick() {
        viewModel.deleteCourseId?.let{
            viewModel.deleteCourse(it)
        }
    }
}