package com.kapilguru.trainer.studyMaterial.fileStructure

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityFileStructureBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.studyMaterial.StudyMaterialViewModel
import com.kapilguru.trainer.studyMaterial.StudyMaterialViewModelFactory
import androidx.recyclerview.widget.DividerItemDecoration
import com.kapilguru.trainer.ui.courses.view_course.PdfViewerActivity

class FileStructureActivity: BaseActivity(), FileStructureAdapter.FileItemClick {

    lateinit var binding: ActivityFileStructureBinding
    lateinit var viewModel: StudyMaterialViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var fileStructureAdapter: FileStructureAdapter
    var courseId: Int = 0
    var studyMaterialId: Int = 0
    var parentId: Int = 0
    var courseTitle: String ? = ""
    private  val TAG = "FileStructureActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_file_structure)
        viewModel = ViewModelProvider(this, StudyMaterialViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(StudyMaterialViewModel::class.java)
//        binding.viewModel = viewModel
        dialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        getIntentDataAndMakeAPiCall()
        initiateAdapter()
        setCustomActionBar()
        observeViewModel()
    }


    fun getIntentDataAndMakeAPiCall() {
        courseId = intent.getIntExtra(STUDY_MATERIAL_COURSE_ID, 0)
        parentId = intent.getIntExtra(PARENT_ID, 0)
        studyMaterialId = intent.getIntExtra(STUDY_MATERIAL_ID, 0)
        courseTitle = intent.getStringExtra(COURSE_TITLE_PARAM)
        val folderContentRequest = FolderContentRequest(
            courseId = this.courseId,
            parentId = this.parentId,
            studyMaterialId= this.studyMaterialId)
        viewModel.getFolderContent(folderContentRequest)
    }

    private fun initiateAdapter() {
        fileStructureAdapter = FileStructureAdapter(this)
        binding.fileRecy.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.fileRecy.adapter = fileStructureAdapter
    }

    fun setCustomActionBar() {
        val courseTitle = this.courseTitle ?: ""
        setActionbarBackListener(this, binding.actionbar, courseTitle)
    }

    private fun observeViewModel() {
        viewModel.folderContentResponse.observe(this, Observer { folderContentResponse ->
            when (folderContentResponse.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    folderContentResponse.data?.folderContentResponseApi?.let { response ->
                        if (response.isNotEmpty()) {
                            viewModel.folderContentResponseApi.value = response
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

    private fun setAdapterData(response: List<FolderContentResponseApi>) {
        fileStructureAdapter.listOfItems = response as ArrayList<FolderContentResponseApi>
    }

    override fun onItemClickListener(studyMaterialListResponseApi: FolderContentResponseApi) {
        studyMaterialListResponseApi.isFolder?.let {isFolder->
            if (isFolder == 1) {
                launchSameActivity(studyMaterialListResponseApi)
            } else {
                studyMaterialListResponseApi.mimetype?.let { type ->
                    if(type.contains("pdf",true)) {
                        launchPdfActivity(studyMaterialListResponseApi.filename!!)
                    }
                }
            }
        }?:run {
            studyMaterialListResponseApi.mimetype?.let { type ->
                if(type.contains("pdf",true)) {
                    launchPdfActivity(studyMaterialListResponseApi.filename!!)
                }
            }

        }

    }

    private fun launchSameActivity(studyMaterialListResponseApi: FolderContentResponseApi) {
        startActivity(
            Intent(this,  FileStructureActivity::class.java)
            .putExtra(STUDY_MATERIAL_COURSE_ID,studyMaterialListResponseApi.courseId)
            .putExtra(STUDY_MATERIAL_ID,studyMaterialListResponseApi.studyMaterialId)
            .putExtra(COURSE_TITLE_PARAM,studyMaterialListResponseApi.name)
            .putExtra(PARENT_ID,studyMaterialListResponseApi.id)
        )
    }


    private fun launchPdfActivity(name: String) {
        val url = BuildConfig.BASE_URL+PDF_URL_STUDY_MATERIAL+name
        val intent = Intent(this, PdfViewerActivity::class.java).putExtra(PDF_PARAM,url)
        startActivity(intent)
    }

}