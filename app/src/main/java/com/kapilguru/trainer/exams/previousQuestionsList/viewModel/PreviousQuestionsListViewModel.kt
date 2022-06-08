package com.kapilguru.trainer.exams.previousQuestionsList.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.exams.previousQuestionsList.PreviousQuestionsListRepository
import com.kapilguru.trainer.exams.previousQuestionsList.model.AddExistingQuesApiRequest
import com.kapilguru.trainer.exams.previousQuestionsList.model.PreviousQuestionsListResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchApiResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PreviousQuestionsListViewModel(private val repository: PreviousQuestionsListRepository) : ViewModel() {
    private val TAG = "PreviousQuestionsListViewModel"
    val previousQuestionsList: MutableLiveData<ApiResource<PreviousQuestionsListResponse>> = MutableLiveData()
    var course = CourseResponse()
    var questionPaperId : Int = -1
    val addExistingQuesApiReq : MutableLiveData<AddExistingQuesApiRequest> = MutableLiveData()
    val addExistingQuestionsApiRes : MutableLiveData<ApiResource<AddBatchApiResponse>> = MutableLiveData()

    fun getPreviousQuestionsList() {
        previousQuestionsList.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                previousQuestionsList.postValue(ApiResource.success(data = repository.getPreviousQuestionsList(course.trainerId.toString(), course.courseId.toString())))
            } catch (exception: HttpException) {
                previousQuestionsList.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            }catch (exception: IOException) {
                previousQuestionsList.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun addExistingQuestions(){
        addExistingQuestionsApiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addExistingQuesApiReq.value?.let { request->
                    addExistingQuestionsApiRes.postValue(ApiResource.success(data = repository.addExistingQuestions(request)))
                }
            } catch (exception: HttpException) {
                addExistingQuestionsApiRes.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            }catch (exception: IOException) {
                addExistingQuestionsApiRes.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!"))
            }
        }
    }
}