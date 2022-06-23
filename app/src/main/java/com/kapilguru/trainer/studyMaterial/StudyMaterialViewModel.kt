package com.kapilguru.trainer.studyMaterial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.studyMaterial.fileStructure.FolderContentRequest
import com.kapilguru.trainer.studyMaterial.fileStructure.FolderContentResponse
import com.kapilguru.trainer.studyMaterial.fileStructure.FolderContentResponseApi
import com.kapilguru.trainer.studyMaterial.studyMaterialOverview.StudyMaterialOverViewResponse
import com.kapilguru.trainer.studyMaterial.studyMaterialOverview.StudyMaterialOverViewResponseApi
import com.kapilguru.trainer.studyMaterial.studyMaterialOverview.StudyMatrialOverViewRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudyMaterialViewModel(var repository: StudyMaterialRepository):ViewModel() {

    var studyMaterialOverViewResponseApi: MutableLiveData<StudyMaterialOverViewResponseApi> = MutableLiveData(StudyMaterialOverViewResponseApi())

    var studyMaterialListResponse: MutableLiveData<ApiResource<StudyMaterialListResponse>> = MutableLiveData()

    var studyMaterialOverViewResponse: MutableLiveData<ApiResource<StudyMaterialOverViewResponse>> = MutableLiveData()

    var folderContentResponse: MutableLiveData<ApiResource<FolderContentResponse>> = MutableLiveData()

    var folderContentResponseApi: MutableLiveData<List<FolderContentResponseApi>> = MutableLiveData()
    
    
    fun getListOfStudyMaterials(studyMatrialListRequest :StudyMatrialListRequest) {
        studyMaterialListResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studyMaterialListResponse.postValue(ApiResource.success(data = repository.getListOfStudyMaterials(studyMatrialListRequest))
                )
            } catch (exception: HttpException) {
                studyMaterialListResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            } catch (exception: IOException) {
                studyMaterialListResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }

    fun getStudyMaterialOverView(studyMatrialOverViewRequest : StudyMatrialOverViewRequest) {
        studyMaterialOverViewResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studyMaterialOverViewResponse.postValue(ApiResource.success(data = repository.getStudyMaterialOverView(studyMatrialOverViewRequest))
                )
            } catch (exception: HttpException) {
                studyMaterialOverViewResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            } catch (exception: IOException) {
                studyMaterialOverViewResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }


    fun getFolderContent(folderContentRequest : FolderContentRequest) {
        folderContentResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                folderContentResponse.postValue(ApiResource.success(data = repository.getFolderContent(folderContentRequest))
                )
            } catch (exception: HttpException) {
                folderContentResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            } catch (exception: IOException) {
                folderContentResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }


}