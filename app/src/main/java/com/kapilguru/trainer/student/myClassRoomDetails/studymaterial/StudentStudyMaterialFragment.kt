package com.kapilguru.trainer.student.myClassRoomDetails.studymaterial

import android.app.DownloadManager
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.BuildConfig
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentStudentStudyMaterial2Binding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.myClassRoomDetails.model.StudentStudyMaterialResponseApi
import com.kapilguru.trainer.student.myClassRoomDetails.viewModel.StudentMyClassDetailsViewModel
import paytm.assist.easypay.easypay.appinvoke.BuildConfig.BASE_URL


class StudentStudyMaterialFragment : Fragment(), StudentStudyMaterialAdapter.StudyMaterialListener {

    lateinit var binding: FragmentStudentStudyMaterial2Binding
    val viewModel: StudentMyClassDetailsViewModel by activityViewModels<StudentMyClassDetailsViewModel>()
    lateinit var dialog: CustomProgressDialog
    lateinit var adapter: StudentStudyMaterialAdapter
    private var downloadManager: DownloadManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        requestObserveViewModelData()
    }

    private fun requestObserveViewModelData() {
        val batchId: String? = arguments?.getString(PARAM_BATCH_ID)
        batchId?.let { batchId ->
            viewModel.getStudyMaterial(batchId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentStudyMaterial2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StudentStudyMaterialAdapter(this)
        observeStudyMaterialResponse()
    }

    private fun observeStudyMaterialResponse() {
        viewModel.studentStudyMaterialResponse.observe(this.requireActivity(), Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.studentStudyMaterialResponseApi?.let { it ->
                        if (it.isNotEmpty()) {
                            viewModel.studentStudyMaterialResponseApi.value = it
                        }

                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(requireContext())
                        }
                    }
                }
            }
        })

        viewModel.studentStudyMaterialResponseApi.observe(this.requireActivity(), Observer {
            binding.recy.adapter = adapter
            adapter.setData(it)
        })


    }

    companion object {
        @JvmStatic
        fun newInstance(batchId: String?) = StudentStudyMaterialFragment().apply {
            arguments = Bundle().apply {
                putString(PARAM_BATCH_ID, batchId)
            }
        }
    }

    override fun onTap(studentStudyMaterialResponseApi: StudentStudyMaterialResponseApi) {  // ---need_to_do
       /* downloadManager = requireActivity().getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager?
        downloadManager?.let {
            getDownloadStatus(
                downloadManager(
                    context = requireContext(),
                    url = BuildConfig.BASE_URL + "batchDocuments/files/" + studentStudyMaterialResponseApi.filename,
                    fileName = studentStudyMaterialResponseApi.filename.toString(),
                    downloadManager = it
                )
            )
        }*/
    }

    private fun getDownloadStatus(downLoadId: Long) {
        val cursor: Cursor? = downloadManager!!.query(DownloadManager.Query().setFilterById(downLoadId))
        var statusString = ""
        if (cursor != null && cursor.moveToNext()) {
            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            cursor.close()
            when (status) {

                DownloadManager.STATUS_FAILED -> {
                    statusString = "Download Failed"

                }
                DownloadManager.STATUS_PENDING, DownloadManager.STATUS_PAUSED -> {
                    statusString = "Download Pending"
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    statusString = "Download Success"
                }
                DownloadManager.STATUS_RUNNING -> {
                    statusString = "Download Running"
                }
            }
        }

        showAppToast(requireContext(), statusString)
    }
}