package com.kapilguru.trainer.studyMaterial.studyMaterialOverview

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityStudyMaterialOverViewBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.studyMaterial.StudyMaterialViewModel
import com.kapilguru.trainer.studyMaterial.StudyMaterialViewModelFactory
import com.kapilguru.trainer.studyMaterial.fileStructure.FileStructureActivity

class StudyMaterialOverViewActivity : BaseActivity() {

    lateinit var binding: ActivityStudyMaterialOverViewBinding
    lateinit var viewModel: StudyMaterialViewModel
    lateinit var dialog: CustomProgressDialog
     var courseId: Int? =0
     var studyMaterialId: Int? =0
     var courseTitle: String? =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_study_material_over_view)
        viewModel = ViewModelProvider(this, StudyMaterialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudyMaterialViewModel::class.java)
        binding.viewModel = viewModel
        dialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        setCustomActionBar()
        fetchIntentAndMakeApiCall()
        clickListeners()
        observeViewModels()
    }

    fun setCustomActionBar() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.study_material))
    }

    private fun observeViewModels() {
        viewModel.studyMaterialOverViewResponse.observe(this, Observer { studyMaterialOverViewResponse ->
            when (studyMaterialOverViewResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    studyMaterialOverViewResponse.data?.studyMaterialOverViewResponseApi?.let {
                        if (it.isNotEmpty()) {
                            viewModel.studyMaterialOverViewResponseApi.value = it[0]
                            setCourseIdAndTrainerId(it[0].id, it[0].studyMaterialId,it[0].courseTitle)
                        }
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }

        })
    }

    fun setCourseIdAndTrainerId(id: Int?, studyMaterialId: Int?, courseTitle: String?) {
        this.courseId = id
        this.studyMaterialId = studyMaterialId
        this.courseTitle = courseTitle
    }
    private fun fetchIntentAndMakeApiCall() {
        courseId = intent.getIntExtra(STUDY_MATERIAL_COURSE_ID, 0)
//        studyMaterialId = intent.getIntExtra(STUDY_MATERIAL_ID, 0)
        val trainerId: Int = StorePreferences(this).userId
        courseId?.let {
            if (it != 0) {
                viewModel.getStudyMaterialOverView(StudyMatrialOverViewRequest(it, trainerId))
            }
        }
    }


    fun clickListeners() {
        binding.viewContent.setOnClickListener {
            navigateToFileActivity()
        }
    }

    private fun navigateToFileActivity() {
        startActivity(Intent(this,  FileStructureActivity::class.java)
            .putExtra(STUDY_MATERIAL_COURSE_ID,courseId)
            .putExtra(STUDY_MATERIAL_ID,studyMaterialId)
            .putExtra(COURSE_TITLE_PARAM,courseTitle)
        )
    }
}