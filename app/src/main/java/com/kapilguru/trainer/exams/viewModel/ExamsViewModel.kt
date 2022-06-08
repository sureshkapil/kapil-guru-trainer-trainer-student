package com.kapilguru.trainer.exams.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.exams.ExamsRepository
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaperTitleRequest
import com.kapilguru.trainer.exams.scheduledExams.ScheduledExamsAPI
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchApiResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseApi
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ExamsViewModel(private val repository: ExamsRepository, application: Application) : AndroidViewModel(application) {
    private val TAG = "ExamsViewModel"
    val courseApiRes: MutableLiveData<ApiResource<CourseApi>> = MutableLiveData()
    val questionPaperTitleApiReq: MutableLiveData<QuestionPaperTitleRequest> = MutableLiveData(QuestionPaperTitleRequest())
    val questionPaperTitleApiResponse: MutableLiveData<ApiResource<AddBatchApiResponse>> = MutableLiveData()
    val scheduleExamsAPi: MutableLiveData<ApiResource<ScheduledExamsAPI>> = MutableLiveData()
    val trainerId = StorePreferences(application).userId.toString()
    var verifiedCoursesList = ArrayList<CourseResponse>()
    val emptyCourseId = -1
    var courseIdFromClassRoom : Int = emptyCourseId

    fun getCourseList() {
        courseApiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                courseApiRes.postValue(ApiResource.success(data = repository.getCoursesList(trainerId)))
            } catch (exception: HttpException) {
                courseApiRes.postValue(ApiResource.error(data = null, exception.message() ?: "Error Ocured !"))
            }catch (exception: IOException) {
                courseApiRes.postValue(ApiResource.error(data = null, exception.message ?: "Error Ocured !"))
            }
        }
    }

    fun callCreateQuestionPaperTitleApi(questionPaperTitle: String, course: CourseResponse) {
        questionPaperTitleApiReq.value?.courseId = course.courseId!!
        questionPaperTitleApiReq.value?.trainerId = course.trainerId.toString()
        questionPaperTitleApiReq.value?.title = questionPaperTitle
        questionPaperTitleApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionPaperTitleApiResponse.postValue(ApiResource.success(data = repository.sendQuestionPaperTitle(questionPaperTitleApiReq.value!!)))
            } catch (exception: HttpException) {
                questionPaperTitleApiResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            }catch (exception: IOException) {
                questionPaperTitleApiResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }

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