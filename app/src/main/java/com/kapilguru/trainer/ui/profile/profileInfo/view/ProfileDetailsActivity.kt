package com.kapilguru.trainer.ui.profile.profileInfo.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityProfileDetailsBinding
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.profile.data.ProfileData
import com.kapilguru.trainer.ui.profile.profileInfo.models.*
import com.kapilguru.trainer.ui.profile.profileInfo.viewModel.ProfileInfoViewModelFactory
import com.kapilguru.trainer.ui.profile.profileInfo.viewModel.ProfileInfoViewmodel
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileDetailsActivity : BaseActivity(), ChoosePictureDialogInteractor {
    private val TAG = "ProfileDetailsActivity"
    lateinit var binding: ActivityProfileDetailsBinding
    lateinit var viewModel: ProfileInfoViewmodel
    lateinit var dialog: CustomProgressDialog
    private var profileData: ProfileData? = null

    var isBoldSelected: Boolean = false
    var isItalicSelected: Boolean = false
    var isUnderLineSelected: Boolean = false
    var isBulletsSelected: Boolean = false
    var isNumberSelected: Boolean = false

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_details)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, ProfileInfoViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(ProfileInfoViewmodel::class.java)
        dialog = CustomProgressDialog(this)
        binding.viewModel = viewModel
        binding.individual.clickHandlers = this
        setCustomActionBarListener()
        getProfileData()
        checkAndRemoveFocus()
        observeViewModelData()
        setClickListeners()
        setTitleTypeAdapter()
        setGenderAdapter()
        setCurrencyAdapter()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.profile))
    }

    private fun checkAndRemoveFocus() {
        if (!TextUtils.isEmpty(viewModel.profileMutLiveData.value?.contactNumber)) {
            binding.etMobileNo.isFocusable = false
            binding.etMail.isFocusable = false
        }
    }

    private fun observeViewModelData() {
        observeCountryList()
        observeStateList()
        observeCityList()

        observeOrgCountryList()
        observeOrgStateList()
        observeOrgCityList()
        observeProfileData()
        observeSaveProfileResponse()
        observeErrorDescription()
        observeUploadProfileImage()
    }

    private fun observeProfileData() {
        viewModel.profileDataResponse.observe(this, Observer {
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

    private fun observeUploadProfileImage() {
        viewModel.uploadImageCourseResponse.observe(this, Observer {
            System.out.println("Profile Response: " + Gson().toJson(it.data))
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this, it.data!!.message, Toast.LENGTH_SHORT).show()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeSaveProfileResponse() {
        viewModel.saveProfileResponse.observe(this, Observer {
            Log.d(TAG, "Profile Response: " + Gson().toJson(it.data))
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this, it.data!!.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeCountryList() {
        viewModel.countryList.observe(this, Observer {
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
        viewModel.stateList.observe(this, Observer {
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
        viewModel.cityList.observe(this, Observer {
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

    private fun observeOrgCountryList() {
        viewModel.orgCountryList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateOrgCountrySpinner(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeOrgStateList() {
        viewModel.orgStateList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateOrgStateSpinner(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeOrgCityList() {
        viewModel.orgCityList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateOrgCitySpinner(it)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeErrorDescription() {
        viewModel.errorDescription.observe(this, Observer {
            showToast(it)
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setClickListeners() {
        initActivityResultLaunchers()
//        setBusinessTypeRadioButtonsChangeListener()
        setGstRadioButtonsChangeListeners()
        binding.tvProfileUpdate.setOnClickListener { updateProfileData() }
        binding.cardProfileCapture.setOnClickListener {
            showTwoButtonsDialog()
        }
    }

    private fun initActivityResultLaunchers() {
        initProfileImageResultLauncher()
        initAadharResultLauncher()
        initPanResultLauncher()
        intiEstablishmentResultLauncher()
    }

    private fun initProfileImageResultLauncher() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                viewModel.imageUpdated = true
                binding.imgProfilePic.visibility = View.VISIBLE
                binding.imgProfilePic.setImageURI(uri)
                // update the profile pic
                try {
                    updateUploadedImageToApi(getImageAsBase64().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun initAadharResultLauncher() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                viewModel.imageUpdated = true
                binding.imgProfilePic.visibility = View.VISIBLE
                binding.imgProfilePic.setImageURI(uri)

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
                binding.imgProfilePic.visibility = View.VISIBLE
                binding.imgProfilePic.setImageURI(uri)

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
                binding.imgProfilePic.visibility = View.VISIBLE
                binding.imgProfilePic.setImageURI(uri)

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
        binding.gstRegisterYes.setOnCheckedChangeListener { buttonView, isChecked ->
            showOrHideGSTNumber(isChecked)
        }
        binding.gstRegisterNo.setOnCheckedChangeListener { buttonView, isChecked ->
            showOrHideGSTNumber(!isChecked)
        }
    }

    private fun setBusinessTypeRadioButtonsChangeListener() {
        binding.radioGroupBusiness.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.business_individual) {
                binding.organization.organization.visibility = View.GONE
            } else if (checkedId == R.id.business_organisation) {
                binding.organization.organization.visibility = View.VISIBLE
            }
        }
    }

    private fun setTitleTypeAdapter(){
        val titleTypeArray = resources.getStringArray(R.array.title_type_individual)
        binding.spinnerTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.title = titleTypeArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setGenderAdapter() {
      /*  val genderArray = resources.getStringArray(R.array.gender_type)
        binding.layoutIndividualDetails.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.gender = genderArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }*/
    }

    private fun setCurrencyAdapter() {
      /*  val currentArray = resources.getStringArray(R.array.curreny)
        binding.layoutIndividualDetails.spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.currency = currentArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }*/
    }

    private fun updateUploadedImageToApi(it: String) {
        val request = UploadImageCourse(code = profileData!!.code.toString(), baseImage = it, sourceType = "Profile", sourceId = profileData!!.userId!!)
        Log.d("Upload Request", Gson().toJson(request))
        viewModel.uploadProfileImage(request)
    }

    private fun showTwoButtonsDialog() {
        val data = ChoosePictureDialog(this)
        this.supportFragmentManager.let {
            data.show(it, "100")
        }
    }

    private fun encryptDescription() {
        var description: String? = getDataFromRichTextEditor()
        if (description != null) {
            description = String.convertStringToBase64(description)
            viewModel.profileMutLiveData.value!!.description = description
        }
    }

    fun populateViews() {
        checkAndSetAlternateNumber()
        setTitle()
        setGender()
        setCurrency()
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
     /*   val titleType = resources.getStringArray(R.array.title_type)
        for (title in titleType) {
            titleType[titleType.indexOf(title)] = title.toString().toLowerCase()
        }
        val index = titleType.indexOf(profileData?.gender?.toLowerCase())
        binding.layoutIndividualDetails.spinnerGender.setSelection(index)*/
    }

    private fun setGender() {
       /* val genderType = resources.getStringArray(R.array.gender_type)
        for (gender in genderType) {
            genderType[genderType.indexOf(gender)] = gender.toString().toLowerCase()
        }
        val index = genderType.indexOf(profileData?.gender?.toLowerCase())
        binding.layoutIndividualDetails.spinnerGender.setSelection(index)*/
    }

    private fun setCurrency() {
//        binding.layoutIndividualDetails.spinnerCurrency.setSelection(0)
    }

    // Profile Spinner binding
    private fun populateCountrySpinner(it: ApiResource<CountryResponce>) {
       /* val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryAdapter = ProfileCustomSpinnerAdapter(this, countryList as MutableList<CountryResponceItem>, false)
        binding.layoutIndividualDetails.spinnerCountry.adapter = countryAdapter
        binding.layoutIndividualDetails.spinnerCountry.isEnabled = false
        binding.layoutIndividualDetails.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                    binding.layoutIndividualDetails.spinnerCountry.setSelection(index)
                    break
                }
            }
        }*/
    }

    fun populateCountryCodeAdapter(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryCodeAdapter = ProfileCustomSpinnerAdapter(this, countryList as MutableList<CountryResponceItem>, true)
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
        val alternateCountryCodeAdapter = ProfileCustomSpinnerAdapter(this, countryList as MutableList<CountryResponceItem>, true)
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
      /*  val stateList = it.data?.stateList
        val stateAdapter: ArrayAdapter<StateResponceItem> = ArrayAdapter<StateResponceItem>(
            this, android.R.layout.simple_spinner_item, stateList as MutableList<StateResponceItem>
        )
        binding.layoutIndividualDetails.spinnerState.adapter = stateAdapter
        binding.layoutIndividualDetails.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                    binding.layoutIndividualDetails.spinnerState.setSelection(index)
                    break
                }
            }
        }*/
    }

    private fun populateCitySpinner(it: ApiResource<CityResponce>) {
        val cityList = it.data?.cityList
       /* val cityAdapter: ArrayAdapter<CityResponceItem> = ArrayAdapter<CityResponceItem>(
            this, android.R.layout.simple_spinner_item, cityList as MutableList<CityResponceItem>
        )
        // initializing spinner
        binding.layoutIndividualDetails.spinnerCity.adapter = cityAdapter

        // spinner selected listener
        binding.layoutIndividualDetails.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.cityId = cityList[position].id.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // binding data to spinner
        if (profileData != null) {
            val cityId = profileData!!.cityId
            for (city in cityList) {
                if (TextUtils.equals(cityId, city.id.toString())) {
                    val index = cityList.indexOf(city)
                    binding.layoutIndividualDetails.spinnerCity.setSelection(index)
                    break
                }
            }
        }*/
    }

    // Organization Spinner binding
    fun populateOrgCountrySpinner(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryAdapter = ProfileCustomSpinnerAdapter(this, countryList as MutableList<CountryResponceItem>, false)
        binding.organization.spinnerOrgCountry.adapter = countryAdapter

        binding.organization.spinnerOrgCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.orgCountryId = countryList[position].id.toString()
                viewModel.getOrgStateList(countryList[position].id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        if (profileData != null) {
            val orgCountryId = profileData!!.orgCountryId
            for (country in countryList) {
                if (TextUtils.equals(orgCountryId, country.id.toString())) {
                    val index = countryList.indexOf(country)
                    binding.organization.spinnerOrgCountry.setSelection(index)
                    break
                }
            }
        }
    }

    private fun populateOrgStateSpinner(it: ApiResource<StateResponce>) {
        val stateList = it.data?.stateList
        val stateAdapter: ArrayAdapter<StateResponceItem> = ArrayAdapter<StateResponceItem>(
            this, android.R.layout.simple_spinner_item, stateList as MutableList<StateResponceItem>
        )
        binding.organization.spinnerOrgState.adapter = stateAdapter


        binding.organization.spinnerOrgState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.orgStateId = stateList[position].id.toString()
                viewModel.getOrgCityList(stateList[position].id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        if (profileData != null) {
            val orgStateId = profileData!!.orgStateId
            for (state in stateList) {
                if (TextUtils.equals(orgStateId, state.id.toString())) {
                    val index = stateList.indexOf(state)
                    binding.organization.spinnerOrgState.setSelection(index)
                    break
                }
            }
        }
    }

    private fun populateOrgCitySpinner(it: ApiResource<CityResponce>) {
        val cityList = it.data?.cityList
        val cityAdapter: ArrayAdapter<CityResponceItem> = ArrayAdapter<CityResponceItem>(
            this, android.R.layout.simple_spinner_item, cityList as MutableList<CityResponceItem>
        )
        // initializing spinner
        binding.organization.spinnerOrgCity.adapter = cityAdapter


        // spinner selected listener
        binding.organization.spinnerOrgCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.profileMutLiveData.value?.orgCityId = cityList[position].id.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        // binding data to spinner
        if (profileData != null) {
            val orgCityId = profileData!!.orgCityId
            for (city in cityList) {
                if (TextUtils.equals(orgCityId, city.id.toString())) {
                    val index = cityList.indexOf(city)
                    binding.organization.spinnerOrgCity.setSelection(index)
                    break
                }
            }
        }
    }

    // Code from ProfileOrganization Activity start.
    fun showOrHideGSTNumber(shouldShow: Boolean) {
        if (shouldShow) {
            binding.llGstNo.visibility = View.VISIBLE
        } else {
            binding.llGstNo.visibility = View.GONE
        }
    }

    private fun updateProfileData() {
        if (viewModel.profileMutLiveData.value != null) {
            encryptDescription()
            when (viewModel.profileMutLiveData.value!!.isOrganization) {
                0 -> {
                    if (viewModel.dataValid(false)) viewModel.updateProfileData()
                }
                1 -> {
                    if (viewModel.dataValid(false) && viewModel.dataOrganizationValid()) viewModel.updateProfileData()
                }
            }
        } else {
            showToast("profile mutable live data is null")
        }
    }

    fun getProfileData() {
        val pref = StorePreferences(this)
        viewModel.getProfileData(pref.userId.toString())
    }

    private fun checkAndSetDescription() {
        viewModel.profileMutLiveData.value?.description.let {
            it.let {
                binding.individual.richEditor.html = it?.fromBase64()
            }
        }
    }

    fun getDataFromRichTextEditor(): String? {
        return binding.individual.richEditor.html
    }

    // Rich Text Editer
    fun onBoldIconTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setBold()
        if (isBoldSelected) {
            isBoldSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_in_active
                )
            )
//            richEditor.removeFormat()
        } else {
            isBoldSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onItalicTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setItalic()
        if (isItalicSelected) {
            isItalicSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_in_active
                )
            )
//            richEditor.removeFormat()
        } else {
            isItalicSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onUnderlineTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setUnderline()
        if (isUnderLineSelected) {
            isUnderLineSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_in_active
                )
            )
        } else {
            isUnderLineSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onBulletsTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setBullets()
        if (isBulletsSelected) {
            isBulletsSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_in_active
                )
            )
        } else {
            isBulletsSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_active
                )
            )
        }
    }

    fun onNumbersTouch(view: View) {

        val imageButton = view as ImageButton
        richEditor.setNumbers()
        if (isNumberSelected) {
            isNumberSelected = false
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_in_active
                )
            )
        } else {
            isNumberSelected = true
            imageButton.setColorFilter(
                ContextCompat.getColor(
                    view.context, R.color.rich_txt_editor_active
                )
            )
        }
    }
    // Code from ProfileOrganization Activity end.

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val KEY_PROFILE_DATA = "profile_data"
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

    private fun fetchImagePath(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun setImagePath() {
        imagePath = fetchImagePath()
//        setUri()
    }

    fun getImageAsBase64(): String? {
        return getImageAsBase64(imagePath)
    }

    private fun setUri() {
        appUri = FileProvider.getUriForFile(
            this, "com.kapilguru.trainer.fileprovider", imagePath!!
        )
    }
}