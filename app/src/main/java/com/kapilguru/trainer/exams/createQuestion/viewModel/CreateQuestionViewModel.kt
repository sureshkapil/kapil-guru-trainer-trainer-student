package com.kapilguru.trainer.exams.createQuestion.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.Question
import com.kapilguru.trainer.exams.createQuestion.CreateQuestionRepository
import com.kapilguru.trainer.exams.createQuestion.model.AddQuestionRequest
import com.kapilguru.trainer.exams.createQuestion.model.UpdateQuestionRequest
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchApiResponse
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CreateQuestionViewModel(private val repository: CreateQuestionRepository, application: Application) : AndroidViewModel(application) {
    private val TAG = "CreateQuestionViewModel"
    var course = CourseResponse()
    var questionPaperId: Int = -1
    var questionPaperTitle = ""
    val addQuestionRequest: MutableLiveData<AddQuestionRequest> = MutableLiveData(AddQuestionRequest())
    val addQuestionResponse: MutableLiveData<ApiResource<AddBatchApiResponse>> = MutableLiveData()
    var question: Question? = null
    var isLaunchedForEdit = false

    fun addOrUpdateQuestion() {
        if (isLaunchedForEdit) {
            updateQuestion()
        } else {
            addQuestion()
        }
    }

    private fun addQuestion() {
        addQuestionResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addQuestionRequest.value?.let { addQuesReq ->
                    addQuestionResponse.postValue(ApiResource.success(data = repository.addQuestion(addQuesReq)))
                }
            } catch (exception: HttpException) {
                addQuestionResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            }catch (exception: IOException) {
                addQuestionResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }

    private fun updateQuestion() {
        addQuestionResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addQuestionRequest.value?.let { addQuesReq ->
                    val updateQuesReq = UpdateQuestionRequest().getObject(addQuesReq)
                    addQuestionResponse.postValue(ApiResource.success(data = repository.updateQuestion(question?.questionId.toString(), updateQuesReq)))
                }
            } catch (exception: HttpException) {
                addQuestionResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred !"))
            }catch (exception: IOException) {
                addQuestionResponse.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred !"))
            }
        }
    }
}