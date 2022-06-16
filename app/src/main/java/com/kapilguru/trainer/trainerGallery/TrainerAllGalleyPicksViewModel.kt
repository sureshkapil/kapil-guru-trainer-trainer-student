package com.kapilguru.trainer.trainerGallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.BuildConfig
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TrainerAllGalleyPicksViewModel(var trainerAllGalleryPicksRepository: TrainerAllGalleryPicksRepository):ViewModel() {

   var commonUploadImageResponse:MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()


   var getAllImages:MutableLiveData<ApiResource<TrainerGalleryImagesResponse>> = MutableLiveData()



   fun uploadGalleryImage(uploadImageGallery: UploadImageGallery) {

      commonUploadImageResponse.postValue(ApiResource.loading(null))
      viewModelScope.launch(Dispatchers.IO) {
         try {
            commonUploadImageResponse.postValue(ApiResource.success(trainerAllGalleryPicksRepository.uploadImage(uploadImageGallery)))
         } catch (e: HttpException) {
            commonUploadImageResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
         }catch (e: IOException) {
            commonUploadImageResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         }
         catch(e: Exception){
            commonUploadImageResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         }
      }
   }

   fun getAllImagesList() {
      getAllImages.postValue(ApiResource.loading(null))
      viewModelScope.launch(Dispatchers.IO) {
         try {
            getAllImages.postValue(ApiResource.success(trainerAllGalleryPicksRepository.getImagesList(BuildConfig.PACKAGE_ID)))
         } catch (e: HttpException) {
            getAllImages.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
         } catch (e: IOException) {
            getAllImages.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         } catch (e: Exception) {
            getAllImages.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         }
      }
      getAllImages
   }


}