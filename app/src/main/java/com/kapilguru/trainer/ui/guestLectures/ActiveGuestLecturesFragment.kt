package com.kapilguru.trainer.ui.guestLectures

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentActiveGuestLecturesBinding
import com.kapilguru.trainer.demo_webinar_students.DemoWebinarStudentActivity
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.AddGuestLectureActivity
import com.kapilguru.trainer.ui.guestLectures.guestLectureDetails.GuestLectureDetailsActivity
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.GuestLectureStudentViewActivity
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData
import com.kapilguru.trainer.ui.guestLectures.viewModel.GuestLectureViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActiveGuestLecturesFragment : Fragment(), GuestLectureAdapter.OnItemClickGuestLecture {
    private val TAG = "ActiveGuestLecturesFragment"
    lateinit var binding: FragmentActiveGuestLecturesBinding
    val viewModel: GuestLectureViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: GuestLectureAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentActiveGuestLecturesBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setClickListeners()
        observeViewModelData()
        getActiveGuestLectures()
    }

    private fun setAdapter() {
        adapter = GuestLectureAdapter(false, this)
        binding.rvGuestLecture.adapter = adapter
    }

    private fun setClickListeners() {
        binding.addGuestLectureButtom.setOnClickListener {
            startActivity(Intent(requireActivity(), AddGuestLectureActivity::class.java))
        }
    }

    private fun observeViewModelData() {
        observeActiveGuestLecturesApiResponse()
        observerDeleteData()
    }

    private fun observeActiveGuestLecturesApiResponse() {
        viewModel.guestLectureResponseList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    it.data?.let { guestLectureListApi ->
                        checkAndSetAdapterData(guestLectureListApi.data as ArrayList<GuestLectureData>)
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })
    }

    private fun observerDeleteData() {
        viewModel.deleteGuestLectureApiResponse.observe(viewLifecycleOwner, Observer { commonResApi ->
            when (commonResApi.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    commonResApi.data?.let {
                        it.commonResponse.let {
                            getActiveGuestLectures()
                        }
                    }
                }

                Status.ERROR -> {
                    if (commonResApi.data?.status != 200) {
                        commonResApi.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetAdapterData(guestLectureList: ArrayList<GuestLectureData>) {
        val activeGuestLectureList = getActiveGuestLectureList(guestLectureList)
        if (activeGuestLectureList.isEmpty()) {
            showGuestLectureListEmptyView(true)
        } else {
            showGuestLectureListEmptyView(false)
            adapter.setGuestLectureList(activeGuestLectureList)
            viewModel.totalGuestLectures.value = activeGuestLectureList.size.toString()
        }
    }

    private fun getActiveGuestLectureList(guestLectureList: ArrayList<GuestLectureData>): ArrayList<GuestLectureData> {
        val activeGuestLectureList = ArrayList<GuestLectureData>()
        for (guestLecture in guestLectureList) {
            val lectureDate = guestLecture.lectureDate
            lectureDate?.let { lectureDateNotNull ->
                val simpleDateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
                simpleDateFormat.timeZone = TimeZone.getTimeZone("IST")
                val guestLectureDate = simpleDateFormat.parse(lectureDateNotNull)
                guestLectureDate?.let { guestLectureDateNotNull ->
                    val guestLectureDurationInMin = guestLecture.duration
                    var guestLectureDurationInMillis = 0
                    guestLectureDurationInMin?.let { guestLectureDurationInMillisNotNull ->
                        guestLectureDurationInMillis = guestLectureDurationInMillisNotNull * 60 * 1000
                    }
                    if (guestLectureDateNotNull.time + guestLectureDurationInMillis > System.currentTimeMillis()) {
                        activeGuestLectureList.add(guestLecture)
                    }
                }
            }
        }
        return activeGuestLectureList
    }

    private fun showGuestLectureListEmptyView(isDataEmpty: Boolean) {
        if (isDataEmpty) {
            binding.llGuestLectureNonEmptyView.visibility = View.GONE
            binding.guestLectureEmptyView.root.visibility = View.VISIBLE
            binding.guestLectureEmptyView.actvHeading.text = getString(R.string.this_is_your_space)
            binding.guestLectureEmptyView.actvDescription.text = getString(R.string.get_started_by_adding_guest_lecture)
            binding.guestLectureEmptyView.actvAddCourse.text = getString(R.string.add_guest_lecture_all_caps)
            binding.guestLectureEmptyView.llAddCourse.setOnClickListener {
                launchAddGuestLectureActivity()
            }
        } else {
            binding.llGuestLectureNonEmptyView.visibility = View.VISIBLE
            binding.guestLectureEmptyView.root.visibility = View.GONE
        }
    }

    private fun launchAddGuestLectureActivity() {
        startActivity(Intent(requireContext(), AddGuestLectureActivity::class.java))
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(requireActivity())
            builder.apply {
                setPositiveButton(R.string.ok) { dialog, id ->
                    setCancelable(true)
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun getActiveGuestLectures() {
        viewModel.fetchGuestLectureList()
    }

    override fun onItemClick(guestLectureData: GuestLectureData) {
        val bundle = Bundle()
        bundle.putSerializable("guestLectureData", guestLectureData)
        val intent = Intent(requireContext(), GuestLectureDetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onEditClick(guestLectureData: GuestLectureData) {
        if (shouldEnableDeleteOrEdit(guestLectureData)) {
            showErrorDialog(getString(R.string.can_not_edit_guest_lecture))
        } else {
            val bundle = Bundle()
            bundle.putSerializable("guestLectureData", guestLectureData)
            val intent = Intent(requireContext(), AddGuestLectureActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun shouldEnableDeleteOrEdit(guestLectureData: GuestLectureData) = guestLectureData.status == APPROVED && guestLectureData.noOfAttendees != 0

    override fun onDeleteClick(guestLectureData: GuestLectureData) {
        if (shouldEnableDeleteOrEdit(guestLectureData)) {
            showErrorDialog(getString(R.string.delete_webinar))
        } else {
            showDeleteDialog(guestLectureData.title!!, guestLectureData.id!!)
        }
    }

    private fun showDeleteDialog(message: String, guestLectureDeleteId: Int) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(requireActivity())
            builder.apply {
                setTitle("Are You Sure?")
                setPositiveButton(R.string.webinar_delete, DialogInterface.OnClickListener { dialog, id ->
                    setCancelable(true)
                    viewModel.deleteGuestLecture(guestLectureDeleteId.toString())
                })
                setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->
                    setCancelable(true)
                })
                setMessage("You Want to delete the " + message + "!")
            }
            builder.create()
        }
        alertDialog.show()
    }

    override fun onShareClick(guestLectureCode: String) {
        shareIntent(shareText = BuildConfig.SHARE_URL + DEMO_LECTURES_DETAILS + guestLectureCode, context = requireContext())
    }

    override fun onStudentClick(guestLectureId: Int) {
        startActivity(Intent(requireContext(), DemoWebinarStudentActivity::class.java).apply {
            putExtra(PARAM_DEMO_WEBINAR_ID, guestLectureId.toString())
            putExtra(PARAM_IS_FROM, PARAM_IS_FROM_DEMO)
        })
    }

    override fun onStudentViewClick(guestLectureId: Int) {
        GuestLectureStudentViewActivity.launchActivity(requireActivity(), guestLectureId)
    }
}