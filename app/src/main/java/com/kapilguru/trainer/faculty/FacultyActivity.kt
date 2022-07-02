package com.kapilguru.trainer.faculty

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityFacultyBinding
import com.kapilguru.trainer.faculty.addFaculty.AddFacultyActivity
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences

class FacultyActivity : BaseActivity(), FacultyRecyAdapter.FacultyItemClick {

    lateinit var binding: ActivityFacultyBinding
    lateinit var viewModel: FacultyViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var adapter: FacultyRecyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_faculty)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faculty)
        viewModel = ViewModelProvider(this, FacultyViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(FacultyViewModel::class.java)
        binding.viewModel = viewModel
        dialog = CustomProgressDialog(this)
        setCustomActionBarListener()
        setClickListeners()
        setRecyclerAdapter()
        observer()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.faculty))
    }


    private fun setClickListeners() {
        binding.buttonAddFaculty.setOnClickListener {
            navigateToAddFaculty()
        }
    }

    fun setRecyclerAdapter() {
        adapter = FacultyRecyAdapter(this)
        binding.recyclerview.adapter = adapter
    }

    private fun navigateToAddFaculty() {
        startActivity(Intent(this, AddFacultyActivity::class.java))
    }


    private fun observer() {
        getFacultyApiRequest()
        viewModel.getFacultyListResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                    /* this api call is made after another api call.
                     So no need to show the loading indicator as it is shown already.*/
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    it.data?.facultyListResponseApi?.let { response ->
                        if (response.isNotEmpty()) {
                            setAdapterData(response)
                        }
                    }

                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

    }

    private fun getFacultyApiRequest() {
        val tenandId = StorePreferences(this).tenantId
        val trainerId = StorePreferences(this).userId
        viewModel.getFaculty(GetFacultyRequest(tenantId = tenandId, trainerId = trainerId))
    }

    private fun setAdapterData(response: List<FacultyListResponseApi>) {
        adapter.listOfItems = response as ArrayList<FacultyListResponseApi>
    }

    override fun onItemClickListener(studyMaterialListResponseApi: FacultyListResponseApi) {
        navigateToAddFaculty(studyMaterialListResponseApi)
    }

    private fun navigateToAddFaculty(facultyListResponseApi: FacultyListResponseApi) {
        val facultySettingsModel = FacultySettingsModel().getFacultySettingsModel(facultyListResponseApi)
        val addFacultyRequest = AddFacultyRequest(
            emailId = facultyListResponseApi.emailId, name = facultyListResponseApi.name, contactNumber = facultyListResponseApi.contactNumber
        )
        startActivity(Intent(this, AddFacultyActivity::class.java)
            .putExtra(UPDATE_FACULTY_ID, facultyListResponseApi.id.toString())
            .putExtra(Faculty_Settings_PARAM, facultySettingsModel)
            .putExtra(ADD_FACULTY_REQUEST_PARAM, addFacultyRequest)
            .putExtra(IS_FROM_EDIT_PARAM,true)
        )
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getFacultyApiRequest()
    }

}