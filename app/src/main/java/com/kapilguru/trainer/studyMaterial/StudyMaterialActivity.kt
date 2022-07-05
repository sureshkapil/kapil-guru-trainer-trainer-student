package com.kapilguru.trainer.studyMaterial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityStudyMaterialBinding
import com.kapilguru.trainer.databinding.ActivityTrainerAllGalleryPicksBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.studyMaterial.studyMaterialOverview.StudyMaterialOverViewActivity
import java.io.File

class StudyMaterialActivity : BaseActivity(), StudyMaterialListAdapter.StudyMaterialItemClick {

    lateinit var binding: ActivityStudyMaterialBinding
    lateinit var viewModel: StudyMaterialViewModel
    lateinit var appUri: Uri
    lateinit var adapter: StudyMaterialListAdapter
    lateinit var dialog: CustomProgressDialog
    var isStudyMaterial = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_study_material)
        viewModel = ViewModelProvider(this, StudyMaterialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudyMaterialViewModel::class.java)
        dialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        getIntentData()
        setclickListeners()
        setCustomActionBar()
        setAdapterInfo()
        observeViewModels()
    }

    private fun getIntentData() {
        isStudyMaterial = intent.getBooleanExtra(PARAM_IS_FROM_DASHBOARD_AS_STUDY_MATERIAL,false)
        binding.addButton.text = if(isStudyMaterial)getString(R.string.study_material) else getString(R.string.recorded_courses)
    }

    private fun setclickListeners() {

        /*    binding.uploadImage.setOnClickListener {
                uploadGalleryImage()
            }*/
    }

    fun setCustomActionBar() {
        if(isStudyMaterial) {
            setActionbarBackListener(this, binding.actionbar, getString(R.string.study_material))
        } else {
            setActionbarBackListener(this, binding.actionbar, getString(R.string.recorded_courses))
        }
    }

    private fun setAdapterInfo() {
        val id = StorePreferences(this).userId
        viewModel.getListOfStudyMaterials(StudyMatrialListRequest().apply {
            trainerId = id
            isRecorded = getStudyMaterialStatus()
        })
        adapter = StudyMaterialListAdapter(this)
        binding.studyMaterialRecy.adapter = adapter
    }

    private fun getStudyMaterialStatus() = if(isStudyMaterial) 2 else 1

    private fun observeViewModels() {
        viewModel.studyMaterialListResponse.observe(this, Observer { studyMaterialListResponse ->
            when (studyMaterialListResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    studyMaterialListResponse.data?.studyMaterialListResponseApi?.let {
                        if (it.isNotEmpty()) {
                            setDataToAdapter(it)
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }

        })
    }

    private fun setDataToAdapter(list: List<StudyMaterialListResponseApi>) {
        adapter.listOfItems = (list as ArrayList<StudyMaterialListResponseApi>)
    }

    override fun onItemClickListener(studyMaterialListResponseApi: StudyMaterialListResponseApi) {
        startActivity(Intent(this,StudyMaterialOverViewActivity::class.java)
            .putExtra(STUDY_MATERIAL_COURSE_ID,studyMaterialListResponseApi.id)
            .putExtra(STUDY_MATERIAL_ID,studyMaterialListResponseApi.studyMaterialId))
    }

}