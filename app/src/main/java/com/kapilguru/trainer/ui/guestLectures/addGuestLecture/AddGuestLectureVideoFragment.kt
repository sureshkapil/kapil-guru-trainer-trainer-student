package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentAddGuestLectureVideoBinding
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureRequest
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.viewModel.AddGuestLectureViewModel
import kotlinx.android.synthetic.main.fragment_add_course_demo_video.*
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddGuestLectureVideoFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    val viewModel : AddGuestLectureViewModel by activityViewModels()
    lateinit var binding : FragmentAddGuestLectureVideoBinding
    var path: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_guest_lecture_video,container,false)

        binding.viewModel = viewModel
        binding.clickHandler = this
        binding.lifecycleOwner = this
        return binding.root
    }


    // when only on this fragment is visible then observe and show UI
    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if(menuVisible && viewModel.getIsLaunchedForEdit()) {
            fetchVideoInfo(viewModel.addGuestLectureRequest)
        } else {
            showVideoView(false)
        }

    }

    private fun fetchVideoInfo(addGuestLectureRequest: AddGuestLectureRequest) {
        val videoCode = addGuestLectureRequest.lectureVideo
        videoCode?.let {
            val uri = Uri.parse(VIDEO_BASE_URL + videoCode)
            showVideo(uri)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddGuestLectureVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
//                viewModel.isVideoUpdated = true
            } else {
                path =null // reset the path again
                showSingleButtonErrorDialog(requireContext(),"Please Upload a video of Size less than 150 MB")
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