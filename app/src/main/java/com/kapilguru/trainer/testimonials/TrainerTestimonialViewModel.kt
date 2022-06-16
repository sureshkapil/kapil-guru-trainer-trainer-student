package com.kapilguru.trainer.testimonials

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TrainerTestimonialViewModel(var trainerTestimonialRepository: TrainerTestimonialRepository, val application: Application):ViewModel() {

   
   var postTestimonial: MutableLiveData<PostTestimonialsModel> = MutableLiveData(PostTestimonialsModel())
   var postTestimonialsResponse: MutableLiveData<ApiResource<PostTestimonialsResponse>> = MutableLiveData()

   init {
      postTestimonial.value?.tenantId = StorePreferences(application).tenantId
      postTestimonial.value?.name = StorePreferences(application).userName
      postTestimonial.value?.trainerId = StorePreferences(application).userId
   }

   fun addTestimonials() {
      postTestimonialsResponse.value = ApiResource.loading(null)
      viewModelScope.launch(Dispatchers.IO) {
         try {
            postTestimonialsResponse.postValue(ApiResource.success(trainerTestimonialRepository.addtestimonials(postTestimonial.value!!)))
         } catch (e: HttpException) {
            postTestimonialsResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
         } catch (e: IOException) {
            postTestimonialsResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         } catch (e: Exception) {
            postTestimonialsResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         }
      }
   }




}