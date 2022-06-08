package com.kapilguru.trainer.ui.guestLectures

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
import com.kapilguru.trainer.databinding.FragmentLiveUpcomingGuestLecturesBinding
import com.kapilguru.trainer.demo_webinar_students.DemoWebinarStudentActivity
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.guestLectures.guestLectureDetails.GuestLectureDetailsActivity
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.GuestLectureStudentViewActivity
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureData
import com.kapilguru.trainer.ui.guestLectures.viewModel.GuestLectureViewModel

class LiveUpComingGuestLecturesFragment : Fragment(), GuestLectureAdapter.OnItemClickGuestLecture {
    private val TAG = "LiveUpComingGuestLecturesFragment"
    lateinit var binding: FragmentLiveUpcomingGuestLecturesBinding
    val viewModel: GuestLectureViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: GuestLectureAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLiveUpcomingGuestLecturesBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeViewModelData()
        getLiveUpComingGuestLectures()
    }

    private fun setAdapter() {
        adapter = GuestLectureAdapter(true, this)
        binding.rvGuestLecture.adapter = adapter
    }

    private fun observeViewModelData() {
        observeLiveUpComingGuestLecturesApiResponse()
    }

    private fun observeLiveUpComingGuestLecturesApiResponse() {
        viewModel.liveUpComingGuestLectureResponseList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    it.data?.let { guestLectureListApi ->
                        setAdapterData(guestLectureListApi.data as ArrayList<GuestLectureData>)
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

    private fun setAdapterData(liveUpComingGuestLecture: ArrayList<GuestLectureData>) {
        adapter.setGuestLectureList(liveUpComingGuestLecture)
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

    private fun getLiveUpComingGuestLectures() {
        viewModel.fetchLiveUpComingGuestLectureList()
    }

    override fun onItemClick(guestLectureData: GuestLectureData) {
        val bundle = Bundle()
        bundle.putSerializable("guestLectureData", guestLectureData)
        val intent = Intent(requireContext(), GuestLectureDetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onEditClick(guestLectureData: GuestLectureData) {

    }

    override fun onDeleteClick(guestLectureData: GuestLectureData) {
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