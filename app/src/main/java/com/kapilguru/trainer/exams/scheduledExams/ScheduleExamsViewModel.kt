package com.kapilguru.trainer.exams.scheduledExams

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ScheduleExamsViewModel(private val repository: ScheduleExamsRepository, application: Application) : AndroidViewModel(application) {
    val scheduleExamsAPi: MutableLiveData<ApiResource<ScheduledExamsAPI>> = MutableLiveData()
    val trainerId = StorePreferences(application).trainerId.toString()


    fun getScheduleExamsList() {
        scheduleExamsAPi.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                scheduleExamsAPi.postValue(ApiResource.success(data = repository.getScheduleList(trainerId)))
            } catch (exception: HttpException) {
                scheduleExamsAPi.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            }catch (exception: IOException) {
                scheduleExamsAPi.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }

}