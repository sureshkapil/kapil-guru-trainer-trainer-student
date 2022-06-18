package com.kapilguru.trainer.testimonials

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TrainerTestimonialViewModel(var trainerTestimonialRepository: TrainerTestimonialRepository, val application: Application):ViewModel() {

   
   var postTestimonial: MutableLiveData<PostTestimonialsModel> = MutableLiveData(PostTestimonialsModel())
   var postTestimonialsResponse: MutableLiveData<ApiResource<PostTestimonialsResponse>> = MutableLiveData()
   var fetchTestimonialsResponse: MutableLiveData<ApiResource<FetchTestimonialsResponse>> = MutableLiveData()

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

   fun getTestimonials(tenantId: Int) {
      fetchTestimonialsResponse.value = ApiResource.loading(null)
      viewModelScope.launch(Dispatchers.IO) {
         try {
            fetchTestimonialsResponse.postValue(ApiResource.success(trainerTestimonialRepository.getAllTestimonials(tenantId)))
         } catch (e: HttpException) {
            fetchTestimonialsResponse.postValue(ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!"))
         } catch (e: IOException) {
            fetchTestimonialsResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         } catch (e: Exception) {
            fetchTestimonialsResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
         }
      }
   }


}