package com.kapilguru.trainer.testimonials

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourseResponse

class TrainerTestimonialViewModel(var trainerTestimonialRepository: TrainerTestimonialRepository):ViewModel() {

   var commonUploadImageResponse:MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()



//   fun uploadGalleryImage(uploadImageGallery: UploadImageGallery) {
//
//      commonUploadImageResponse.postValue(ApiResource.loading(null))
//      viewModelScope.launch(Dispatchers.IO) {
//         try {
//            commonUploadImageResponse.postValue(ApiResource.success(trainerAllGalleryPicksRepository.uploadImage(uploadImageGallery)))
//         } catch (e: HttpException) {
//            commonUploadImageResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
//         }catch (e: IOException) {
//            commonUploadImageResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
//         }
//         catch(e: Exception){
//            commonUploadImageResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
//         }
//      }
//   }

}