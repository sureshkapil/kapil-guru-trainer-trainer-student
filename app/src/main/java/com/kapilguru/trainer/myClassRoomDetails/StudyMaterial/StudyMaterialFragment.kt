package com.kapilguru.trainer.myClassRoomDetails.studymaterial

import android.R.attr
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentStudyMaterial2Binding
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqViewModel
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentData
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentsRequest
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.android.synthetic.main.countryname_spinner_item.view.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.provider.Browser

import android.R.attr.password
import android.app.DownloadManager
import com.kapilguru.trainer.network.RetrofitNetwork
import android.content.Context.DOWNLOAD_SERVICE
import android.os.Environment
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService




private var downloadManager: DownloadManager? = null
private var downLoadId: Long = 0

class StudyMaterialFragment : Fragment(), PdfDocumentAdapter.DocumentClickListerner {

    lateinit var binding: FragmentStudyMaterial2Binding
    val viewModel: BatchCompletionReqViewModel by activityViewModels()
    lateinit var dialog: CustomProgressDialog

    var BUFFER_SIZE = 1024 * 2
    var pdfFileName: String? = null
    var downloadPdfUrl: String? = null
    lateinit var appUri: Uri
    var filepath: File? = null
    private val pickPdf = registerForActivityResult(ActivityResultContracts.GetContent()) { it ->
        it?.let {uri ->
            appUri = it
            pdfFileName = getFileName(context = requireContext(), uri = it)
            pdfFileName?.let { name ->
                binding.uploadedFileNameText.text = name.split(".")[0]
                filepath = fetchFilePath(context = requireContext(), binding.uploadedFileNameText.text.toString())
                copy(requireContext(), it, filepath)
                viewModel.isPdfUploaded = true
            } ?: run {
                Toast.makeText(requireContext(), "uploaded pdf is corrupted,Please try again", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyMaterial2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(requireContext())

        initializeDownloadManager()
        getBatchDocumentPdfList()
        uploadPdfListener()
        viewModelObserver()
    }

    private fun getBatchDocumentPdfList() {
        val trainerId = StorePreferences(requireContext()).trainerId.toString()
        viewModel.getBatchPdfList(BatchDocumentsRequest(viewModel.batchId.value?.toInt(),trainerId))
    }

    private fun viewModelObserver() {
        viewModel.pdfName.observe(viewLifecycleOwner, Observer {
            binding.uploadedFileNameText.text = it
        })
        viewModel.downloadPdfUrl.observe(viewLifecycleOwner, Observer {
            downloadPdfUrl = it
        })

        viewModel.uploadPdfResponse.observe(viewLifecycleOwner, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    binding.batchPDFTitle.text?.clear()
                    binding.uploadedFileNameText.text = getString(R.string.upload_document)
                    showAppToast(requireContext(), apiResponse.data?.message.toString())
                    getBatchDocumentPdfList()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }

            }
        })

        viewModel.batchDocumentResponse.observe(viewLifecycleOwner, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val pdfAdapter = PdfDocumentAdapter(mListener = this)
                    apiResponse.data?.data?.let {
                        pdfAdapter.setData(it)
                        binding.rvPdfDocuments.adapter = pdfAdapter
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }

            }
        })
    }

    private fun uploadPdfListener() {
        binding.cardUpload.setOnClickListener {
            pickPdf.launch("application/pdf")
        }

        binding.btnUploadDocument.setOnClickListener {
            validateApiRequest()
        }
    }

    private fun validateApiRequest() {
        val title = binding.batchPDFTitle.text.toString().trim()
            if (title.isEmpty()){
                showAppToast(requireContext(), "Please enter course title")
            }else if (viewModel.isPdfUploaded) {
                val batch = getBase64File()
               batch.first?.let { it ->
                    viewModel.pdfName.value = batch.second!!
                    uploadPdfToApi(it, title)
                } ?: kotlin.run {
                    showAppToast(requireContext(), "Please choose Pdf")
                }
            }

    }

    private fun uploadPdfToApi(it: File, pdfName: String) {
        val pref = StorePreferences(requireContext())
        viewModel.uploadBatchPDF(
            file = it,
            trainerId = pref.trainerId.toString(),
            pdfName = pdfName,
            courseId = viewModel.courseId.value.toString(),
            batchId = viewModel.batchId.value.toString())
    }

    fun getBase64File(): Pair<File?, String?> = Pair(first = filepath, second = binding.uploadedFileNameText.text.toString())


    fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copystream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(java.lang.Exception::class, IOException::class)
    fun copystream(input: InputStream?, output: OutputStream?): Int {
        val buffer = ByteArray(BUFFER_SIZE)
        val inFile = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (inFile.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
                Log.e(e.message, e.toString())
            }
            try {
                inFile.close()
            } catch (e: IOException) {
                Log.e(e.message, e.toString())
            }
        }
        return count
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = StudyMaterialFragment()
    }


    private fun initializeDownloadManager() {
        downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
    }

    private fun downloadFile(url: String, fileName: String) {
        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        val token = StorePreferences(requireContext()).trainerToken
        request.addRequestHeader("Authorization", token)
        request.setTitle(fileName).setDescription("File is downloading...").setDestinationInExternalFilesDir(
            requireContext(), Environment.DIRECTORY_DOWNLOADS, "Kapil Guru Study Material"
        ).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        //Enqueue the download.The download will start automatically once the download manager is ready
        // to execute it and connectivity is available.
        downLoadId = downloadManager!!.enqueue(request)
    }


    override fun onDocumentClicked(fileName: String) {
        val downloadUrl = BuildConfig.BASE_URL+"batchDocuments/files/"+fileName
        downloadFile(downloadUrl, fileName)
    }
}