package com.kapilguru.trainer.trainerGallery

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityTrainerAllGalleryPicksBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import java.io.File
import androidx.recyclerview.widget.GridLayoutManager


class TrainerAllGalleryPicksActivity : BaseActivity(), ChoosePictureDialogInteractor, TrainerAllGalleryPicksReyclerAdapter.ItemClickListener,
    TwoButtonDialogInteractor{

    private var deletImageName: String?=null
    lateinit var binding: ActivityTrainerAllGalleryPicksBinding
    lateinit var viewModel: TrainerAllGalleyPicksViewModel
    var imageFile: File? = null
    lateinit var appUri: Uri
    lateinit var adapter:TrainerAllGalleryPicksReyclerAdapter
    lateinit var dialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_trainer_all_gallery_picks)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, TrainerAllGalleryPicksViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(TrainerAllGalleyPicksViewModel::class.java)
        dialog = CustomProgressDialog(this)
        setCustomActionBar()
        setclickListeners()
        setAllgalleryImagesAdapter()
        observeViewModels()
    }

    fun setCustomActionBar() {
        this.setActionbarBackListener(this, binding.customActionBar, getString(R.string.upload_image))
    }

    private fun setclickListeners() {
        binding.buttonAddCourse.setOnClickListener {
            launchBottomSheet()
        }
        binding.uploadImage.setOnClickListener {
            uploadGalleryImage()
        }
    }

    private fun setAllgalleryImagesAdapter() {
        binding.galleryAllImagesRecy.layoutManager = GridLayoutManager(this, 2)
        viewModel.getAllImagesList()
        adapter = TrainerAllGalleryPicksReyclerAdapter(this)
        binding.galleryAllImagesRecy.adapter = adapter
    }

    private fun observeViewModels() {
        viewModel.getAllImages.observe(this, Observer {allImagesResponse->
            when (allImagesResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                   allImagesResponse.data?.trainerGalleryImagesResponseApi?.let {
                       if(it.isNotEmpty()) {
                           setDataToAdapter(it)
                       }
                   }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }

        })


        viewModel.commonUploadImageResponse.observe(this, Observer {commonUploadImageResponse->
            when (commonUploadImageResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    commonUploadImageResponse.data?.status?.let {
                        if(it == 200) {
                            showUpdatedMessage()
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.deleteImageResponse.observe(this, Observer {deleteImageResponse->
            when (deleteImageResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    deleteImageResponse.data?.status?.let {
                        if(it == 200) {
                            showDeletedMessage()
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun showUpdatedMessage() {
        viewModel.getAllImagesList()
        showAppToast(this,"Image Uploaded Success fully");
    }

    private fun showDeletedMessage() {
        viewModel.getAllImagesList()
        showAppToast(this,"Image Deleted Successfully");
    }

    private fun setDataToAdapter(list: List<TrainerGalleryImagesResponseApi>) {
        adapter.setData(list as ArrayList<TrainerGalleryImagesResponseApi>)
    }

    private fun uploadGalleryImage() {
        viewModel.uploadGalleryImage(
            UploadImageGallery(
                tenantId = StorePreferences(this).tenantId,
                sourceId = StorePreferences(this).userId,
                baseImage = getImageAsBase64()!!,
            )
        )
    }

    private fun launchBottomSheet() {
        val data = ChoosePictureDialog(this)
        this.supportFragmentManager.let {
            data.show(it, "100")
        }
    }

    override fun didClickOnCamera(isCameraClicked: Boolean) {
        setImagePath()
        when (isCameraClicked) {
            true -> {
                captureImageRequest.launch(appUri)
            }
            false -> {
                pickImageRequest.launch("image/*")
            }
        }
    }

    private fun setImagePath() {
        imageFile = createImageFile(this)
        setUri()
    }

    private fun setUri() {
        appUri = FileProvider.getUriForFile(this, "com.kapilguru.trainer.fileprovider", imageFile!!)
    }


    private val captureImageRequest = registerForActivityResult(ActivityResultContracts.TakePicture()) {it->
        if (it) {
            binding.newUploadedImagePreview.setImageURI(appUri)
            uploadGalleryImage()
        }
    }

    private val pickImageRequest = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.newUploadedImagePreview.setImageURI(appUri)
//        cropActivityResultLauncher.launch(null)
    }

    fun getImageAsBase64(): String? {
        return if (this::appUri.isInitialized) {
            getImageAsBase64(imageFile)
        } else {
            null
        }
    }

    override fun onDeleteItemClick(trainerGalleryImagesResponseApi: TrainerGalleryImagesResponseApi) {
        trainerGalleryImagesResponseApi.name?.let {name ->
            deletImageName = name
            showTwoButtonDialog(name)

        }
    }

    private fun showTwoButtonDialog(courseTitle: String) {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: TwoButtonDialog = TwoButtonDialog.newInstance(
            String.format(getString(R.string.you_want_to_delete_image)),this)
        editNameDialogFragment.show(fm, "two_button_dialog")
    }

    override fun onNegativeClick() {

    }

    override fun onPositiveClick() {
        deletImageName?.let {name->
            viewModel.deleteImage(BuildConfig.PACKAGE_ID, name)
        }
    }

}