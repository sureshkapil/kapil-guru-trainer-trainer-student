package com.kapilguru.trainer.ui.webiner.addWebinar

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentAddWebinarVideoBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.webiner.addWebinar.viewModel.AddWebinarViewModel
import kotlinx.android.synthetic.main.fragment_add_course_demo_video.*
import java.io.File

class AddWebinarVideoFragment : Fragment() {

    val viewModel : AddWebinarViewModel by activityViewModels()
    lateinit var binding : FragmentAddWebinarVideoBinding
    var path: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_webinar_video, container, false)

        binding.viewModel = viewModel
        binding.clickHandlers = this
        binding.lifecycleOwner = this
        return binding.root

    }


    // when only on this fragment is visible then observe and show UI
    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if(menuVisible) observeWebinarResponse() else showVideoView(false)

    }

    fun onClick(view: View) {
        getVideoURi.launch("video/*")
    }


    private fun observeWebinarResponse() {
        viewModel.webinarResponse.observe(viewLifecycleOwner, Observer { apiResponse ->
            when (apiResponse.status) {
                Status.SUCCESS -> {
                    apiResponse.data?.webinarData?.let { it ->
                        val webinarData = it[0]
                        val videoCode = webinarData.webinarVideo
                        videoCode?.let {
                            val uri = Uri.parse(VIDEO_BASE_URL + videoCode)
                            showVideo(uri)
                        }
                    }
                }
            }
        })
    }

    private val getVideoURi = registerForActivityResult(ActivityResultContracts.GetContent()) { uri->
        path = fetchVideoFilePath(context = requireContext(), "abc")
        copy(requireContext(), uri, path)
        path?.let{ it->
            if(calculateFileSize(it, MAX_VIDEO_FILE_SIZE)){
                showVideo(uri)
            } else {
                path =null // reset the path again
                showSingleButtonErrorDialog(requireContext(), "Please Upload a video of Size less than 150 MB")
            }
        }

    }

    fun getUploadVideoPath(): File? = path

    private fun showVideo(it: Uri?) {
        showVideoView(true)
        videoView.setVideoURI(it)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.seekTo(100)
    }

    private fun showVideoView(shouldShowVideo: Boolean) {
        aCImgVNoVideoIcon.visibility = if(shouldShowVideo) View.GONE else View.VISIBLE
        aCImgVNoVideoIcon.isClickable = false
        videoView.visibility = if(shouldShowVideo) View.VISIBLE else View.GONE
    }


}