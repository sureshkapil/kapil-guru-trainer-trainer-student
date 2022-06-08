package com.kapilguru.trainer.ui.reports.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.reports.ReportsRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class ReportsViewModel(private val repository : ReportsRepository,application : Application) : AndroidViewModel(application) {
    private val TAG = "ReportsViewModel"
    var trainerId : Int = StorePreferences(application).trainerId

    val courseList = liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = repository.getCousesList(trainerId.toString())))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        } catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    var webinarsList =  liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = repository.getWebinarList(trainerId.toString())))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    var guestLectureList =  liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = repository.getGuestLectureList(trainerId.toString())))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}