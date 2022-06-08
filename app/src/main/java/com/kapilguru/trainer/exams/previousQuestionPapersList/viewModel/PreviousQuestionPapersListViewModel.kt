package com.kapilguru.trainer.exams.previousQuestionPapersList.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.Question
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaper
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionsListResponse
import com.kapilguru.trainer.exams.previousQuestionPapersList.PreviousQuestionPapersListRepository
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.CopyFromQuesPaperRequest
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.PreviousQuestionPapersListResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PreviousQuestionPapersListViewModel(val repository: PreviousQuestionPapersListRepository) : ViewModel() {
    private val TAG = "PreviousQuestionPapersListViewModel"
    val previousQuestionPapersListApiRes: MutableLiveData<ApiResource<PreviousQuestionPapersListResponse>> = MutableLiveData()
    val questionsListApiResponse: MutableLiveData<ApiResource<QuestionsListResponse>> = MutableLiveData()
    val questionsList : MutableLiveData<ArrayList<Question>> = MutableLiveData()
    var questionPaper = QuestionPaper()
    var course = CourseResponse()
    var copyFromQuestionPaperApiReq = CopyFromQuesPaperRequest()
    val copyFromQuestionPaperApiResponse: MutableLiveData<ApiResource<QuestionsListResponse>> = MutableLiveData()

    fun getQuestionPapersList() {
        previousQuestionPapersListApiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                previousQuestionPapersListApiRes.postValue(
                    ApiResource.success(data = repository.getPreviousQuestionPapersList(course.trainerId.toString(), course.courseId.toString()))
                )
            } catch (exception: HttpException) {
                previousQuestionPapersListApiRes.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            }catch (exception: IOException) {
                previousQuestionPapersListApiRes.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }

    fun getQuestionsList() {
        questionsListApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionsListApiResponse.postValue(ApiResource.success(data =repository.getQuestionsList(questionPaper.questionPaperId.toString())))
            } catch (exception: HttpException) {
                questionsListApiResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            }catch (exception: IOException) {
                questionsListApiResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }

    fun callCopyFromQuestionPaperApi(request: CopyFromQuesPaperRequest){
        copyFromQuestionPaperApiReq = request
        copyFromQuestionPaperApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                copyFromQuestionPaperApiResponse.postValue(ApiResource.success(data =repository.callCopyFromQuestionPaper(request)))
            } catch (exception: HttpException) {
                copyFromQuestionPaperApiResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            }catch (exception: IOException) {
                copyFromQuestionPaperApiResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }
}