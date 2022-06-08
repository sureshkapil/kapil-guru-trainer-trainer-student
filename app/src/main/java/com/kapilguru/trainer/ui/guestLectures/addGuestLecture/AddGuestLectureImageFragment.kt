package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.trainer.*
import com.kapilguru.trainer.Companion.fetchImage
import com.kapilguru.trainer.databinding.FragmentAddGuestLectureImageBinding
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.viewModel.AddGuestLectureViewModel
import com.theartofdev.edmodo.cropper.CropImage
import java.io.*

class AddGuestLectureImageFragment : Fragment(),ChoosePictureDialogInteractor {
    private val TAG = "AddGuestLectureImageFragment"
    lateinit var binding: FragmentAddGuestLectureImageBinding
    val viewModel: AddGuestLectureViewModel by activityViewModels()
    lateinit var appUri: Uri
    lateinit var imageFile: File
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private val imageRequest = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        cropActivityResultLauncher.launch(null)
    }
    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        appUri = it
        cropActivityResultLauncher.launch(null)
    }
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity(appUri)
                .setAspectRatio(2, 1)
                .setFixAspectRatio(true)
                .setMaxCropResultSize(Add_COURSE_IMAGE_MAX, Add_COURSE_IMAGE_MAX)
                .setMinCropResultSize(ADD_COURSE_IMAGE_MIN, ADD_COURSE_IMAGE_MIN)
                .setRequestedSize(ADD_GUEST_LECTURE_WINDOW_X, ADD_GUEST_LECTURE_WINDOW_Y)
                .setAutoZoomEnabled(false)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_guest_lecture_image, container, false)
        binding.viewModel = viewModel
        binding.clickHandlers = this
        binding.lifecycleOwner = this.requireActivity()
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                binding.aCImgVOriginalImage.visibility = View.VISIBLE
                viewModel.setIsImageChangedAfterUpload(true)
                binding.aCImgVOriginalImage.setImageURI(uri)
                imageFile = File(uri.path!!)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAndSetImage()
    }

    private fun checkAndSetImage() {
        if (viewModel.getIsLaunchedForEdit()) {
            val imageCode = viewModel.addGuestLectureRequest.image
            imageCode?.let {
                setImage(it)
            }
        } else {
            setOriginalImageVisibility(false)
        }
    }

    private fun setImage(imageCode: String) {
        setOriginalImageVisibility(true)
        binding.aCImgVOriginalImage.fetchImage(imageCode)
    }

    private fun setOriginalImageVisibility(shouldShowOriginalImage: Boolean) {
        binding.aCImgVOriginalImage.visibility = if(shouldShowOriginalImage) View.VISIBLE else View.GONE
        binding.aCImgVNoImageIcon.visibility = if(shouldShowOriginalImage) View.GONE else View.VISIBLE
    }

    fun onImageCardClick(view: View) {
        showTwoButtonsDialog()
    }

    private fun showTwoButtonsDialog() {
        val data = ChoosePictureDialog(this)
        activity?.supportFragmentManager?.let {
            data.show(it, "100")
        }
    }

    fun getImageAsBase64(): String ? {
        return if(this::imageFile.isInitialized){
            getImageAsBase64(imageFile)
        }else{
            null
        }
    }

    override fun didClickOnCamera(isCameraClicked: Boolean) {
        setImagePath()
        when (isCameraClicked) {
            true -> {
                imageRequest.launch(appUri)
            }
            false -> {
                pickImage.launch("image/*")
            }
        }
    }

    private fun setImagePath() {
        imageFile = createImageFile(requireContext())
        setUri()
    }

    private fun setUri() {
        appUri = FileProvider.getUriForFile(this.requireActivity(), "com.kapilguru.trainer.fileprovider", imageFile)
    }
}