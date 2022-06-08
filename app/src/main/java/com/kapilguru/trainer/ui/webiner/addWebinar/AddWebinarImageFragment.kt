package com.kapilguru.trainer.ui.webiner.addWebinar

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
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.Companion.fetchImage
import com.kapilguru.trainer.Companion.loadGlideImage
import com.kapilguru.trainer.databinding.FragmentAddWebinarImageBinding
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.webiner.addWebinar.viewModel.AddWebinarViewModel
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

class AddWebinarImageFragment : Fragment(), ChoosePictureDialogInteractor {
    private val TAG = "AddWebinarImageFragment"
    lateinit var binding: FragmentAddWebinarImageBinding
    val viewModel: AddWebinarViewModel by activityViewModels()
    var appUri: Uri? = null
    var imageFile: File? = null
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
                .setRequestedSize(ADD_WEBINAR_WINDOW_X, ADD_WEBINAR_WINDOW_Y)
                .setAutoZoomEnabled(false)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_webinar_image, container, false)
        binding.viewModel = viewModel
        binding.clickHandlers = this
        binding.lifecycleOwner = this.requireActivity()
        registerForCropResultLauncher()
        observeWebinarResponse()
        return binding.root
    }

    private fun registerForCropResultLauncher() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                binding.aCImgVOriginalImage.visibility = View.VISIBLE
                viewModel.setIsNewImageSelected(true)
                binding.aCImgVOriginalImage.setImageURI(uri)
                imageFile = File(uri.path!!)
            }
        }
    }

    private fun observeWebinarResponse() {
        viewModel.webinarResponse.observe(viewLifecycleOwner, Observer { apiResponse ->
            Log.d(TAG, "observeWebinarResponse: ")
            when (apiResponse.status) {
                Status.SUCCESS -> {
                    apiResponse.data?.webinarData?.let { it ->
                        val webinarData = it[0]
                        val imageCode = webinarData.image
                        imageCode?.let {
                            setImage(it)
                        }
                    }
                }
            }
        })
    }

    private fun setImage(imageCode: String) {
        binding.aCImgVOriginalImage.visibility = View.VISIBLE
        binding.aCImgVNoImageIcon.visibility = View.GONE
        binding.aCImgVOriginalImage.fetchImage(imageCode)
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
        return getImageAsBase64(imageFile)
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
        imageFile?.let { file ->
            appUri = FileProvider.getUriForFile(this.requireActivity(), "com.kapilguru.trainer.fileprovider", file)
        }
    }
}