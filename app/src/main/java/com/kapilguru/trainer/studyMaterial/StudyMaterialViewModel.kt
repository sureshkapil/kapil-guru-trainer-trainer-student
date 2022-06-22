package com.kapilguru.trainer.studyMaterial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudyMaterialViewModel(var repository: StudyMaterialRepository):ViewModel() {
    
    var studyMaterialListResponse: MutableLiveData<ApiResource<StudyMaterialListResponse>> = MutableLiveData()
    
    
    
    fun getListOfStudyMaterials(studyMatrialListRequest :StudyMatrialListRequest) {
        studyMaterialListResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studyMaterialListResponse.postValue(
                    ApiResource.success(data = repository.getListOfStudyMaterials(studyMatrialListRequest))
                )
            } catch (exception: HttpException) {
                studyMaterialListResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            } catch (exception: IOException) {
                studyMaterialListResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
        
    }


}