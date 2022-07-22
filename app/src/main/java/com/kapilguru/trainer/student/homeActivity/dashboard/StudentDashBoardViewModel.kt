package com.kapilguru.trainer.student.homeActivity.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.trainer.BuildConfig
import com.kapilguru.trainer.R
import com.kapilguru.trainer.network.ApiResource
import com.kapilguru.trainer.network.CommonResponse
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.student.homeActivity.liveCourses.model.LiveCourseResponse
import com.kapilguru.trainer.student.homeActivity.models.*
import com.kapilguru.trainer.student.homeActivity.studentGallery.model.ImageResponse
import com.kapilguru.trainer.student.homeActivity.studentTestimonials.model.StudentTestimonialResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StudentDashBoardViewModel(val homeScreenRepository:StudentHomeScreenRepository, val context: Application,) : AndroidViewModel(context) {

    lateinit var popularTrendingList: ArrayList<PopularAndTrendingApi>
    lateinit var trendingWebinarsList: ArrayList<AllWebinarsApi>
    lateinit var trendingDemosList: ArrayList<AllDemosApi>
    lateinit var trendingJobsList: ArrayList<JobOpeningsData>
    val listOfHomeTopItems: MutableLiveData<MutableList<DashBoardViewPagerItem>> = MutableLiveData()
    val listOfHomeItems: MutableLiveData<MutableList<StudentDashBoardItem>> = MutableLiveData()
    val listOfTabItems: MutableLiveData<MutableList<StudentDashBoardCustomTabModel>> = MutableLiveData()
    val allWebinarsData: MutableLiveData<ApiResource<AllWebinarsResponse>> = MutableLiveData()
    val allJobOpeningsData: MutableLiveData<ApiResource<JobOpeningsResponse>> = MutableLiveData()
    val popularAndTrendingResponse: MutableLiveData<ApiResource<PopularAndTrendingResponse>> = MutableLiveData()
    val liveCourseApiResponse : MutableLiveData<ApiResource<LiveCourseResponse>> = MutableLiveData()
    val alDemosData: MutableLiveData<ApiResource<AllDemosResponse>> = MutableLiveData()
    val whyKapilGuruData: MutableLiveData<MutableList<StudentDashBoardItem>> = MutableLiveData()
    val createLeadRequest: MutableLiveData<CreateLeadRequest> = MutableLiveData(CreateLeadRequest())
    val commonResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    val createLeadError: MutableLiveData<String> = MutableLiveData()
    val imagesListApiRes : MutableLiveData<ApiResource<ImageResponse>> = MutableLiveData()
    val studentTestimonialListApiRes : MutableLiveData<ApiResource<StudentTestimonialResponse>> = MutableLiveData()

    fun setHomeTopItems() {
        val dashBoardViewPagerItem = mutableListOf<DashBoardViewPagerItem>()
        dashBoardViewPagerItem.add(DashBoardViewPagerItem().apply {
            image = R.drawable.ic_live
            title = "Live\nCourses"
            backgroundcolor = R.color.home_top_bg1
        })
        dashBoardViewPagerItem.add(DashBoardViewPagerItem().apply {
            image = R.drawable.ic_recorded_classes
            title = "Recorded\nCourses"
            backgroundcolor = R.color.home_top_bg2
        })
        dashBoardViewPagerItem.add(DashBoardViewPagerItem().apply {
            image = R.drawable.ic_study_metrial
            title = "Study Materials"
            backgroundcolor = R.color.home_top_bg3
        })
        dashBoardViewPagerItem.add(DashBoardViewPagerItem().apply {
            image = R.drawable.ic_free_lecture
            title = "Free Lectures"
            backgroundcolor = R.color.home_top_bg3
        })
        listOfHomeTopItems.postValue(dashBoardViewPagerItem)
    }

    fun setHomeItems() {
        val homeItems = mutableListOf<StudentDashBoardItem>()

        homeItems.add(StudentDashBoardItem().apply {
            image = R.drawable.student_ic_course
            title = "My\nCourses"
        })

        homeItems.add(StudentDashBoardItem().apply {
            image = R.drawable.ic_subscriptions
            title = "My Free\nLectures"
        })

        homeItems.add(StudentDashBoardItem().apply {
            image = R.drawable.student_ic_webinar
            title = "My Collections"
        })

        homeItems.add(StudentDashBoardItem().apply {
            image = R.drawable.student_ic_completed_request
            title = "Certificates"
        })

        homeItems.add(StudentDashBoardItem().apply {
            image = R.drawable.student_ic_exams
            title = "Exams"
        })

        homeItems.add(StudentDashBoardItem().apply {
            image = R.drawable.student_ic_messages
            title = "Messages"
        })

        listOfHomeItems.postValue(homeItems)
    }

    fun setDashBoardTabsItems() {
        val tabsData = mutableListOf<StudentDashBoardCustomTabModel>()
//        tabsData.add(StudentDashBoardCustomTabModel().apply {
//            title = "TRENDING"
//            subTitle = "WEBINARS"
//            image = R.drawable.ic_girl_in_cirle_one
//        })

        tabsData.add(StudentDashBoardCustomTabModel().apply {
            title = "TRENDING"
            subTitle = "Free Lectures"
            image = R.drawable.ic_girl_in_circle_two
        })

//        tabsData.add(StudentDashBoardCustomTabModel().apply {
//            title = "VIEW & APPLY"
//            subTitle = "JOB OPENINGS"
//            image = R.drawable.ic_girl_in_cirle_one
//        })

        listOfTabItems.postValue(tabsData)
    }


    fun fetchAllWebinars() {
        val userId = StorePreferences(context).userId
        allWebinarsData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allWebinarsData.postValue(ApiResource.success(homeScreenRepository.fetchAllWebinars()))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                allWebinarsData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            } catch (exception: IOException) {
                allWebinarsData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                allWebinarsData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun fetchAllDemos() {
        val userId = StorePreferences(context).userId
        alDemosData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alDemosData.postValue(ApiResource.success(homeScreenRepository.fetchAllDemos()))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                alDemosData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            } catch (exception: IOException) {
                alDemosData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                alDemosData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun fetchAllJobOpenings() {
        allJobOpeningsData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allJobOpeningsData.postValue(ApiResource.success(homeScreenRepository.fetchAllJobOpenings()))
            }
            catch (exception: RetrofitNetwork.NetworkConnectionError) {
                allJobOpeningsData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            } catch (exception: IOException) {
                allJobOpeningsData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                allJobOpeningsData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun fetchAllPopularAndTrending() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                popularAndTrendingResponse.postValue(ApiResource.success(homeScreenRepository.getDashBoardPopularAndTrendingCourses()))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                popularAndTrendingResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            }catch (exception: IOException) {
                popularAndTrendingResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                popularAndTrendingResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getLiveCourses() {
        val parentTrainerId = StorePreferences(context).parentTrainerId.toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                liveCourseApiResponse.postValue(ApiResource.success(homeScreenRepository.getLiveCourses(parentTrainerId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                liveCourseApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            }catch (exception: IOException) {
                liveCourseApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                liveCourseApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun createLeadApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                commonResponse.postValue(ApiResource.success(homeScreenRepository.createLeadApi(createLeadRequest.value!!)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                commonResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: HttpException) {
                commonResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                commonResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun validateCreateLead(): Boolean {
        when {
            /* createLeadRequest.value!!.fullName.isNullOrBlank() -> {
                 createLeadError.value = "Please Enter Full  Name"
                 return false
             }
             !createLeadRequest.value!!.contactNumber!!.isValidMobileNo() -> {
                 createLeadError.value = "Please Enter Valid Mobile Number"
                 return false
             }
             createLeadRequest.value!!.emailId!!.emailValidation() -> {
                 createLeadError.value = "Please Enter Valid email "
                 return false
             }*/
            createLeadRequest.value!!.message.isNullOrBlank() -> {
                createLeadError.value = "Message Can't be Empty"
                return false
            }
            else -> {
                createLeadRequest.value!!.fullName = StorePreferences(context).userName
                createLeadRequest.value!!.contactNumber = StorePreferences(context).contactNumber
                createLeadRequest.value!!.emailId = StorePreferences(context).email
                return true
            }
        }
    }

    fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val packageId = BuildConfig.PACKAGE_ID
            try {
                imagesListApiRes.postValue(ApiResource.success(homeScreenRepository.getImages(packageId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                imagesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            }catch (exception: IOException) {
                imagesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                imagesListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getStudentTestimonials() {
        viewModelScope.launch(Dispatchers.IO) {
            val parentTenantId = StorePreferences(context).tenantId.toString()
            try {
                studentTestimonialListApiRes.postValue(ApiResource.success(homeScreenRepository.getStudentTestimonials(parentTenantId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                studentTestimonialListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            }catch (exception: IOException) {
                studentTestimonialListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                studentTestimonialListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}