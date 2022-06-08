package com.kapilguru.trainer.exams.conductExams.createQuestionPaper.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.CreateQuestionPaperRepository
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.*
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CreateQuestionPaperViewModel(private val repository: CreateQuestionPaperRepository, application: Application) :
    AndroidViewModel(application) {

    private val TAG = "CreateQuestionPaperViewModel"
    var course = CourseResponse()
    var isLaunchedForAddingNewQuestions = false
    var isLaunchedForCopyQuestionPaper = false
    var isLaunchedForViewAndAssign = false
    val questionsListApiResponse: MutableLiveData<ApiResource<QuestionsListResponse>> = MutableLiveData()
    val questionsList : MutableLiveData<ArrayList<Question>> = MutableLiveData(ArrayList<Question>())
    var questionPaper = QuestionPaper()

    fun copyQuestionPaper() {

    }

    fun getQuestionsList() {
        questionsListApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionsListApiResponse.postValue(ApiResource.success(data =repository.getQuestionsList(questionPaper.questionPaperId.toString())))
            } catch (exception: HttpException) {
                questionsListApiResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            } catch (exception: IOException) {
                questionsListApiResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }
}