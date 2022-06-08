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
import com.kapilguru.trainer.databinding.FragmentLiveWebinarsBinding
import com.kapilguru.trainer.demo_webinar_students.DemoWebinarStudentActivity
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.webiner.model.LiveUpComingWebinarData
import com.kapilguru.trainer.ui.webiner.viewModel.WebinarViewModel
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.WebinarDetailsActivity
import com.kapilguru.trainer.ui.webiner.webinarStudentView.WebinarStudentViewActivity

class LiveUpComingWebinarsFragment : Fragment(), LiveUpComingWebinarAdapter.OnLiveUpcomingWebinarClickListener {
    private val TAG = "LiveWebinarsFragment"
    lateinit var binding: FragmentLiveWebinarsBinding
    val viewModel: WebinarViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: LiveUpComingWebinarAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLiveWebinarsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeViewModelData()
        viewModel.fetchLiveUpComingWebinarList()
    }

    private fun setAdapter() {
        adapter = LiveUpComingWebinarAdapter(this)
        binding.webinarRecyclerView.adapter = adapter
    }

    private fun observeViewModelData() {
        observeLiveUpComingWebinarListApiResponse()
    }

    private fun observeLiveUpComingWebinarListApiResponse() {
        viewModel.liveUpComingWebinarListApi.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { webinarListApi ->
                        setAdapterData(webinarListApi.webinarData)
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

    private fun setAdapterData(liveUpComingWebinarList: ArrayList<LiveUpComingWebinarData>) {
        adapter.setWebinarList(liveUpComingWebinarList)
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

    override fun onItemClick(webinarData: LiveUpComingWebinarData) {
        val intent = Intent(requireContext(), WebinarDetailsActivity::class.java)
        intent.putExtra("webinarData", webinarData.webinarId.toString())
        startActivity(intent)
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
        WebinarStudentViewActivity.launchActivity(webinarId, requireActivity())
    }
}