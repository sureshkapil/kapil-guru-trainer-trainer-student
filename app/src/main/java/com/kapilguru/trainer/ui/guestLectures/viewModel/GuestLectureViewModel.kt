package com.kapilguru.trainer.ui.guestLectures.viewModel

import android.app.Application
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.R
import com.kapilguru.trainer.SENT_FOR_APPROVAL
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponseApi
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.guestLectures.GuestLectureRepository
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class GuestLectureViewModel(private val guestLectureRepository: GuestLectureRepository, val context: Application) : AndroidViewModel(context) {
    var totalGuestLectures: MutableLiveData<String> = MutableLiveData("0")
    var trainerId: Int
    var guestLectureDeleteId: MutableLiveData<String> = MutableLiveData()
    var guestLectureResponseList: MutableLiveData<ApiResource<GuestLectureResponse>> = MutableLiveData()
    var liveUpComingGuestLectureResponseList: MutableLiveData<ApiResource<GuestLectureResponse>> = MutableLiveData()
    var deleteGuestLectureApiResponse: MutableLiveData<ApiResource<CommonResponseApi>> = MutableLiveData()

    init {
        val pref = StorePreferences(context)
        trainerId = pref.userId
    }

    fun fetchGuestLectureList(){
        guestLectureResponseList.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                guestLectureResponseList.postValue(ApiResource.success(data = guestLectureRepository.getGuestLectureList(trainerId.toString())))
            } catch (exception: IOException) {
                guestLectureResponseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: HttpException) {
                guestLectureResponseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun fetchLiveUpComingGuestLectureList(){
        liveUpComingGuestLectureResponseList.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                liveUpComingGuestLectureResponseList.postValue(ApiResource.success(data = guestLectureRepository.getLiveUpComingGuestLectureList(trainerId.toString())))
            } catch (exception: IOException) {
                liveUpComingGuestLectureResponseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: HttpException) {
                liveUpComingGuestLectureResponseList.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun deleteGuestLecture(guestLectureId : String){
        deleteGuestLectureApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteGuestLectureApiResponse.postValue(ApiResource.success(data = guestLectureRepository.deleteGuestLecture(guestLectureId)))
            } catch (exception: IOException) {
                deleteGuestLectureApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: HttpException) {
                deleteGuestLectureApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    companion object guestLectureStatus {
        private val TAG = "guestLectureStatus"
        val calendar = Calendar.getInstance()

        @JvmStatic
        @BindingAdapter(value = ["isVerified", "isRejected"], requireAll = true)
        fun TextView.setGuestLectureStatus(isVerified: Int, isRejected : Int) {
            if(isRejected == 1){ // rejected
                this.text = resources.getString(R.string.status_rejected)
                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_rejected_circle,0,0,0)
            }else{
                if(isVerified == 0){ // Sent For Approval
                    this.text = SENT_FOR_APPROVAL
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_sent_for_approval_circle,0,0,0)
                }else{ // Approved
                    this.text = resources.getString(R.string.approved)
                    this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.status_approved_circle,0,0,0)
                }
            }
        }
    }
}