package com.kapilguru.trainer.ui.courses.addcourse

import android.content.Context
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentAddCourseDemoVideoBinding
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModel
import kotlinx.android.synthetic.main.fragment_add_course_demo_video.*
import kotlinx.android.synthetic.main.fragment_add_course_lecture_syllabus.*
import java.io.*
import java.util.*


class AddCourseDemoVideoFragment : Fragment() {

    val viewModel: AddCourseViewModel by activityViewModels()
    lateinit var viewBinding: FragmentAddCourseDemoVideoBinding
    private val IMAGE_DIRECTORY = "/demonuts_upload_gallery"
    var path: File? = null

    var BUFFER_SIZE = 1024 * 2
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_course_demo_video, container, false)
        viewBinding.viewModel = viewModel
        viewBinding.clickHandler = this
        viewBinding.lifecycleOwner = this
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForEditAndFetchVideo()
    }
    private fun checkForEditAndFetchVideo() {
        if (viewModel.isEdit) {
            viewModel.addCourseRequest.courseVideo?.let {courseVideoUrl->
                val uri = Uri.parse(VIDEO_BASE_URL +courseVideoUrl)
                showVideo(uri)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = AddCourseDemoVideoFragment()
    }


    fun onClick(view: View) {
        getVideoURi.launch("video/*")
    }

    private val getVideoURi = registerForActivityResult(ActivityResultContracts.GetContent()) {uri->
      path = fetchVideoFilePath(context = requireContext(), "abc")
        copy(requireContext(), uri ,path)
        path?.let{it->
            if(calculateFileSize(it, MAX_VIDEO_FILE_SIZE)){
                showVideo(uri)
                viewModel.isVideoUpdated = true
            } else {
                path =null // reset the path again
                showSingleButtonErrorDialog(requireContext(),"Please Upload a video of Size less than 150 MB")
            }
        }

    }

    fun fetchVideoFilePath(context: Context, pdfName: String): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File.createTempFile(
            pdfName, /* prefix */
            ".mp4", /* suffix */
            storageDir      /* directory */
        )
    }

    private fun showVideo(it: Uri?) {
        aCImgVNoVideoIcon.visibility = View.GONE
        aCImgVNoVideoIcon.isClickable = false
        videoView.visibility = View.VISIBLE
        videoView.setVideoURI(it)
//        showVideoBuffering()
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.seekTo(100)
//        videoView.start()
    }

    fun getFilePathFromURI(contentUri: Uri?): String? {
        //copy file and send new file path
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
//        val wallpaperDirectory = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        val copyFile = File(
            fetchVideoPath(requireContext()).toString()
        )
        // create folder if not exists

        return copyFile.absolutePath
    }

    fun getUploadVideoPath(): File? = path

    fun fetchVideoPath(context: Context): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File.createTempFile(
            "video", /* prefix */
            ".mp4", /* suffix */
            storageDir      /* directory */
        )
    }

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

    fun showVideoBuffering() {
        viewBinding.videoView.setOnPreparedListener(OnPreparedListener { mp ->
            mp.setOnBufferingUpdateListener { mp, percent ->
                if (percent == 100) {
                  showProgress(false)
                } else {
                    showProgress(true)
                }
            }
        })
    }

    private fun showProgress(isVisible: Boolean) {
        if(isVisible)
        viewBinding.progressBar.visibility = View.VISIBLE
        else
            viewBinding.progressBar.visibility = View.GONE
    }

}

///storage/emulated/0/Android/data/com.kapilguru.trainer/files/Documents/abc1870726931764298883.mp4
//content://com.android.providers.media.documents/document/video%3A106

///storage/emulated/0/Android/data/com.kapilguru.trainer/files/Documents/abc2000382572394086562.mp4