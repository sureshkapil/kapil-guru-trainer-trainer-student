package com.kapilguru.trainer.ui.webiner

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
import com.kapilguru.trainer.databinding.FragmentActiveWebinarsBinding
import com.kapilguru.trainer.demo_webinar_students.DemoWebinarStudentActivity
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.webiner.addWebinar.AddWebinarActivity
import com.kapilguru.trainer.ui.webiner.model.ActiveWebinarData
import com.kapilguru.trainer.ui.webiner.viewModel.WebinarViewModel
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.WebinarDetailsActivity
import com.kapilguru.trainer.ui.webiner.webinarStudentView.WebinarStudentViewActivity
import java.text.SimpleDateFormat

class ActiveWebinarsFragment : Fragment(), WebinarAdapter.OnItemClickWebinar {
    private val TAG = "ActiveWebinarsFragment"
    lateinit var binding: FragmentActiveWebinarsBinding
    val viewModel: WebinarViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: WebinarAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveWebinarsBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setClickListeners()
        observeViewModelData()
        viewModel.fetchWebinarList()
    }

    private fun setAdapter() {
        adapter = WebinarAdapter(this as WebinarAdapter.OnItemClickWebinar)
        binding.webinarRecyclerView.adapter = adapter
    }

    private fun setClickListeners() {
        binding.addWebinarButton.setOnClickListener {
            launchAddWebinarActivity()
        }
    }

    private fun launchAddWebinarActivity() {
        val intent = Intent(requireContext(), AddWebinarActivity::class.java)
        intent.putExtra(WEBINAR_EDIT_KEY_PARAM, 1)
        startActivity(intent)
    }

    private fun observeViewModelData() {
        observeWebinarListApiResponse()
    }

    private fun observeWebinarListApiResponse() {
        viewModel.webinarListApi.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { webinarListApi ->
                        checkAndSetAdapterData(webinarListApi.webinarData)
                    }
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun checkAndSetAdapterData(webinarList: ArrayList<ActiveWebinarData>) {
        val activeWebinarList = getActiveWebinarList(webinarList)
        if (activeWebinarList.isEmpty()) {
            showWebinarListEmptyView(true)
        } else {
            showWebinarListEmptyView(false)
            adapter.setWebinarList(activeWebinarList)
            viewModel.totalWebinars.value = activeWebinarList.size.toString()
        }
    }

    private fun getActiveWebinarList(webinarList: ArrayList<ActiveWebinarData>): ArrayList<ActiveWebinarData> {
        val activeWebinarList = ArrayList<ActiveWebinarData>()
        for (webinar in webinarList) {
            val endDate = webinar.endDate
            endDate?.let { endDateNotNull ->
                val simpleDateFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
                val webinarEndDate = simpleDateFormat.parse(endDateNotNull)
                webinarEndDate?.let { webinarEndDateNotNull ->
                    if (webinarEndDateNotNull.time > System.currentTimeMillis()) {
                        activeWebinarList.add(webinar)
                    }
                }
            }
        }
        return activeWebinarList
    }

    private fun showWebinarListEmptyView(isDataEmpty: Boolean) {
        if (isDataEmpty) {
            binding.webinarNonEmptyView.visibility = View.GONE
            binding.webinarEmptyView.root.visibility = View.VISIBLE
            binding.webinarEmptyView.actvHeading.text = getString(R.string.this_is_your_space)
            binding.webinarEmptyView.actvDescription.text = getString(R.string.get_started_by_adding_webinar)
            binding.webinarEmptyView.actvAddCourse.text = getString(R.string.add_webinar_all_caps)
            binding.webinarEmptyView.llAddCourse.setOnClickListener {
                launchAddWebinarActivity()
            }
        } else {
            binding.webinarNonEmptyView.visibility = View.VISIBLE
            binding.webinarEmptyView.root.visibility = View.GONE

        }
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

    override fun onItemClick(webinarData: ActiveWebinarData) {
        val intent = Intent(requireContext(), WebinarDetailsActivity::class.java)
        intent.putExtra("webinarData", webinarData.id.toString())
        startActivity(intent)
    }

    override fun onEditCLick(webinarData: ActiveWebinarData, webinarId: Int) {
        if (shouldEnableDeleteOrEdit(webinarData)) {
            showErrorDialog(getString(R.string.edit_webinar))
        } else {
            val intent = Intent(requireContext(), AddWebinarActivity::class.java)
            intent.putExtra(WEBINAR_EDIT_KEY_PARAM, 2)
            intent.putExtra(WEBINAR_EDIT_ID_PARAM, webinarId)
            startActivity(intent)
        }
    }

    private fun shouldEnableDeleteOrEdit(webinarData: ActiveWebinarData) = webinarData.status == "Approved" && webinarData.noOfAttendees != 0

    override fun onDeleteClick(webinarId: Int, webinarTitle: String, webinarStatus: String, webinarData: ActiveWebinarData) {
        if (shouldEnableDeleteOrEdit(webinarData)) {
            showErrorDialog(getString(R.string.delete_webinar))
        } else {
            showDeleteDialog(webinarTitle, webinarId)
        }
    }

    private fun showDeleteDialog(message: String, webinarId: Int) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(requireActivity())
            builder.apply {
                setTitle("Are You Sure?")
                setPositiveButton(R.string.webinar_delete) { dialog, id ->
                    setCancelable(true)
                    viewModel.webinarId.value = webinarId.toString()
                    observerDeleteData()
                }
                setNegativeButton(R.string.no) { dialog, id ->
                    setCancelable(true)
                }
                setMessage("You Want to delete the " + message + "!")
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun observerDeleteData() {
        viewModel.webinarDeleteApi.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { webinarDeleteApi ->
                        webinarDeleteApi.commonResponse.let { deleteWebinarData ->
                            viewModel.fetchWebinarList()
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }


    override fun onShareClick(webinarCode: String) {
        shareIntent(shareText = BuildConfig.SHARE_URL + WEBINAR_DETAILS + webinarCode, context = requireContext())
    }

    override fun onStudentClick(webinarId: Int, title: String) {
        startActivity(Intent(requireContext(), DemoWebinarStudentActivity::class.java).apply {
            putExtra(PARAM_DEMO_WEBINAR_ID, webinarId.toString())
            putExtra(PARAM_DEMO_WEBINAR_TITILE, title)
            putExtra(PARAM_IS_FROM, PARAM_IS_FROM_WEBINAR)
        })
    }

    override fun onStudentViewClicked(webinarId: Int) {
        WebinarStudentViewActivity.launchActivity(webinarId,requireActivity())
    }
}