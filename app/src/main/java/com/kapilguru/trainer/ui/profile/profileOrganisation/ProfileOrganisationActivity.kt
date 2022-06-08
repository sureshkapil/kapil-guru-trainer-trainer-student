package com.kapilguru.trainer.ui.profile.profileOrganisation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityProfileOrganisationBinding
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.home.HomeActivity
import com.kapilguru.trainer.ui.profile.data.ProfileData
import com.kapilguru.trainer.ui.profile.profileInfo.models.*
import com.kapilguru.trainer.ui.profile.profileInfo.view.ProfileCustomSpinnerAdapter
import com.kapilguru.trainer.ui.profile.profileOrganisation.viewModel.ProfileOrgViewModel
import com.kapilguru.trainer.ui.profile.profileOrganisation.viewModel.ProfileOrgViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_course_titile_and_description.*

class ProfileOrganisationActivity : AppCompatActivity() {
    private val TAG = "ProfileOrgActivity"
    lateinit var viewModel: ProfileOrgViewModel
    lateinit var binding: ActivityProfileOrganisationBinding
    lateinit var dialog: CustomProgressDialog
    var isProfileUpdated: Boolean = false
    private var profileData: ProfileData? = null

    var isBoldSelected: Boolean = false
    var isItalicSelected: Boolean = false
    var isUnderLineSelected: Boolean = false
    var isBulletsSelected: Boolean = false
    var isNumberSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_organisation)
        viewModel = ViewModelProvider(
            this, ProfileOrgViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(ProfileOrgViewModel::class.java)
        binding.viewModel = viewModel
        binding.clickHandlers = this
        viewModel.profileMutLiveData.value = intent.extras?.getParcelable(KEY_PROFILE_DATA)
        Log.d(TAG, "onCreate : " + viewModel.profileMutLiveData.value.toString())
        dialog = CustomProgressDialog(this)
        setActivityLabel()
        checkAndShowOrgDetails()
        checkAndFetchCountryList()
        observeViewModelDate()
        setClickListeners()
        setAddressValue()
        checkAndSetDescription()
    }

    private fun setActivityLabel() {
        title = if (viewModel.getIsOrganisation()) {
            "Organisation Details"
        } else {
            "Individual Details"
        }
    }

    private fun checkAndShowOrgDetails() {
        showOrHideOrganisationDetails(viewModel.getIsOrganisation())
    }

    private fun checkAndFetchCountryList() {
        if (viewModel.getIsOrganisation()) {
            viewModel.getCountryList()
        }
    }

    private fun observeViewModelDate() {
        observeCountryist()
        observeStateList()
        observeCityList()
        observeSaveProfileResponce()
        observeErrorDescription()
    }

    private fun observeCountryist() {
        viewModel.countryList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateCountrySpinner(it)
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

    private fun observeSaveProfileResponce() {
        viewModel.saveProfileResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    navigateToProfileOptionsScreen()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun navigateToProfileOptionsScreen() {
        finishAffinity()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(HomeActivity.showProfile, true)
        startActivity(intent)
    }

    fun populateCountrySpinner(it: ApiResource<CountryResponce>) {
        val countryList: List<CountryResponceItem>? = it.data?.countryList
        val countryAdapter = ProfileCustomSpinnerAdapter(
            this, countryList as MutableList<CountryResponceItem>, false
        )
        binding.spinnerCountry.adapter = countryAdapter
        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.getStateList(countryList[position].id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        if (profileData != null) {
            var countryId = profileData!!.orgCountryId
            if (TextUtils.isEmpty(countryId)) return
            for (country in countryList) {
                if (TextUtils.equals(countryId, country.id.toString())) {
                    var index = countryList.indexOf(country)
                    binding.spinnerCountry.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateStateSpinner(it: ApiResource<StateResponce>) {
        val stateList = it.data?.stateList
        val stateAdapter: ArrayAdapter<StateResponceItem> = ArrayAdapter<StateResponceItem>(
            this, android.R.layout.simple_spinner_item, stateList as MutableList<StateResponceItem>
        )
        binding.spinnerState.adapter = stateAdapter
        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.getCityList(stateList[position].id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        if (profileData != null) {
            val stateId = profileData!!.orgStateId
            if (TextUtils.isEmpty(stateId)) return
            for (state in stateList) {
                if (TextUtils.equals(stateId, state.id.toString())) {
                    val index = stateList.indexOf(state)
                    binding.spinnerState.setSelection(index)
                    break
                }
            }
        }
    }

    fun populateCitySpinner(it: ApiResource<CityResponce>) {
        val cityList = it.data?.cityList
        val cityAdapter: ArrayAdapter<CityResponceItem> = ArrayAdapter<CityResponceItem>(
            this, android.R.layout.simple_spinner_item, cityList as MutableList<CityResponceItem>
        )
        binding.spinnerCity.adapter = cityAdapter
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        if (profileData != null) {
            val cityId = profileData!!.orgCityId
            if (TextUtils.isEmpty(cityId)) return
            for (city in cityList) {
                if (TextUtils.equals(cityId, city.id.toString())) {
                    val index = cityList.indexOf(city)
                    binding.spinnerCity.setSelection(index)
                    break
                }
            }
        }
    }

    private fun observeErrorDescription() {
        viewModel.errorDescription.observe(this, Observer {
            showToast(it)
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun setClickListeners() {
        binding.gstRegisterYes.setOnCheckedChangeListener { buttonView, isChecked ->
            showOrHideGSTNumber(isChecked)
        }
        binding.gstRegisterNo.setOnCheckedChangeListener { buttonView, isChecked ->
            showOrHideGSTNumber(!isChecked)
        }
        binding.tvSave.setOnClickListener {
            setData()
            if (viewModel.dataValid()) {
                saveProfileData()
            }
        }
    }

    fun showOrHideOrganisationDetails(shouldShow: Boolean) {
        if (shouldShow) {
            binding.llOrganisationDetails.visibility = View.VISIBLE
            binding.llGst.visibility = View.VISIBLE
        } else {
            binding.llOrganisationDetails.visibility = View.GONE
            binding.llGst.visibility = View.GONE
        }
    }

    fun showOrHideGSTNumber(shouldShow: Boolean) {
        if (shouldShow) {
            binding.llGstNo.visibility = View.VISIBLE
        } else {
            binding.llGstNo.visibility = View.GONE
        }
    }

    private fun setData() {
        viewModel.profileMutLiveData.value!!.orgAddressLine1 = binding.etAddress.text.toString()
        viewModel.profileMutLiveData.value!!.orgAddressLine2 = ""

        var description: String? = getDataFromRichTextEditor()
        if (description != null) {
            description = String.convertStringToBase64(description)
            viewModel.profileMutLiveData.value!!.description = description
        }
    }

    fun saveProfileData() {
        Log.d(TAG, "saveProfileData")
        if (viewModel.profileMutLiveData.value != null) {
            if (StorePreferences(this).isProfileUpdated == 1) {
                viewModel.updateProfileData()
            } else {
                viewModel.saveProfileData()
            }
        } else {
            Log.d(TAG, "profile mutable live data is null")
        }
    }

    private fun setAddressValue() {
        binding.etAddress.setText(viewModel.profileMutLiveData.value?.addressLine1 + viewModel.profileMutLiveData.value?.addressLine2)
    }

    private fun checkAndSetDescription() {
        viewModel.profileMutLiveData.value?.description.let {
            it.let {
                binding.richEditor.html = it?.fromBase64()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun getDataFromRichTextEditor(): String? {
        return binding.richEditor.html
    }

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

    companion object {
        const val KEY_PROFILE_DATA = "profile_data"
    }
}