package com.kapilguru.trainer.ui.courses.addcourse.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.addcourse.CategoryRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class CategoryViewModel(private  val categoryRepository: CategoryRepository): ViewModel() {

    var categoryListApi = liveData(Dispatchers.IO) {
        Log.v("checkInfo", "checkhere_2");
        emit(ApiResource.loading(data = null))
        try {
            Log.v("checkInfo", "checkhere");
            emit(ApiResource.success(data = categoryRepository.getCategory()))
        } catch (exception: HttpException) {
            Log.v("checkInfo", "checkhere_22");
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception: IOException) {
            Log.v("checkInfo", "checkhere_22");
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}