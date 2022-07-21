package com.kapilguru.trainer.student.homeActivity.studentGallery.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.BuildConfig
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.homeActivity.studentGallery.StudentGalleryRepository
import com.kapilguru.trainer.student.homeActivity.studentGallery.model.ImageResData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentGalleryViewModel(val repository: StudentGalleryRepository) : ViewModel() {
    private val TAG = "StudentGalleryViewModel"

    val imagesList: MutableLiveData<ArrayList<ImageResData>> = MutableLiveData()
    val showLoadingIndicator: MutableLiveData<Boolean> = MutableLiveData()
    val informUser: MutableLiveData<String> = MutableLiveData()

    fun getImages() {
        showLoadingIndicator.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageResponse = repository.getImages(BuildConfig.PACKAGE_ID)
                imageResponse.imageList?.let { imageListNotNull ->
                    if(imageListNotNull.isNotEmpty()){
                        imagesList.postValue(imageListNotNull)
                    }
                }
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                informUser.postValue(exception.message)
            } catch (exception: IOException) {
                informUser.postValue(exception.message ?: "Error Occurred!")
            } catch (exception: HttpException) {
                informUser.postValue(exception.message ?: "Error Occurred!")
            }
            showLoadingIndicator.postValue(false)
        }
    }
}