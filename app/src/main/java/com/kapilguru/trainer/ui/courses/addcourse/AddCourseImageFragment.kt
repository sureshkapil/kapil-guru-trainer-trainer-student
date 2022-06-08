package com.kapilguru.trainer.ui.courses.addcourse

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.MutableLiveData
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentAddCourseImageBinding
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModel
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

class AddCourseImageFragment : Fragment(), ChoosePictureDialogInteractor {
    private val TAG = "AddCourseImageFragment"
    private var param1: String? = null
    private var param2: String? = null
    lateinit var viewBinding: FragmentAddCourseImageBinding
    val viewModel: AddCourseViewModel by activityViewModels()
    lateinit var mContext: Context
    lateinit var appUri: Uri
    var imageFile: File? = null
    private lateinit var croppedImageUri : Uri
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
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setMaxCropResultSize(Add_COURSE_IMAGE_MAX, Add_COURSE_IMAGE_MAX)
                .setMinCropResultSize(ADD_COURSE_IMAGE_MIN, ADD_COURSE_IMAGE_MIN)
                .setRequestedSize(ADD_COURSE_WINDOW, ADD_COURSE_WINDOW)
                .setAutoZoomEnabled(false)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_course_image, container, false)
        viewBinding.viewModel = viewModel
        viewBinding.clickHandlers = this
        viewBinding.lifecycleOwner = this.requireActivity()
        checkForEditAndFetchImage()

        return viewBinding.root
    }

    private fun checkForEditAndFetchImage() {
        if (viewModel.isEdit) {
            viewBinding.aCImgVOriginalImage.visibility = View.VISIBLE
            viewBinding.aCImgVNoImageIcon.visibility = View.GONE
            val isImageEmpty: MutableLiveData<Boolean> = MutableLiveData()
            viewModel.addCourseRequest.courseImage?.let {imageurl->
                val isImageNull = imageurl.contains("null",true)
                viewModel.isEditImageNull.value = isImageNull
                Log.d(TAG, "checkForEditAndFetchImage: ${IMAGE_BASE_URL+imageurl}")
                if(!isImageNull) {
                    setGlideImageUrl(
                        viewBinding.aCImgVOriginalImage,
                        viewBinding.aCImgVNoImageIcon,
                        this.requireContext(),
                        IMAGE_BASE_URL+imageurl,
                        )
                }
            }?:run {
                viewModel.isEditImageNull.value = true
            }
        }
    }

    private fun setImagePath() {
        imageFile = createImageFile(requireContext())
        setUri()
    }

    private fun setUri() {
        appUri = FileProvider.getUriForFile(this.requireActivity(), "com.kapilguru.trainer.fileprovider", imageFile!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { it ->
            mContext = it
        }
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                croppedImageUri = uri
                viewModel.imageUpdated = true
                viewBinding.aCImgVOriginalImage.visibility = View.VISIBLE
                viewBinding.aCImgVOriginalImage.setImageURI(uri)
            }
        }
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

    fun getImageAsBase64(): String? {
        return if (this::croppedImageUri.isInitialized) {
            getImageAsBase64(File(croppedImageUri.path!!))
        } else {
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
}