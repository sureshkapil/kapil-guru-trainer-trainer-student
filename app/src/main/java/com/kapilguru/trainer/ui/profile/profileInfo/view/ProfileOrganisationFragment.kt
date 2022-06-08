package com.kapilguru.trainer.ui.profile.profileInfo.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.FragmentProfileIndividualBinding
import com.kapilguru.trainer.databinding.FragmentProfileOptionsBinding
import com.kapilguru.trainer.databinding.FragmentProfileOrganisationBinding
import com.kapilguru.trainer.exams.createQuestion.TextFormat
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.profile.data.ProfileData
import com.kapilguru.trainer.ui.profile.profileInfo.models.*
import com.kapilguru.trainer.ui.profile.profileInfo.viewModel.ProfileInfoViewmodel
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileOrganisationFragment : Fragment(), ChoosePictureDialogInteractor {
    private val TAG = "ProfileOrganisationFragment"

    lateinit var binding: FragmentProfileOrganisationBinding
    val viewModel: ProfileInfoViewmodel by activityViewModels()
    lateinit var dialog: CustomProgressDialog
    private var profileData: ProfileData? = null
    lateinit var profileDescription: TextFormat
    private var isNotIndian = false


    var mCurrentPhotoPath: String? = null
    lateinit var appUri: Uri
    var imagePath: File? = null

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
            return CropImage.activity(appUri).setAspectRatio(1, 1).setFixAspectRatio(true).setMaxCropResultSize(Add_COURSE_IMAGE_MAX, Add_COURSE_IMAGE_MAX)
                .setMinCropResultSize(ADD_COURSE_IMAGE_MIN, ADD_COURSE_IMAGE_MIN).setRequestedSize(ADD_COURSE_WINDOW, ADD_COURSE_WINDOW).setAutoZoomEnabled(false).getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_organisation, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindingRelatedData()
        initLateInitVariables()
        observeViewModelData()
        getProfileData()
        setClickListeners()
    }

    private fun setBindingRelatedData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        addRichEdittext()
        setViewsForOrganization()
        showOrHideViews()
    }

    private fun addRichEdittext() {
        profileDescription = TextFormat(requireActivity(), binding.llProfileDescription)
        profileDescription.setHeader(getString(R.string.describe_about_yourself), R.color.white)
        profileDescription.binding.actvHeader.setTextColor(requireContext().getColor(R.color.purple))
    }

    private fun setViewsForOrganization(){
        binding.tilName.hint = getString(R.string.organization_name_man)
    }

    private fun showOrHideViews() {
        isNotIndian = StorePreferences(requireContext()).isOtherCountryCode == 1
        if (isNotIndian) {
            binding.layoutProfileKyc.root.visibility = View.GONE
            binding.layoutProfileGst.root.visibility = View.GONE
        } else {
            binding.layoutProfileKyc.root.visibility = View.VISIBLE
            binding.layoutProfileGst.root.visibility = View.VISIBLE
        }
    }

    private fun initLateInitVariables() {
        dialog = CustomProgressDialog(requireContext())
    }

    fun getProfileData() {
        val pref = StorePreferences(requireContext())
        viewModel.getProfileData(pref.trainerId.toString())
    }

    private fun observeViewModelData() {
        observeCountryList()
        observeStateList()
        observeCityList()
        observeProfileData()
        observeSaveProfileResponse()
        observeErrorDescription()
        observeUploadProfileImage()
    }

    private fun observeCountryList() {
        viewModel.countryList.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateCountrySpinner(it)
                    populateCountryCodeAdapter(it)
                    populateAlternateCountryCodeAdapter(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeStateList() {
        viewModel.stateList.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateStateSpinner(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeCityList() {
        viewModel.cityList.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateCitySpinner(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeProfileData() {
        viewModel.profileDataResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    viewModel.profileMutLiveData.value = it?.data?.data?.get(0)
                    profileData = viewModel.profileMutLiveData.value
                    viewModel.getCountryList()
                    viewModel.getOrgCountryList()
                    populateViews()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }

        })
    }

    private fun observeSaveProfileResponse() {
        viewModel.saveProfileResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    showAppToast(requireContext(), it.data!!.message)
                    activity?.finish()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    // Profile Spinner binding
    private fun populateCountrySpinner(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryAdapter = ProfileCustomSpinnerAdapter(requireContext(), countryList as MutableList<CountryResponceItem>, false)
        binding.layoutProfileLocation.spinnerCountry.adapter = countryAdapter
        binding.layoutProfileLocation.spinnerCountry.isEnabled = false
        binding.layoutProfileLocation.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.countryId = countryList[position].id.toString()
                viewModel.getStateList(countryList[position].id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        if (profileData != null) {
            val countryCode = profileData!!.countryCode
            for (country in countryList) {
                if (TextUtils.equals(countryCode, country.phoneCode.toString())) {
                    val index = countryList.indexOf(country)
                    binding.layoutProfileLocation.spinnerCountry.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateCountryCodeAdapter(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryCodeAdapter = ProfileCustomSpinnerAdapter(requireContext(), countryList as MutableList<CountryResponceItem>, true)
        binding.spinnerCountryCode.adapter = countryCodeAdapter
        binding.spinnerCountryCode.isEnabled = false
        binding.spinnerCountryCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.countryCode = countryList[position].phoneCode.toString()
                binding.spinnerAlternateCountryCode.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        if (profileData != null) {
            val countryCode = profileData!!.countryCode
            for (country in countryList) {
                if (TextUtils.equals(countryCode, country.phoneCode.toString())) {
                    val index = countryList.indexOf(country)
                    binding.spinnerCountryCode.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateAlternateCountryCodeAdapter(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val alternateCountryCodeAdapter = ProfileCustomSpinnerAdapter(requireContext(), countryList as MutableList<CountryResponceItem>, true)
        binding.spinnerAlternateCountryCode.isEnabled = false
        binding.spinnerAlternateCountryCode.adapter = alternateCountryCodeAdapter
        binding.spinnerAlternateCountryCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        if (profileData != null) {
            val countryCode = profileData!!.countryCode
            for (country in countryList) {
                if (TextUtils.equals(countryCode, country.phoneCode.toString())) {
                    val index = countryList.indexOf(country)
                    binding.spinnerAlternateCountryCode.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateStateSpinner(it: ApiResource<StateResponce>) {
        val stateList = it.data?.stateList
        val stateAdapter: ArrayAdapter<StateResponceItem> = ArrayAdapter<StateResponceItem>(requireContext(), android.R.layout.simple_spinner_item, stateList as MutableList<StateResponceItem>)
        binding.layoutProfileLocation.spinnerState.adapter = stateAdapter
        binding.layoutProfileLocation.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.stateId = stateList[position].id.toString()
                viewModel.getCityList(stateList[position].id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        if (profileData != null) {
            val stateId = profileData!!.stateId
            for (state in stateList) {
                if (TextUtils.equals(stateId, state.id.toString())) {
                    val index = stateList.indexOf(state)
                    binding.layoutProfileLocation.spinnerState.setSelection(index)
                    break
                }
            }
        }
    }

    private fun populateCitySpinner(it: ApiResource<CityResponce>) {
        val cityList = it.data?.cityList
        val cityAdapter: ArrayAdapter<CityResponceItem> = ArrayAdapter<CityResponceItem>(requireContext(), android.R.layout.simple_spinner_item, cityList as MutableList<CityResponceItem>)
        binding.layoutProfileLocation.spinnerCity.adapter = cityAdapter
        binding.layoutProfileLocation.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.cityId = cityList[position].id.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        if (profileData != null) {
            val cityId = profileData!!.cityId
            for (city in cityList) {
                if (TextUtils.equals(cityId, city.id.toString())) {
                    val index = cityList.indexOf(city)
                    binding.layoutProfileLocation.spinnerCity.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateViews() {
        checkAndSetAlternateNumber()
        setTitle()
        checkAndSetDescription()
    }

    private fun checkAndSetAlternateNumber() {
        if (TextUtils.isEmpty(profileData?.alternateNumber)) {
            profileData?.contactNumber?.let { contactNumberNotNull ->
                binding.etAlternateMobileNo.setText(contactNumberNotNull)
            }
        }
    }

    private fun setTitle() {
        val titleType = resources.getStringArray(R.array.title_type_individual)
        for (title in titleType) {
            titleType[titleType.indexOf(title)] = title.toString().toLowerCase()
        }
        val index = titleType.indexOf(profileData?.title?.toLowerCase())
        binding.spinnerTitle.setSelection(index)
    }

    private fun checkAndSetDescription() {
        viewModel.profileMutLiveData.value?.description.let {
            it.let {
                profileDescription.binding.richEditor.html = it?.fromBase64()
            }
        }
    }

    private fun observeErrorDescription() {
        viewModel.errorDescription.observe(viewLifecycleOwner, Observer {
            showAppToast(requireContext(), it)
        })
    }

    private fun observeUploadProfileImage() {
        viewModel.uploadImageCourseResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    showAppToast(requireContext(), it.data!!.message)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setClickListeners() {
        setSpinnerAdapters()
        setTitleTypeAdapter()
        initActivityResultLaunchers()
        setGstRadioButtonsChangeListeners()
        binding.tvProfileUpdate.setOnClickListener { updateProfileData() }
        binding.cvProfileImage.setOnClickListener {
            showTwoButtonsDialog()
        }
    }

    private fun setSpinnerAdapters() {
        setTitleTypeAdapter()
    }

    private fun setTitleTypeAdapter() {
        val titleTypeArray = resources.getStringArray(R.array.title_type_individual)
        binding.spinnerTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    viewModel.profileMutLiveData.value?.title = null
                } else {
                    viewModel.profileMutLiveData.value?.title = titleTypeArray[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initActivityResultLaunchers() {
        initProfileImageResultLauncher()
        initAadharResultLauncher()
        initPanResultLauncher()
        intiEstablishmentResultLauncher()
    }

    fun getImageAsBase64(): String? {
        return getImageAsBase64(imagePath)
    }

    private fun initProfileImageResultLauncher() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                viewModel.imageUpdated = true
                binding.ivProfile.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)
                // update the profile pic
                try {
                    updateUploadedImageToApi(getImageAsBase64().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun updateUploadedImageToApi(it: String) {
        val request = UploadImageCourse(code = profileData!!.code.toString(), baseImage = it, sourceType = "Profile", sourceId = profileData!!.userId!!)
        viewModel.uploadProfileImage(request)
    }

    private fun initAadharResultLauncher() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                viewModel.imageUpdated = true
                binding.ivProfile.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)

                // update the profile pic
                try {
                    updateUploadedImageToApi(getImageAsBase64().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initPanResultLauncher() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                viewModel.imageUpdated = true
                binding.ivProfile.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)

                // update the profile pic
                try {
                    updateUploadedImageToApi(getImageAsBase64().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun intiEstablishmentResultLauncher() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                viewModel.imageUpdated = true
                binding.ivProfile.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)

                // update the profile pic
                try {
                    updateUploadedImageToApi(getImageAsBase64().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setGstRadioButtonsChangeListeners() {
        binding.layoutProfileGst.gstRegisterYes.setOnCheckedChangeListener { buttonView, isChecked ->
            showOrHideGSTNumber(isChecked)
        }
        binding.layoutProfileGst.gstRegisterNo.setOnCheckedChangeListener { buttonView, isChecked ->
            showOrHideGSTNumber(!isChecked)
        }
    }

    fun showOrHideGSTNumber(shouldShow: Boolean) {
        if (shouldShow) {
            binding.layoutProfileGst.llGstNo.visibility = View.VISIBLE
        } else {
            binding.layoutProfileGst.llGstNo.visibility = View.GONE
        }
    }

    private fun updateProfileData() {
        if (viewModel.profileMutLiveData.value != null) {
            encryptDescription()
            setCreatedAndModifiedDateNull()
            when (viewModel.profileMutLiveData.value!!.isOrganization) {
                0 -> {
                    if (viewModel.dataValid()) viewModel.updateProfileData()
                }
                1 -> {
                    if (viewModel.dataValid() && viewModel.dataOrganizationValid()) viewModel.updateProfileData()
                }
            }
        }
    }
    
    private fun encryptDescription() {
        var description: String = getDataFromRichTextEditor()
        if (!TextUtils.isEmpty(description)) {
            description = String.convertStringToBase64(description)
            viewModel.profileMutLiveData.value!!.description = description
        } else {
            viewModel.profileMutLiveData.value!!.description = ""
        }
    }

    fun getDataFromRichTextEditor(): String {
        return profileDescription.getText()
    }

    /*Created date and modified date are values coming in getProfileData Api Response.
   These values are set to null before making api call as they are not required, and error is coming when sent in request*/
    private fun setCreatedAndModifiedDateNull() {
        viewModel.profileMutLiveData.value?.createdDate = null
        viewModel.profileMutLiveData.value?.modifiedDate = null
    }

    private fun showTwoButtonsDialog() {
        val data = ChoosePictureDialog(this)
        parentFragmentManager.let {
            data.show(it, "100")
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
        imagePath = fetchImagePath()
    }

    private fun fetchImagePath(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }
}