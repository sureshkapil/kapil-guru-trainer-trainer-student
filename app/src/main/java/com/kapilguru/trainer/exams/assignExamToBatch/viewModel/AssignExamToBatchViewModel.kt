package com.kapilguru.trainer.exams.assignExamToBatch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.exams.assignExamToBatch.AssignExamToBatchRepository
import com.kapilguru.trainer.exams.assignExamToBatch.model.AssignExamToBatchRequest
import com.kapilguru.trainer.exams.assignExamToBatch.model.BatchByCourseResponse
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaper
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchApiResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AssignExamToBatchViewModel(private val repository: AssignExamToBatchRepository) : ViewModel() {
    private val TAG = "AssignExamToBatchViewModel"
    val batchByCourseApiResponse: MutableLiveData<ApiResource<BatchByCourseResponse>> = MutableLiveData()
    val assignExamToBatchApiResponse: MutableLiveData<ApiResource<AddBatchApiResponse>> = MutableLiveData()
    val assignExamToBatchApiReq: MutableLiveData<AssignExamToBatchRequest> = MutableLiveData(AssignExamToBatchRequest())
    var course = CourseResponse()
    var questionPaper = QuestionPaper()

    fun getActiveBatchesByCourse() {
        batchByCourseApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchByCourseApiResponse.postValue(ApiResource.success(data = repository.getActiveBatchesByCourse(course.trainerId.toString(), course.courseId.toString())))
            } catch (exception: HttpException) {
                batchByCourseApiResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            } catch (exception: IOException) {
                batchByCourseApiResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun callAssignExamToBatchApi() {
        assignExamToBatchApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                assignExamToBatchApiReq.value?.let { request ->
                    assignExamToBatchApiResponse.postValue(ApiResource.success(data = repository.assignExamToBatch(request)))
                }
            } catch (exception: HttpException) {
                assignExamToBatchApiResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            }catch (exception: IOException) {
                assignExamToBatchApiResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!"))
            }
        }
    }
}