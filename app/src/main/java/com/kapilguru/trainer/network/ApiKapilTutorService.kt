package com.kapilguru.trainer.network

import com.kapilguru.student.courseDetails.model.BatchRequest
import com.kapilguru.student.courseDetails.model.ContactTrainerResponseAPi
import com.kapilguru.student.courseDetails.review.model.StudentReviewResponse
import com.kapilguru.trainer.addStudent.*
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.BestTrainerResponse
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.CourseBestTrainerMapRequest
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.CourseBestTrainerMapResponce
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsResponse
import com.kapilguru.trainer.allSubscription.models.UpdateKycRequest
import com.kapilguru.trainer.allSubscription.mySubscriptions.model.MySubscriptionResponse
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapRequest
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapResponse
import com.kapilguru.trainer.allSubscription.positionSubscription.model.TrainerCoursePositionDetailsRespose
import com.kapilguru.trainer.announcement.inbox.data.InboxResponse
import com.kapilguru.trainer.announcement.newMessage.data.*
import com.kapilguru.trainer.announcement.sentItems.data.LastMessageRequest
import com.kapilguru.trainer.announcement.sentItems.data.SentItemsResponse
import com.kapilguru.trainer.batchExamReports.BatchReportsRequestModel
import com.kapilguru.trainer.batchExamReports.BatchStudentReportApi
import com.kapilguru.trainer.coupons.*
import com.kapilguru.trainer.demo_webinar_students.DemoWebinarStudentsResponse
import com.kapilguru.trainer.enquiries.addOfflineEnquiry.data.AddEnquiryReq
import com.kapilguru.trainer.enquiries.addOfflineEnquiry.data.AddEnquiryRes
import com.kapilguru.trainer.enquiries.kapilGuruEnquiries.data.EnquiriesResponse
import com.kapilguru.trainer.exams.assignExamToBatch.model.AssignExamToBatchRequest
import com.kapilguru.trainer.exams.assignExamToBatch.model.BatchByCourseResponse
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaperTitleRequest
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionsListResponse
import com.kapilguru.trainer.exams.createQuestion.model.AddQuestionRequest
import com.kapilguru.trainer.exams.createQuestion.model.UpdateQuestionRequest
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.CopyFromQuesPaperRequest
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.PreviousQuestionPapersListResponse
import com.kapilguru.trainer.exams.previousQuestionsList.model.AddExistingQuesApiRequest
import com.kapilguru.trainer.exams.previousQuestionsList.model.PreviousQuestionsListResponse
import com.kapilguru.trainer.exams.scheduledExams.ScheduledExamsAPI
import com.kapilguru.trainer.faculty.*
import com.kapilguru.trainer.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.trainer.forgotPassword.model.ChangePasswordResponse
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileResponse
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionResponse
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.SendBatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentsRequest
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentsResponse
import com.kapilguru.trainer.myClassroom.liveClasses.model.LiveUpComingClassResponse
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.payment.model.InitiateTransactionResponse
import com.kapilguru.trainer.payment.model.TransactionStatusResponse
import com.kapilguru.trainer.referandearn.ReferAndEarnRequest
import com.kapilguru.trainer.referandearn.myReferrals.model.MyReferralResponse
import com.kapilguru.trainer.signup.model.register.RegisterRequest
import com.kapilguru.trainer.signup.model.register.RegisterResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOptSmsResponse
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailResponse
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpRequest
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpResponse
import com.kapilguru.trainer.student.announcement.inbox.data.StudentInboxResponse
import com.kapilguru.trainer.student.announcement.inbox.data.StudentLastMessageRequest
import com.kapilguru.trainer.student.announcement.newMessage.data.*
import com.kapilguru.trainer.student.announcement.sentItems.data.StudentSentItemsResponse
import com.kapilguru.trainer.student.exam.model.*
import com.kapilguru.trainer.student.homeActivity.models.*
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListRequest
import com.kapilguru.trainer.student.myClassRoomDetails.exam.model.StudentQuestionPaperListResponse
import com.kapilguru.trainer.student.myClassRoomDetails.model.*
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentAllClassesResponse
import com.kapilguru.trainer.student.profile.data.SaveStudentProfileResponse
import com.kapilguru.trainer.student.profile.data.StudentProfileData
import com.kapilguru.trainer.student.profile.data.StudentProfileResponse
import com.kapilguru.trainer.studentExamBatchResult.BatchExamStudentResultApi
import com.kapilguru.trainer.studentExamBatchResult.StudentAnswerSheetApi
import com.kapilguru.trainer.studentExamBatchResult.StudentExamPaperRequest
import com.kapilguru.trainer.studentExamBatchResult.StudentReportRequest
import com.kapilguru.trainer.studentsList.model.AllStudentsListPerTrainerApi
import com.kapilguru.trainer.studentsList.model.RequestRaiseComplaint

import com.kapilguru.trainer.studyMaterial.StudyMaterialListResponse
import com.kapilguru.trainer.studyMaterial.StudyMatrialListRequest
import com.kapilguru.trainer.studyMaterial.fileStructure.FolderContentRequest
import com.kapilguru.trainer.studyMaterial.fileStructure.FolderContentResponse
import com.kapilguru.trainer.studyMaterial.studyMaterialOverview.StudyMaterialOverViewResponse
import com.kapilguru.trainer.studyMaterial.studyMaterialOverview.StudyMatrialOverViewRequest

import com.kapilguru.trainer.testimonials.FetchTestimonialsResponse
import com.kapilguru.trainer.testimonials.PostTestimonialsModel
import com.kapilguru.trainer.testimonials.PostTestimonialsResponse
import com.kapilguru.trainer.testimonials.TestimonialApproveRequest
import com.kapilguru.trainer.trainerGallery.DeleteImageResponse
import com.kapilguru.trainer.trainerGallery.TrainerGalleryImagesResponse
import com.kapilguru.trainer.trainerGallery.UploadImageGallery
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest
import com.kapilguru.trainer.ui.changePassword.model.LogoutResponse
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchApiResponse
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchRequest
import com.kapilguru.trainer.ui.courses.add_batch.models.EditBatchApiRequest
import com.kapilguru.trainer.ui.courses.add_batch.models.EditBatchResponse
import com.kapilguru.trainer.ui.courses.addcourse.models.*
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.models.BatchSyllabusAPI
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.models.BatchStudentListApi
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApi
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest
import com.kapilguru.trainer.ui.courses.courses_list.models.CourseApi
import com.kapilguru.trainer.ui.courses.onGoingBatches.models.OnGoingBatchApi
import com.kapilguru.trainer.ui.courses.tax.TaxCalculationResponse
import com.kapilguru.trainer.ui.courses.view_course.ContactTrainerRequest
import com.kapilguru.trainer.ui.courses.view_course.CourseDetailsResponse
import com.kapilguru.trainer.ui.courses.view_course.CourseSyllabusResponse
import com.kapilguru.trainer.ui.earnings.history.model.EarningsHistoryResponseApi
import com.kapilguru.trainer.ui.earnings.history.model.HistoryPaymentAmountDetailsApi
import com.kapilguru.trainer.ui.earnings.model.EarningsDetailsApiResponse
import com.kapilguru.trainer.ui.earnings.model.EarningsListResponse
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureRequest
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureResponse
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageResponce
import com.kapilguru.trainer.ui.guestLectures.guestLectureStudent.model.GuestLectureStudentViewRes
import com.kapilguru.trainer.ui.guestLectures.model.EditGuestLectureResponse
import com.kapilguru.trainer.ui.guestLectures.model.GuestLectureResponse
import com.kapilguru.trainer.ui.home.NotificationCountResponse
import com.kapilguru.trainer.ui.home.UpComingScheduleResponse
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginValidateRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginValidateResponse
import com.kapilguru.trainer.ui.profile.bank.data.BankDetailsFetchResponce
import com.kapilguru.trainer.ui.profile.bank.data.BankDetailsUploadRequest
import com.kapilguru.trainer.ui.profile.bank.data.BankDetailsUploadResponce
import com.kapilguru.trainer.ui.profile.data.*
import com.kapilguru.trainer.ui.profile.profileInfo.models.CityResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.CountryResponce
import com.kapilguru.trainer.ui.profile.profileInfo.models.StateResponce
import com.kapilguru.trainer.ui.refund.model.RefundResponse
import com.kapilguru.trainer.ui.webiner.addWebinar.model.AddWebinarDetailsResponse
import com.kapilguru.trainer.ui.webiner.addWebinar.model.AddWebinarRequest
import com.kapilguru.trainer.ui.webiner.model.LiveUpComingWebinarResponse
import com.kapilguru.trainer.ui.webiner.model.WebinarResponse
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiKapilTutorService {

    @POST("user/login")
    suspend fun userLogin(@Body loginUserRequest: LoginUserRequest): LoginResponse

    @GET("trainer/trainerCourses/{trainerId}")
    suspend fun coursesList(@Path("trainerId") trainerId: String): CourseApi

    @DELETE("trainer/courses/{trainerId}")
    suspend fun deleteCourse(@Path("trainerId") trainerId: String): CommonResponseApi

    @POST("trainer/batches")
    suspend fun batchList(@Body batchListApiRequest: BatchListApiRequest): BatchListApi

    @DELETE("trainer/courses_batch/{batchId}")
    suspend fun deleteBatch(@Path("batchId") batchId: String): CommonResponseApi

    @GET("trainer/courses/{courseId}")
    suspend fun batchSyllabusInfo(@Path("courseId") courseId: String?): BatchSyllabusAPI

//    @GET("trainer/trainerCoursesStudentsDetails/{trainerId}")
//    suspend fun getTrainerStudents(@Path("trainerId") trainerId: String): StudentListApi

    @GET("trainer/getTrainerStudentDetails/{trainerId}")
    suspend fun getTrainerStudents(@Path("trainerId") trainerId: String): AllStudentsListPerTrainerApi

    @GET("trainer/getCourseStudentDetails/{courseId}")
    suspend fun getTrainerCourseStudentList(@Path("courseId") courseId: String): AllStudentsListPerTrainerApi

    @GET("trainer/getBatchStudentDetails/{batchId}")
    suspend fun fetchStudentListForBatchApi(@Path("batchId") courseId: String): AllStudentsListPerTrainerApi

    @GET("trainer/registeredWebinarParticipantsList/{webinarId}")
    suspend fun getWebinarStudentsList(@Path("webinarId") webinarId: String): AllStudentsListPerTrainerApi

    @POST("trainer/user_complaints")
    suspend fun postComplaint(@Body requestRaiseComplaint: RequestRaiseComplaint): SaveProfileResponse

    @POST("trainer/addCourse")
    suspend fun addCourseInfo(@Body addCourseRequest: AddCourseRequest): AddCourseResponse

    @GET("trainer/course_category")
    suspend fun getCategoryList(): CategoryApiResponse

    @POST("trainer/courses_batch")
    suspend fun addBatchInfo(@Body addBatchRequest: AddBatchRequest): AddBatchApiResponse

    @PUT("trainer/updateBatch/{batchId}")
    suspend fun updatedateBatch(@Path("batchId") batchId: Int, @Body addBatchRequest: AddBatchRequest): EditBatchResponse

    @GET("trainer/courses_batch/{batchId}")
    suspend fun getEditBatchInfo(@Path("batchId") batchId: Int): EditBatchApiRequest

    @GET("trainer/trainerCoursesBatchesDetails/{trainerId}")
    suspend fun getOnGoingBatchesList(@Path("trainerId") trainerId: String): OnGoingBatchApi

    @GET("trainer/getBatchStudentDetails/{batchId}")
    suspend fun batchStudentList(@Path("batchId") batchId: String): BatchStudentListApi

    @GET("trainer/countries")
    suspend fun countriesList(): CountryResponce

    @GET("student/countries")
    suspend fun studentCountriesList(): CountryResponce

    @GET("trainer/getStatesbyCountryId/{countryId}")
    suspend fun stateList(@Path("countryId") countryId: Int): StateResponce

    @GET("public/getStatesbyCountryId/{countryId}")
    suspend fun studentStateList(@Path("countryId") countryId: Int): StateResponce

    @GET("trainer/getCitiesbyStateId/{stateId}")
    suspend fun cityList(@Path("stateId") stateId: Int): CityResponce

    @GET("public/getCitiesbyStateId/{stateId}")
    suspend fun studentCityList(@Path("stateId") stateId: Int): CityResponce

    @GET("trainer/getTrainerProfileDetails/{id}")
    suspend fun getProfileData(@Path("id") userId: String): ProfileResponse

    @GET("trainer/getTrainerProfileDetails/{id}")
    fun refreshToken(@Path("id") userId: String): Call

    @POST("trainer/trainers")
    suspend fun saveProfile(@Body profileDataRequest: ProfileData): SaveProfileResponse

    @PUT("trainer/updateTrainer/{id}")
    suspend fun updateProfile(@Path("id") userId: String, @Body profileDataRequest: ProfileData): SaveProfileResponse

    @PUT("trainer/courses/{id}")
    suspend fun updateCourse(@Path("id") courseId: String, @Body addCourseRequest: AddCourseRequest): UpdateCourseApi


    @GET("trainer/courses/{id}")
    suspend fun getCourse(@Path("id") courseId: String): GetCourseResponse

    @GET("trainer/activeBatches/{id}")
    suspend fun getBatchList(@Path("id") userId: String): NewMessageResponse

    @GET("trainer/admin")
    suspend fun getAdminList(): GetAdminListRes

    @POST("trainer/announcements")
    suspend fun postBatchNewMessageReq(@Body batchMessageRequest: SendBatchMessageRequest): SendNewMessageResponce

    @POST("trainer/announcements_user_map")
    suspend fun postAdminNewMessageReq(@Body sendAdminMessageReq: SendAdminMessageRequest): SendNewMessageResponce

    @GET("trainer/receivedMessages/{id}")
    suspend fun getInBoxResponce(@Path("id") userId: String): InboxResponse

    @GET("trainer/sentMessages/{id}")
    suspend fun getSentItemsResponce(@Path("id") userId: String): SentItemsResponse

    @PUT("trainer/users/{trainerId}")
    suspend fun updateLastMessageId(@Path("trainerId") trainerId: String, @Body lastMessageRequest: LastMessageRequest): CommonResponse

    @POST("sms/sendOtpSms")
    suspend fun generateOTP(@Body getOtpRequest: GetOTPRequest): GetOTPResponce

    @POST("user/checkOtp")
    suspend fun checkOTP(@Body checkOTPRequest: CheckOTPRequest): CheckOTPResponce

    @GET("trainer/getTrainerStudentPaymentDetails/{trainerId}")
    suspend fun refundList(@Path("trainerId") trainerId: String): RefundResponse

    @GET("trainer/webinars/trainer_id/{trainerId}")
    suspend fun webinarList(@Path("trainerId") trainerId: String): WebinarResponse

    @GET("trainer/liveUpcomingWebinars/{trainerId}")
    suspend fun liveUpComingWebinarList(@Path("trainerId") trainerId: String): LiveUpComingWebinarResponse

    @GET("trainer/user_bank_details/user_id/{id}")
    suspend fun getBankDetails(@Path("id") userId: String): BankDetailsFetchResponce

    @PUT("trainer/user_bank_details/{id}")
    suspend fun updateBankDetails(@Path("id") bankId: String, @Body bankDetailsUploadReq: BankDetailsUploadRequest): BankDetailsUploadResponce

    @POST("trainer/user_bank_details")
    suspend fun saveBankDetails(@Body bankDetailsUploadReq: BankDetailsUploadRequest): BankDetailsUploadResponce

    @GET("trainer/guest_lectures/trainer_id/{trainerId}")
    suspend fun guestLectureList(@Path("trainerId") trainerId: String): GuestLectureResponse

    @GET("trainer/liveUpcomingLectures/{trainerId}")
    suspend fun liveUpComingGuestLectureList(@Path("trainerId") trainerId: String): GuestLectureResponse

    @GET("trainer/course_languages")
    suspend fun courseLanguages(): LanguageResponce

    @PUT("trainer/guest_lectures/{id}") // here id is insertId for first update, and guestLecture id for edit
    suspend fun updateGuestLecture(@Path("id") insertId: String, @Body addGuestLectureRequest: AddGuestLectureRequest): CommonResponseApi

    @POST("trainer/addWebinar")
    suspend fun addWebinarDetails(@Body addWebinarRequest: AddWebinarRequest): AddWebinarDetailsResponse

    @PUT("trainer/webinars/{id}")  // here id is insertId for first update, and webinar id for edit
    suspend fun updateWebinar(@Path("id") insertId: String, @Body addWebinarRequest: AddWebinarRequest): CommonResponseApi

    @PUT("trainer/guest_lectures/{id}")
    suspend fun updateEditGuestLecture(@Path("id") updateId: String, @Body guestLectureReq: AddGuestLectureRequest): EditGuestLectureResponse

    @POST("user/validateMobile")
    suspend fun validateMobile(@Body validateMobileRequest: ValidateMobileRequest): ValidateMobileResponse

    @POST("user/changePassword")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse

    @GET("trainer/webinars/{id}")
    suspend fun getEditWebinarDetials(@Path("id") webinarId: String): WebinarResponse

    @GET("trainer/course_languages")
    suspend fun getwebinarLanguages(): LanguageResponce

    @DELETE("trainer/webinars/{id}")
    suspend fun deleteWebinarItem(@Path("id") webinarId: String): CommonResponseApi

    @GET("trainer/webinarDetails/{webinarId}")
    suspend fun fetchWebinarDetails(@Path("webinarId") webinarId: String): WebinarResponse

    @PUT("trainer/webinars/{id}")
    suspend fun updateWebinarDetailsAfterEdit(@Path("id") webinarId: String, @Body addWebinarRequest: AddWebinarRequest): CommonResponseApi

    @DELETE("trainer/guest_lectures/{id}")
    suspend fun deleteGuestLecture(@Path("id") guestLectureDeleteId: String): CommonResponseApi

    @GET("trainer/allSubscriptions")
    suspend fun getAllSubscriptions(): AllSubscriptionsResponse

    @GET("trainer/trainerCoursePositionsDetails/{id}")
    suspend fun getTrainerCourseData(@Path("id") id: String): TrainerCoursePositionDetailsRespose

    @GET("trainer/trainerCoursePositionsDetails/{id}")
    suspend fun getBestTrainerCourseList(@Path("id") id: String): BestTrainerResponse

    @GET("trainer/getEarnings/{trainerId}")
    suspend fun earningsList(@Path("trainerId") trainerId: String): EarningsListResponse

    @GET("trainer/getEarningsDetails/{trainerId}")
    suspend fun earningsDetailsList(@Path("trainerId") trainerId: String): EarningsDetailsApiResponse

    @GET("trainer/subscriptionDetails/{trainerId}")
    suspend fun getMySubscriptions(@Path("trainerId") trainerId: String): MySubscriptionResponse

    @POST("trainer/course_positions_map")
    suspend fun mapCoursePosition(@Body coursePositionMapReq: CoursePositionMapRequest): CoursePositionMapResponse

    @POST("trainer/trainer/course_badges_map ")

    suspend fun mapCourseBestTrainer(@Body courseBestTrainerMapRequest: CourseBestTrainerMapRequest): CourseBestTrainerMapResponce

    @POST("trainer/referrals")
    suspend fun postReferAndEarn(@Body referAndEarnRequest: ReferAndEarnRequest): SaveProfileResponse

    @POST("user/validateEmail")
    suspend fun validateMail(@Body validateMail: ValidateMailRequest): ValidateMailResponse

    @POST("user/validateOtp")
    suspend fun validateOtp(@Body validateOtpReq: ValidateOtpRequest): ValidateOtpResponse

    @POST("user/register")
    suspend fun registerAccount(@Body registerAccount: RegisterRequest): RegisterResponse

    @POST("sms/register")
    suspend fun sendOtpMessage(@Body sendOtpSmsRequest: SendOtpSmsRequest): SendOptSmsResponse

    @GET("public/countries")
    suspend fun fetchcountriesListForSignUp(): CountryResponce

    @GET("trainer/getRequestedDetails/{trainerId}")
    suspend fun getEarningsHistory(@Path("trainerId") trainerId: String): EarningsHistoryResponseApi

    @GET("trainer/getRequestedAmountHistoryDetails/{trainerId}/{selectedId}")
    suspend fun getHistoryDetailAmount(@Path("trainerId") trainerId: String, @Path("selectedId") selectedId: String): HistoryPaymentAmountDetailsApi

    @GET("trainer/getRequestedAmountHistoryDetails/{trainerId}/{selectedId}")
    suspend fun getHistoryDetailAmount1(@Path("trainerId") trainerId: String, @Path("selectedId") selectedId: String): HistoryPaymentAmountDetailsApi

    @POST("user/otpLoginValidate")
    suspend fun validateOTPLogin(@Body otpLoginValidateReq: OTPLoginValidateRequest): OTPLoginValidateResponse

    @POST("sms/otpLoginValidate")
    suspend fun requestOTP(@Body otpLoginRequest: OTPLoginValidateRequest): OTPLoginValidateResponse

    @POST("user/otpLogin")
    suspend fun otpLogin(@Body otpLoginRequest: OTPLoginRequest): LoginResponse

    @POST("user/logout")
    suspend fun logoutUser(@Body logoutRequest: LogoutRequest): LogoutResponse

    @GET("user/getUserMessagesStatus/{userId}")
    suspend fun getNotificationCount(@Path("userId") userId: String): NotificationCountResponse

    @GET("trainer/liveUpcomingClasses/{trainerId}")
    suspend fun getLiveUpComingClasses(@Path("trainerId") trainerId: String): LiveUpComingClassResponse

    @POST("trainer/getBcrTrainer")
    suspend fun getBatchCompletionResponse(@Body batchCompletionReq: BatchCompletionRequest): BatchCompletionResponse

    @POST("trainer/batch_completion_request")
    suspend fun sendBatchCompletionRequest(@Body sendBatchCompletionRequest: SendBatchCompletionRequest): AddBatchApiResponse

    @POST("trainer/exam_question_paper")
    suspend fun sendQuestionPaperTitle(@Body questionPaperTitleRequest: QuestionPaperTitleRequest): AddBatchApiResponse

    @POST("trainer/addQuestion")
    suspend fun addQuestion(@Body addQuestionRequest: AddQuestionRequest): AddBatchApiResponse

    @PUT("trainer/exam_questions_list/{questionId}")
    suspend fun updateQuestion(@Path("questionId") questionId: String, @Body updateQuestionRequest: UpdateQuestionRequest): AddBatchApiResponse

    @GET("trainer/getPreviousQuestionsPapersList/{trainerId}/{courseId}")
    suspend fun getPreviousQuestionPapersList(@Path("trainerId") trainerId: String, @Path("courseId") courseId: String): PreviousQuestionPapersListResponse

    @GET("trainer/getQuestionsList/{trainerId}")
    suspend fun getQuestionsList(@Path("trainerId") trainerId: String): QuestionsListResponse

    @GET("trainer/examByTrainer/{trainerId}")
    suspend fun scheduledExamsList(@Path("trainerId") trainerId: String): ScheduledExamsAPI

    @POST("trainer/batchStudentReport")
    suspend fun batchStudentReport(@Body batchReportsRequestModel: BatchReportsRequestModel): BatchStudentReportApi

    @POST("student/studentReport")
    suspend fun getStudentReport(@Body studentReportRequest: StudentReportRequest): BatchExamStudentResultApi

    @POST("trainer/getStudentResponses")
    suspend fun getAnswerSheet(@Body studentReportRequest: StudentExamPaperRequest): StudentAnswerSheetApi

    @GET("trainer/getPreviousQuestionsList/{trainerId}/{courseId}")
    suspend fun getPreviousQuestionsList(@Path("trainerId") trainerId: String, @Path("courseId") courseId: String): PreviousQuestionsListResponse

    @POST("image/saveImage")
    suspend fun uploadImageCourse(@Body uploadImageCourse: UploadImageCourse): UploadImageCourseResponse

    @Multipart
    @POST("videos/upload")
    suspend fun uploadVideo(
        @Part files: MultipartBody.Part,
        @Part("trainer_id") trainerId: RequestBody,
        @Part("source_id") sourceId: RequestBody,
        @Part("code") code: RequestBody,
        @Part("video_type") videoType: RequestBody
    ): UploadVideoResponse

    @Multipart
    @POST("courseFiles/upload")
    suspend fun uploadCoursePdfFile(
        @Part files: MultipartBody.Part, @Part("trainer_id") trainerId: RequestBody, @Part("course_id") courseId: RequestBody, @Part("title") title: RequestBody
    ): CommonResponseApi

    @Multipart
    @POST("batchDocuments/upload")
    suspend fun batchCoursePdfFileUpload(
        @Part files: MultipartBody.Part,
        @Part("trainer_id") trainerId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("course_id") courseId: RequestBody,
        @Part("batch_id") batch_id: RequestBody
    ): CommonResponseApi


    @GET("batchDocuments/files/{fileName}")
    suspend fun downloadPdfFile(@Path("fileName") fileName: String)

    @POST("trainer/getBatchDcuments")
    suspend fun getBatchDocuments(@Body batchDocumentsRequest: BatchDocumentsRequest): BatchDocumentsResponse

    @GET("trainer/getUploadedPdf/{pdfId}")
    suspend fun getCoursePdfFile(@Path("pdfId") pdfId: String): CoursePdfResponse

    @POST("payment/initiateTransaction")
    suspend fun callInitiationTransactionApi(@Body initiateTransReq: InitiateTransactionRequest): InitiateTransactionResponse

    @POST("payment/transactionStatus")
    suspend fun callTransactionStatusApi(@Body initiateTransReq: InitiateTransactionRequest): TransactionStatusResponse

    @POST("trainer/copyFromQP")
    suspend fun copyFromQuestionPaper(@Body copyFromQuestionPaperRequest: CopyFromQuesPaperRequest): QuestionsListResponse

    @POST("trainer/addExistingQuestions")
    suspend fun addExistingQuesApiRequest(@Body addExistingQuesApiRequest: AddExistingQuesApiRequest): AddBatchApiResponse

    @GET("trainer/activeBatchesByCourse/{trainerId}/{courseId}")
    suspend fun getActiveBatchesByCourse(@Path("trainerId") trainerId: String, @Path("courseId") courseId: String): BatchByCourseResponse

    @POST("trainer/assignQPToBatch")
    suspend fun assignExamToBatch(@Body assignExamToBatchRequest: AssignExamToBatchRequest): AddBatchApiResponse

    /*Guest Lecture*/
    @POST("trainer/addLecture")
    suspend fun addGuestLectureDetails(@Body guestLectureReq: AddGuestLectureRequest): AddGuestLectureResponse

    @GET("trainer/registeredWebinarParticipantsList/{webinarId}")
    suspend fun getDemoWebinarStudents(@Path("webinarId") webinarId: String): DemoWebinarStudentsResponse

    @GET("trainer/registeredDLParticipantsList/{webinarId}")
    suspend fun getDemoStudentListApi(@Path("webinarId") webinarId: String): DemoWebinarStudentsResponse

    @GET("trainer/trainerUpcomingSchedule/{trainerId}")
    suspend fun fetchUpcomingSchedule(@Path("trainerId") trainerId: String): UpComingScheduleResponse

    @GET("trainer/batchDetails/{batchId}")
    suspend fun getBatchDetails(@Path("batchId") batchId: String): NewMessageResponse

    @GET("trainer/referrals/user_id/{trainerId}")
    suspend fun getMyReferrals(@Path("trainerId") trainerId: String): MyReferralResponse

    @PUT("trainer/updateKyc/{trainerId}")
    suspend fun updateKyc(@Path("trainerId") trainerId: String, @Body updateKycRequest: UpdateKycRequest): CommonResponseApi

    @GET("trainer/guest_lectures/{demoLectureId}")
    suspend fun getGuestLectureStudentViewDetails(@Path("demoLectureId") demoLectureId: String): GuestLectureStudentViewRes

    @Multipart
    @POST("userDocuments/upload")
    suspend fun uploadAadharPdfFile(@Part files: MultipartBody.Part, @Part("user_id") userId: RequestBody, @Part("title") title: RequestBody): CommonResponseApi

    @Multipart
    @POST("userDocuments/upload")
    suspend fun uploadPanPdfFile(@Part files: MultipartBody.Part, @Part("user_id") userId: RequestBody, @Part("title") title: RequestBody): CommonResponseApi

    @GET("public/allCourseDetails/{courseId}")
    suspend fun fetchCourseDetails(@Path("courseId") courseId: String): CourseDetailsResponse

    @GET("public/getUploadedPdf/{syllabusAttachmentId}")
    suspend fun fetchSyllabusAttachments(@Path("syllabusAttachmentId") syllabusAttachmentId: String): CourseSyllabusResponse

    @POST("public/batch_request")
    suspend fun requestBatch(@Body batchRequest: BatchRequest): CommonResponse

    @GET("public/studentReviews/{studentId}")
    suspend fun getStudentReviews(@Path("studentId") studentId: String): StudentReviewResponse

//    @PUT("student/updateReview")
//    suspend fun writeUpdateReview(@Body writeReviewRequest: WriteReviewRequest): CommonResponse

    @POST("public/student_enquiry_to_trainer")
    suspend fun contactTrainer(@Body contactTrainerRequest: ContactTrainerRequest): ContactTrainerResponseAPi


    @GET("public/allWebinars")
    suspend fun fetchAllWebinars(): AllWebinarsResponse

    @GET("public/allDemoLectures")
    suspend fun fetchAllDemos(): AllDemosResponse

    @GET("public/getVerifiedJobOpenings")
    suspend fun getAllJobOpenings(): JobOpeningsResponse

    @GET("public/popularTrendingCourses?q=home")
    suspend fun getDashBoardPopularAndTrendingCourses(): PopularAndTrendingResponse

    @GET("public/popularTrendingCourses")
    suspend fun getAllPopularAndTrendingCourses(): PopularAndTrendingResponse

    @POST("leads/createLead")
    suspend fun createLeadApi(@Body createLeadRequest: CreateLeadRequest): CommonResponse

    @POST("image/uploadGalleryImage")
    suspend fun uploadTrainerGalleryImages(@Body uploadImageCourse: UploadImageGallery): UploadImageCourseResponse

    @GET("image/getImagesList/{packageId}")
    suspend fun getTrainerGalleryImages(@Path("packageId") packageId: String): TrainerGalleryImagesResponse

    @POST("trainer/app_testimonials")
    suspend fun addtestimonials(@Body addTrainerTestimonial: PostTestimonialsModel): PostTestimonialsResponse

    @GET("trainer/app_testimonials/tenant_id/{tenantId}")
    suspend fun getTestimonials(@Path("tenantId") tenantId: Int): FetchTestimonialsResponse

    @PUT("trainer/app_testimonials/{id}")
    suspend fun updateTestimonialStatus(@Path("id") id: String, @Body testimonialApproveRequest: TestimonialApproveRequest): CommonResponseApi

    @DELETE("trainer/app_testimonials/{id}")
    suspend fun deleteTestimonial(@Path("id") id: String): CommonResponseApi

    @GET("trainer/tax_charges")
    suspend fun getTaxes(): TaxCalculationResponse


    @POST("trainer/getStudyMaterials")
    suspend fun getListOfStudyMaterials(@Body studyMaterialListRequest: StudyMatrialListRequest): StudyMaterialListResponse

//    @POST("/trainer/getStudyMaterials")
//    suspend fun getStudyMaterialList(studyMaterialListRequest: StudyMaterialListRequest) = apiKapilTutorService.getStudyMaterialList(studyMaterialListRequest)

    @GET("trainer/kg_coupons/{trainerId}")
    suspend fun getCouponsList(@Path("trainerId") trainerId: Int): AllCouponsResponseList

    //Student Profile
    @GET("student/getStudentProfileDetails/{id}")
    suspend fun getStudentProfileData(@Path("id") userId: String): StudentProfileResponse

    @PUT("student/updateStudent/{id}")
    suspend fun updateStudentProfile(@Path("id") userId: String, @Body studentProfileDataRequest: StudentProfileData): SaveStudentProfileResponse

    @GET("student/user_bank_details/user_id/{id}")
    suspend fun getStudentBankDetails(@Path("id") userId: String): BankDetailsFetchResponce

    @PUT("student/user_bank_details/{id}")
    suspend fun updateStudentBankDetails(@Path("id") bankId: String, @Body bankDetailsUploadReq: BankDetailsUploadRequest): BankDetailsUploadResponce

    @POST("student/user_bank_details")
    suspend fun saveStudentBankDetails(@Body bankDetailsUploadReq: BankDetailsUploadRequest): BankDetailsUploadResponce

    @GET("student/getStudentsClassroom/{userId}")
    suspend fun getAllStudentClasses(@Path("userId") userId: String): StudentAllClassesResponse

    @GET("student/batchDetails/{batchId}")
    suspend fun getStudentBatchDetails(@Path("batchId") batchId: String): StudentBatchDetailsResponse

    @POST("student/user_complaints")
    suspend fun raiseComplaintByStudent(@Body raiseComplaintByStudentRequest: RaiseComplaintByStudentRequest): RaiseComplaintByStudentResponse

    @POST("student/requestRefund")
    suspend fun requestRefundByStudent(@Body refundStudentRequest: RefundStudentRequest): RefundStudentResponse

    @PUT("student/updateReview")
    suspend fun updateReviewByStudent(@Body reviewStudentRequest: ReviewStudentRequest): ReviewStudentResponse

    @POST("student/questionPaperList")
    suspend fun studentQuestionPaperList(@Body studentQuestionPaperListRequest: StudentQuestionPaperListRequest): StudentQuestionPaperListResponse

    @GET("student/getStudentsBatchDcuments/{batchId}")
    suspend fun getStudentStudyMaterial(@Path("batchId") batchId: String): StudentStudyMaterialResponse

    @POST("student/getQuestions")
    suspend fun getStudentQuestions(@Body studentQuestionsRequest: StudentQuestionsRequest): StudentQuestionsReponse

    @POST("student/submitResponse")
    suspend fun submitStudentQuestion(@Body studentSubmitQuestionRequest: StudentSubmitQuestionRequest): CommonResponse

    @POST("student/submitFinalResponse")
    suspend fun submitStudentAllQuestion(@Body studentSubmitAllQuestionsRequest: StudentSubmitAllQuestionsRequest): CommonResponse

    @POST("student/studentReport")
    suspend fun getStudentReportByStudent(@Body studentReportRequest: StudentReportRequest): StudentReportResponse

    @GET("student/allQuestionPaperList/{batchId}")
    suspend fun getStudentExamList(@Path("batchId") batchId: String): StudentQuestionPaperListResponse

    @POST("trainer/kg_coupons")
    suspend fun addCoupon(@Body addCouponsRequest: AddCouponsRequest): AddCouponResponse

    @POST("trainer/getStudyMaterials")
    suspend fun getCategoryCourse(@Body couponCourseCategoryRequest: CouponCourseCategoryRequest): CouponLiveCoursesResponse

    @POST("trainer/getStudyMaterialsOverview")
    suspend fun getStudyMaterialOverView(@Body studyMatrialOverViewRequest: StudyMatrialOverViewRequest): StudyMaterialOverViewResponse

    @POST("trainer/getFolderContent")
    suspend fun getFolderContent(@Body folderContentRequest: FolderContentRequest): FolderContentResponse


    @DELETE("image/deleteFile/{code}/{imageName}")
    suspend fun deleteTrainerGalleryImages(@Path("code") code: String, @Path("imageName") imageName: String): DeleteImageResponse


    @POST("trainer/trainerAppStudents")
    suspend fun getStudentList(@Body studentRequestModel: StudentRequestModel): ResponseStudentModel

    @POST("trainer/kg_coupons")
    suspend fun createCouponCode(@Body createCouponCodeRequestModel: CreateCouponCodeRequestModel): CreateCouponResponse

    @POST("trainer/addFaculty")
    suspend fun addFaculty(@Body addFacultyRequest: AddFacultyRequest): AddFacultyResponse

    @POST("trainer/getFacultyList")
    suspend fun getFaculty(@Body getFacultyRequest: GetFacultyRequest) : FacultyListResponse

    @PUT("trainer/trainer_faculty_map/{id}")
    suspend fun updateFaculty(@Path("id") id: String, @Body facultySettingsModel: FacultySettingsModel) : FacultySettingsResponse

    @POST("trainer/checkStudentBeforeAdd")
    suspend fun checkStudent(@Body checkStudentRequest: CheckStudentRequest) : CheckStudentResponse

    @GET("student/getBatchTrainer/{id}")
    suspend fun getBatchListForStudent(@Path("id") userId: String): StudentNewMessageResponse

    @GET("student/admin")
    suspend fun getAdminListForStudent(): StudentAdminMessageResponse

    @POST("student/announcements")
    suspend fun postNewMessageReqByStudent(@Body newMessageRequest: StudentSendNewMessageRequest): StudentSendNewMessageResponce

    @POST("student/announcements_user_map")
    suspend fun postAdminNewMessageReqByStudent(@Body sendAdminMessageReq: StudentSendAdminMessageRequest): StudentSendNewMessageResponce

    @GET("student/receivedMessages/{id}")
    suspend fun getInBoxResponseForStudent(@Path("id") userId: String): StudentInboxResponse

    @GET("student/sentMessages/{id}")
    suspend fun getSentItemsResponseForStudent(@Path("id") userId: String): StudentSentItemsResponse

    @PUT("student/users/{userId}")
    suspend fun updateLastMessageIdForStudent(@Path("userId") userId: String, @Body studentLastMessageRequest: StudentLastMessageRequest): CommonResponse

    @GET("trainer/getEnquiries/{trainerId}")
    suspend fun getEnquiries(@Path("trainerId") userId: String): EnquiriesResponse

    @POST("trainer/student_enquiry_to_trainer")
    suspend fun addEnquiry(@Body addEnquiryRequest: AddEnquiryReq): AddEnquiryRes

    @POST("trainer/addStudentToBatch")
    suspend fun addOnlineStudent(@Body onlineStudentRequest: OnlineStudentRequest): AddOnlineStudentResponse

    @POST("trainer/offline_students")
    suspend fun addOfflineStudent(@Body addOfflineStudentRequest: AddOfflineStudentRequest) : AddOfflineStudentResponse
}