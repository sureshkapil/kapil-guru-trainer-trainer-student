package com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel

import android.app.Application
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageResponse
import com.kapilguru.trainer.convertDateAndTimeToApiData
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.BatchCompletionReqRepository
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionResBCReq
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionResponse
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.SendBatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.viewModel.BatchCompletionReqViewModel.Companion.acceptPercentage
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentsRequest
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentsResponse
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class BatchCompletionReqViewModel(private val repository: BatchCompletionReqRepository, application: Application) : AndroidViewModel(application) {
    private val TAG = "BatchCompletionReqViewModel"

    var pdfName: MutableLiveData<String> = MutableLiveData()
    var downloadPdfUrl: MutableLiveData<String> = MutableLiveData()
    var isPdfSelected: Boolean = true
    var isPdfUploaded: Boolean = false
    var uploadPdfResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()

    var batchCompletionReq = BatchCompletionRequest()
    var batchData = NewMessageData()
    val batchCompletionResponse: MutableLiveData<ApiResource<BatchCompletionResponse>> = MutableLiveData()
    val batchCompletionStatusList: MutableLiveData<ArrayList<BatchCompletionResBCReq>> = MutableLiveData(ArrayList())
    val sendBatchCompletionRequest = SendBatchCompletionRequest()
    val sendBatchCompletionResponse: MutableLiveData<ApiResource<AddBatchApiResponse>> = MutableLiveData()

    var batchInfo: MutableLiveData<NewMessageData> = MutableLiveData()
    var batchApiResponse: MutableLiveData<ApiResource<NewMessageResponse>> = MutableLiveData()
    var batchId: MutableLiveData<String?> = MutableLiveData()
    var courseId: MutableLiveData<String?> = MutableLiveData()

    var batchDocumentResponse: MutableLiveData<ApiResource<BatchDocumentsResponse>> = MutableLiveData()


    var acceptedPercentage: MutableLiveData<Int> = MutableLiveData(0)
    var pendingCount: MutableLiveData<Int> = MutableLiveData(0)
    var acceptedCount: MutableLiveData<Int> = MutableLiveData(0)
    var rejectedCount: MutableLiveData<Int> = MutableLiveData(0)


    fun fetchBatchCompletionRequest() {
        batchCompletionResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchCompletionResponse.postValue(
                    ApiResource.success(
                        repository.getBatchCompletionResponse(
                            batchCompletionReq
                        )
                    )
                )
            } catch (exception: HttpException) {
                batchCompletionResponse.postValue(
                    ApiResource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred!"
                    )
                )
            }catch (exception: IOException) {
                batchCompletionResponse.postValue(
                    ApiResource.error(
                        data = null,
                        message = exception.message ?: "Error Occurred!"
                    )
                )
            }
        }
    }

    fun sendCompletionRequest() {
        setValuesToSendBatchCompletionReq()
        sendBatchCompletionResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendBatchCompletionResponse.postValue(
                    ApiResource.success(
                        repository.sendBatchCompletionRequest(
                            sendBatchCompletionRequest
                        )
                    )
                )
            } catch (exception: HttpException) {
                sendBatchCompletionResponse.postValue(
                    ApiResource.error(
                        data = null,
                        exception.message() ?: "Error Occured"
                    )
                )
            } catch (exception: IOException) {
                sendBatchCompletionResponse.postValue(
                    ApiResource.error(
                        data = null,
                        exception.message ?: "Error Occured"
                    )
                )
            }
        }
    }

    private fun setValuesToSendBatchCompletionReq() {
        sendBatchCompletionRequest.batchId = batchCompletionReq.batchId
        sendBatchCompletionRequest.courseId = batchCompletionReq.courseId
        sendBatchCompletionRequest.trainerId = batchCompletionReq.trainerId.toString()
        sendBatchCompletionRequest.requestDate =
            Calendar.getInstance().convertDateAndTimeToApiData()
    }


    fun getBatchDetails() {
        batchApiResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchApiResponse.postValue(
                    ApiResource.success(
                        repository.getBatchDetails(
                            batchId.value!!
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                batchApiResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                batchApiResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }



    }

    fun uploadBatchPDF(file: File, trainerId: String, courseId: String,batchId: String, pdfName: String) {
        uploadPdfResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trainerId = trainerId.toRequestBody("text/plain".toMediaType())
                val courseId = courseId.toRequestBody("text/plain".toMediaType())
                val batchId = batchId.toRequestBody("text/plain".toMediaType())
                val pdfName = pdfName.toRequestBody("text/plain".toMediaType())
                val file_size = (file.length() / 1024).toString().toInt()
                val requestFile: RequestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)

                uploadPdfResponse.postValue(
                    ApiResource.success(
                        repository.uploadBatchPdfFile(body, trainerId, pdfName, courseId, batchId)
                    )
                )
            } catch (e: HttpException) {
                uploadPdfResponse.postValue(
                    ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!")
                )
            }catch (e: IOException) {
                uploadPdfResponse.postValue(
                    ApiResource.error(data = null, message = e.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun getBatchPdfList(request: BatchDocumentsRequest) {
        batchDocumentResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchDocumentResponse.postValue(
                    ApiResource.success(repository.getBatchPdfDocuments(request))
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                batchDocumentResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                batchDocumentResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun downloadPdfDocument(fileName: String) {
//        batchDocumentResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.downloadPdfDocument(fileName)
//                batchDocumentResponse.postValue(
//                    ApiResource.success(repository.getBatchPdfDocuments(request))
//                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
//                batchDocumentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
//                batchDocumentResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    companion object {
    @JvmStatic
    @BindingAdapter("acceptPercentage")
    fun TextView.acceptPercentage(list: ArrayList<BatchCompletionResBCReq>?) {
        list?.let { completionList ->
            val acceptedList = completionList.filter {
                it.bcrReqRespStatus.equals("Accepted")
            }
            try {
                val percent = (acceptedList.size / completionList.size) * 100
                this.text = percent.toString() + "%"
            } catch (exception: ArithmeticException) {
                this.text = ""
            }
        } ?: run {
            this.text = ""
        }
    }
}
}