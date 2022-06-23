package com.kapilguru.trainer

import com.kapilguru.student.courseDetails.model.BatchRequest
import com.kapilguru.trainer.allSubscription.bestTrainerSubscription.model.CourseBestTrainerMapRequest
import com.kapilguru.trainer.allSubscription.models.UpdateKycRequest
import com.kapilguru.trainer.allSubscription.positionSubscription.model.CoursePositionMapRequest
import com.kapilguru.trainer.announcement.newMessage.data.SendAdminMessageRequest
import com.kapilguru.trainer.announcement.newMessage.data.SendBatchMessageRequest
import com.kapilguru.trainer.announcement.sentItems.data.LastMessageRequest
import com.kapilguru.trainer.batchExamReports.BatchReportsRequestModel
import com.kapilguru.trainer.coupons.AddCouponsRequest
import com.kapilguru.trainer.coupons.CouponCourseCategoryRequest
import com.kapilguru.trainer.exams.assignExamToBatch.model.AssignExamToBatchRequest
import com.kapilguru.trainer.exams.conductExams.createQuestionPaper.model.QuestionPaperTitleRequest
import com.kapilguru.trainer.exams.createQuestion.model.AddQuestionRequest
import com.kapilguru.trainer.exams.createQuestion.model.UpdateQuestionRequest
import com.kapilguru.trainer.exams.previousQuestionPapersList.model.CopyFromQuesPaperRequest
import com.kapilguru.trainer.exams.previousQuestionsList.model.AddExistingQuesApiRequest
import com.kapilguru.trainer.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.trainer.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.trainer.login.models.LoginUserRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.BatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.completionRequest.model.SendBatchCompletionRequest
import com.kapilguru.trainer.myClassRoomDetails.studymaterial.model.BatchDocumentsRequest
import com.kapilguru.trainer.network.ApiKapilTutorService
import com.kapilguru.trainer.payment.model.InitiateTransactionRequest
import com.kapilguru.trainer.referandearn.ReferAndEarnRequest
import com.kapilguru.trainer.signup.model.register.RegisterRequest
import com.kapilguru.trainer.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.trainer.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.trainer.signup.model.validateOtp.ValidateOtpRequest
import com.kapilguru.trainer.student.homeActivity.models.CreateLeadRequest
import com.kapilguru.trainer.student.profile.data.StudentProfileData
import com.kapilguru.trainer.studentExamBatchResult.StudentExamPaperRequest
import com.kapilguru.trainer.studentExamBatchResult.StudentReportRequest
import com.kapilguru.trainer.studentsList.model.RequestRaiseComplaint

import com.kapilguru.trainer.studyMaterial.StudyMatrialListRequest
import com.kapilguru.trainer.studyMaterial.fileStructure.FolderContentRequest
import com.kapilguru.trainer.studyMaterial.studyMaterialOverview.StudyMatrialOverViewRequest
import com.kapilguru.trainer.testimonials.AddTrainerTestimonial
import com.kapilguru.trainer.testimonials.PostTestimonialsModel
import com.kapilguru.trainer.trainerGallery.UploadImageGallery
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest
import com.kapilguru.trainer.ui.courses.add_batch.models.AddBatchRequest
import com.kapilguru.trainer.ui.courses.addcourse.models.AddCourseRequest
import com.kapilguru.trainer.ui.courses.addcourse.models.UploadImageCourse
import com.kapilguru.trainer.ui.courses.batchesList.models.BatchListApiRequest
import com.kapilguru.trainer.ui.courses.view_course.ContactTrainerRequest
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.AddGuestLectureRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginRequest
import com.kapilguru.trainer.ui.otpLogin.model.OTPLoginValidateRequest
import com.kapilguru.trainer.ui.profile.bank.data.BankDetailsUploadRequest
import com.kapilguru.trainer.ui.profile.data.CheckOTPRequest
import com.kapilguru.trainer.ui.profile.data.GetOTPRequest
import com.kapilguru.trainer.ui.profile.data.ProfileData
import com.kapilguru.trainer.ui.webiner.addWebinar.model.AddWebinarRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

open class ApiHelper(private val apiKapilTutorService: ApiKapilTutorService) {

open suspend fun getUsers(loginUserRequest: LoginUserRequest) = apiKapilTutorService.userLogin(loginUserRequest)

    suspend fun getCousesList(trainerId: String) = apiKapilTutorService.coursesList(trainerId)
    suspend fun getScheduleList(trainerId: String) = apiKapilTutorService.scheduledExamsList(trainerId)

    suspend fun deleteCourse(courseId: String) = apiKapilTutorService.deleteCourse(courseId)

    suspend fun getBatchList(batchListApiRequest: BatchListApiRequest) = apiKapilTutorService.batchList(batchListApiRequest)

    suspend fun deleteBatch(batchId: String) = apiKapilTutorService.deleteBatch(batchId)

    suspend fun getBatchSyllabus(courseId: String) = apiKapilTutorService.batchSyllabusInfo(courseId)

    suspend fun getTrainerStudentList(trainerId: String) = apiKapilTutorService.getTrainerStudents(trainerId)

    suspend fun getTrainerCourseStudentList(courseId: String) = apiKapilTutorService.getTrainerCourseStudentList(courseId)

    suspend fun fetchStudentListForBatchApi(batchId: String) = apiKapilTutorService.fetchStudentListForBatchApi(batchId)

    suspend fun getWebinarStudentsList(webinarId: String) = apiKapilTutorService.getTrainerCourseStudentList(webinarId)

    suspend fun postComplaint(request: RequestRaiseComplaint) = apiKapilTutorService.postComplaint(request)

    suspend fun postCourse(addCourseRequest: AddCourseRequest) = apiKapilTutorService.addCourseInfo(addCourseRequest)

    suspend fun getCategory() = apiKapilTutorService.getCategoryList()

//    suspend fun postAddBatch(addBatchApiRequest: AddBatchApiRequest) = apiKapilTutorService.addBatchInfo(addBatchApiRequest)

//    suspend fun postGuestLectures(addGuestLecturesRequest: AddGuestLectureRequest)=apiKapilTutorService.addGuestLecturesInfo(addGuestLecturesRequest)

    suspend fun postAddBatch(addBatchApiRequest: AddBatchRequest) = apiKapilTutorService.addBatchInfo(addBatchApiRequest)

    suspend fun getEditBatch(batchId: Int) = apiKapilTutorService.getEditBatchInfo(batchId)

    suspend fun getOnGoingABatchList(trainerId: String) = apiKapilTutorService.getOnGoingBatchesList(trainerId)

    suspend fun getBatchStudentList(batchId: String) = apiKapilTutorService.batchStudentList(batchId)

    suspend fun getCountryList() = apiKapilTutorService.countriesList()

    suspend fun getStudentCountryList() = apiKapilTutorService.studentCountriesList()

    suspend fun getStateList(countryId: Int) = apiKapilTutorService.stateList(countryId)

    suspend fun getStudentStateList(countryId: Int) = apiKapilTutorService.studentStateList(countryId)

    suspend fun getCityList(stateId: Int) = apiKapilTutorService.cityList(stateId)

    suspend fun getStudentCityList(stateId: Int) = apiKapilTutorService.studentCityList(stateId)

    suspend fun getProfileData(userId: String) = apiKapilTutorService.getProfileData(userId)

    suspend fun saveProfileData(profileData: ProfileData) = apiKapilTutorService.saveProfile(profileData)

    suspend fun updateProfileData(userId: String, profileData: ProfileData) = apiKapilTutorService.updateProfile(userId, profileData)

    suspend fun updateCourse(courseId: String, addCourseRequest: AddCourseRequest) = apiKapilTutorService.updateCourse(courseId, addCourseRequest)

    suspend fun getCourseDetails(courseId: String) = apiKapilTutorService.getCourse(courseId)

    suspend fun uploadCourseImage(uploadCourseImage: UploadImageCourse) = apiKapilTutorService.uploadImageCourse(uploadCourseImage)

    suspend fun uploadVideo(files: MultipartBody.Part, trainerId: RequestBody, sourceId: RequestBody, code: RequestBody, videoType: RequestBody) =
        apiKapilTutorService.uploadVideo(files, trainerId, sourceId, code, videoType)

    suspend fun getBatchList(userId: String) = apiKapilTutorService.getBatchList(userId)

    suspend fun getAdminList() = apiKapilTutorService.getAdminList()

    suspend fun uploadCoursePdfFile(file: MultipartBody.Part, trainerId: RequestBody, courseId: RequestBody, pdfName: RequestBody) =
        apiKapilTutorService.uploadCoursePdfFile(file, trainerId, courseId, pdfName)

    suspend fun uploadBatchPdfFile(file: MultipartBody.Part, trainerId: RequestBody, pdfName: RequestBody, courseId: RequestBody, batchId: RequestBody) =
        apiKapilTutorService.batchCoursePdfFileUpload(file, trainerId, pdfName, courseId, batchId)

    suspend fun getCoursePdfFile(pdfId: String) = apiKapilTutorService.getCoursePdfFile(pdfId)

    suspend fun sendBatchNewMessageReq(request: SendBatchMessageRequest) = apiKapilTutorService.postBatchNewMessageReq(request)

    suspend fun sendAdminNewMessageReq(request: SendAdminMessageRequest) = apiKapilTutorService.postAdminNewMessageReq(request)

    suspend fun getInboxResponce(trainerId: String) = apiKapilTutorService.getInBoxResponce(trainerId)

    suspend fun getSentItemsResponce(trainerId: String) = apiKapilTutorService.getSentItemsResponce(trainerId)

    suspend fun updateLastMessageId(userId : String, lastMessageRequest: LastMessageRequest) = apiKapilTutorService.updateLastMessageId(userId,lastMessageRequest)

    suspend fun generateOTP(getOTPRequest: GetOTPRequest) = apiKapilTutorService.generateOTP(getOTPRequest)

    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiKapilTutorService.checkOTP(checkOTPRequest)

    suspend fun getRefundList(trainerId: String) = apiKapilTutorService.refundList(trainerId)

    suspend fun getWebinarList(trainerId: String) = apiKapilTutorService.webinarList(trainerId)

    suspend fun getLiveUpComingWebinarList(trainerId: String) = apiKapilTutorService.liveUpComingWebinarList(trainerId)

    suspend fun getBankDetails(userId: String) = apiKapilTutorService.getBankDetails(userId)

    suspend fun updateBankDetails(bankId: String, bankDetailsUploadReq: BankDetailsUploadRequest) = apiKapilTutorService.updateBankDetails(bankId, bankDetailsUploadReq)

    suspend fun saveBankDetails(bankDetailsUploadReq: BankDetailsUploadRequest) = apiKapilTutorService.saveBankDetails(bankDetailsUploadReq)

    suspend fun getGuestlectureList(trainerId: String) = apiKapilTutorService.guestLectureList(trainerId)

    suspend fun getLiveUpComingGuestLectureList(trainerId: String) = apiKapilTutorService.liveUpComingGuestLectureList(trainerId)

    suspend fun getCourseLanguages() = apiKapilTutorService.courseLanguages()

    suspend fun addGuestLectureDetails(addGuestLectureReq: AddGuestLectureRequest) = apiKapilTutorService.addGuestLectureDetails(addGuestLectureReq)

    suspend fun addGuestLectureVideo(insertId: String, addGuestLectureReq: AddGuestLectureRequest) = apiKapilTutorService.updateGuestLecture(insertId, addGuestLectureReq)

    suspend fun updateGuestLectureDetails(id: String, addGuestLectureReq: AddGuestLectureRequest) = apiKapilTutorService.updateGuestLecture(id, addGuestLectureReq)

    suspend fun addWebinarDetails(addWebinarRequest: AddWebinarRequest) = apiKapilTutorService.addWebinarDetails(addWebinarRequest)

    suspend fun updateWebinar(insertID: String, addWebinarRequest: AddWebinarRequest) = apiKapilTutorService.updateWebinar(insertID, addWebinarRequest)

    suspend fun updateEditGuestLecture(id: String, addGuestLectureReq: AddGuestLectureRequest) = apiKapilTutorService.updateEditGuestLecture(id, addGuestLectureReq)

    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiKapilTutorService.validateMobile(validateMobileRequest)

    suspend fun changePassword(changePasswordReq: ChangePasswordRequest) = apiKapilTutorService.changePassword(changePasswordReq)

    suspend fun getWebinarDetailstoEdit(webinarID: String) = apiKapilTutorService.getEditWebinarDetials(webinarID)

    suspend fun getWebLanguages() = apiKapilTutorService.getwebinarLanguages()

    suspend fun deleteWebinar(webinarId: String) = apiKapilTutorService.deleteWebinarItem(webinarId)

    suspend fun fetchWebinarDetails(webinarId: String) = apiKapilTutorService.fetchWebinarDetails(webinarId)

    suspend fun updateEditWebinarDetails(webinarID: String, addWebinarRequest: AddWebinarRequest) = apiKapilTutorService.updateWebinarDetailsAfterEdit(webinarID, addWebinarRequest)

    suspend fun deleteGuestLecture(guestLectureDeleteId: String) = apiKapilTutorService.deleteGuestLecture(guestLectureDeleteId)

    suspend fun getAllSubscriptions() = apiKapilTutorService.getAllSubscriptions()

    suspend fun getTrainerCourseData(id: String) = apiKapilTutorService.getTrainerCourseData(id)

    suspend fun getEarningsList(trainerId: String) = apiKapilTutorService.earningsList(trainerId)

    suspend fun getEarningsDetailsList(trainerId: String) = apiKapilTutorService.earningsDetailsList(trainerId)

    suspend fun getBestTrainerCourseList(id: String) = apiKapilTutorService.getBestTrainerCourseList(id)

    suspend fun getMySubscriptions(trainerId: String) = apiKapilTutorService.getMySubscriptions(trainerId)

    suspend fun mapCoursePosition(coursePositionMapReq: CoursePositionMapRequest) = apiKapilTutorService.mapCoursePosition(coursePositionMapReq)

    suspend fun mapCourseBestTrainer(courseBestTrainerMapRequest: CourseBestTrainerMapRequest) = apiKapilTutorService.mapCourseBestTrainer(courseBestTrainerMapRequest)

    suspend fun postReferAndEarn(referAndEarnRequest: ReferAndEarnRequest) = apiKapilTutorService.postReferAndEarn(referAndEarnRequest)

    suspend fun validateMail(validateMailReq: ValidateMailRequest) = apiKapilTutorService.validateMail(validateMailReq)

    suspend fun registerAccount(registerRequest: RegisterRequest) = apiKapilTutorService.registerAccount(registerRequest)

    suspend fun sendOtpMessage(sendOtpSmsRequest: SendOtpSmsRequest)= apiKapilTutorService.sendOtpMessage(sendOtpSmsRequest)

    suspend fun validateOtp(validateOtpRequest: ValidateOtpRequest) = apiKapilTutorService.validateOtp(validateOtpRequest)

    suspend fun fetchCountryListForSignUp() = apiKapilTutorService.fetchcountriesListForSignUp()

    suspend fun getEarningsHistory(trainerId: String) = apiKapilTutorService.getEarningsHistory(trainerId)

    suspend fun getHistoryDetailAmount(trainerId: String, selectedId: String) = apiKapilTutorService.getHistoryDetailAmount(trainerId, selectedId)

    suspend fun getHistoryDetailAmount1(trainerId: String, selectedId: String) = apiKapilTutorService.getHistoryDetailAmount1(trainerId, selectedId)

    suspend fun validateOTPLogin(otpLoginValidateReq: OTPLoginValidateRequest) = apiKapilTutorService.validateOTPLogin(otpLoginValidateReq)

    suspend fun requestOTP(otpLoginRequest: OTPLoginValidateRequest) = apiKapilTutorService.requestOTP(otpLoginRequest)

    suspend fun otpLogin(otpLoginRequest: OTPLoginRequest) = apiKapilTutorService.otpLogin(otpLoginRequest)

    suspend fun logoutUser(logoutRequest: LogoutRequest) = apiKapilTutorService.logoutUser(logoutRequest)

    suspend fun getNotificationCount(userId : String) = apiKapilTutorService.getNotificationCount(userId)

    suspend fun getLiveUpComingClasses(trainerId: String) = apiKapilTutorService.getLiveUpComingClasses(trainerId)

    suspend fun getBatchCompletionResponse(batchCompletionReq: BatchCompletionRequest) = apiKapilTutorService.getBatchCompletionResponse(batchCompletionReq)

    suspend fun sendBatchCompletionRequest(sendBatchCompletionRequest: SendBatchCompletionRequest) = apiKapilTutorService.sendBatchCompletionRequest(sendBatchCompletionRequest)

    suspend fun sendQuestionPaperTitle(sendQuestionPaperTitle: QuestionPaperTitleRequest) = apiKapilTutorService.sendQuestionPaperTitle(sendQuestionPaperTitle)

    suspend fun addQuestion(addQuestionRequest: AddQuestionRequest) = apiKapilTutorService.addQuestion(addQuestionRequest)

    suspend fun updateQuestion(questionId: String, updateQuestionRequest: UpdateQuestionRequest) = apiKapilTutorService.updateQuestion(questionId, updateQuestionRequest)

    suspend fun getPreviousQuestionPapersList(trainerId: String, courseId: String) = apiKapilTutorService.getPreviousQuestionPapersList(trainerId, courseId)

    suspend fun getQuestionsList(trainerId: String) = apiKapilTutorService.getQuestionsList(trainerId)

    suspend fun batchStudentReport(batchReportsRequestModel: BatchReportsRequestModel) = apiKapilTutorService.batchStudentReport(batchReportsRequestModel)

    suspend fun getStudentReport(studentReportRequest: StudentReportRequest) = apiKapilTutorService.getStudentReport(studentReportRequest)

    suspend fun getAnswerSheet(studentReportRequest: StudentExamPaperRequest) = apiKapilTutorService.getAnswerSheet(studentReportRequest)

    suspend fun getPreviousQuestionsList(trainerId: String, courseId: String) = apiKapilTutorService.getPreviousQuestionsList(trainerId, courseId)

    suspend fun callInitiationTransactionApi(initiateTransactionRequest: InitiateTransactionRequest) = apiKapilTutorService.callInitiationTransactionApi(initiateTransactionRequest)

    suspend fun callTransactionStatusApi(initiateTransactionRequest: InitiateTransactionRequest) = apiKapilTutorService.callTransactionStatusApi(initiateTransactionRequest)

    suspend fun callCopyFromQuestionPaper(copyFromQuestionPaperRequest: CopyFromQuesPaperRequest) = apiKapilTutorService.copyFromQuestionPaper(copyFromQuestionPaperRequest)

    suspend fun addExistingQuestions(addExistingQuesApiRequest: AddExistingQuesApiRequest) = apiKapilTutorService.addExistingQuesApiRequest(addExistingQuesApiRequest)

    suspend fun getActiveBatchesByCourse(trainerId: String, courseId: String) = apiKapilTutorService.getActiveBatchesByCourse(trainerId, courseId)

    suspend fun assignExamToBatch(assignExamToBatchRequest: AssignExamToBatchRequest) = apiKapilTutorService.assignExamToBatch(assignExamToBatchRequest)

    suspend fun getDemoWebinarStudents(webinarId: String) = apiKapilTutorService.getDemoWebinarStudents(webinarId)

    suspend fun getDemoStudentListApi(webinarId: String) = apiKapilTutorService.getDemoStudentListApi(webinarId)

    suspend fun fetchUpcomingSchedule(trainerId: String) = apiKapilTutorService.fetchUpcomingSchedule(trainerId)

    suspend fun getBatchDetails(batchId: String) = apiKapilTutorService.getBatchDetails(batchId)

    suspend fun getBatchDocuments(batchDocumentsRequest: BatchDocumentsRequest) = apiKapilTutorService.getBatchDocuments(batchDocumentsRequest)

    suspend fun downloadPdfDocument(fileName: String) = apiKapilTutorService.downloadPdfFile(fileName)

    suspend fun getMyReferrals(trainerId : String) = apiKapilTutorService.getMyReferrals(trainerId)

    suspend fun updateKyc(trainerId : String,updateKycRequest : UpdateKycRequest) = apiKapilTutorService.updateKyc(trainerId,updateKycRequest)

    suspend fun getGuestLectureStudentViewDetails(demoLectureId: String) = apiKapilTutorService.getGuestLectureStudentViewDetails(demoLectureId)

    suspend fun uploadAadharPdfFile(file: MultipartBody.Part, userId: RequestBody, title: RequestBody) =
        apiKapilTutorService.uploadAadharPdfFile(file, userId, title)

    suspend fun uploadPanPdfFile(file: MultipartBody.Part, userId: RequestBody, title: RequestBody) =
        apiKapilTutorService.uploadPanPdfFile(file, userId, title)

    suspend fun fetchCourseDetails(courseId: String) = apiKapilTutorService.fetchCourseDetails(courseId)

    suspend fun fetchSyllabusAttachments(syllabusAttachmentId: String) = apiKapilTutorService.fetchSyllabusAttachments(syllabusAttachmentId)

    suspend fun requestBatch(batchRequest: BatchRequest) =  apiKapilTutorService.requestBatch(batchRequest)

    suspend fun getStudentReviews(studentId: String) = apiKapilTutorService.getStudentReviews(studentId)

//    suspend fun updateStudentReview(writeReviewRequest: WriteReviewRequest) = apiKapilTutorService.writeUpdateReview(writeReviewRequest)

    suspend fun contactTrainer(contactTrainerRequest: ContactTrainerRequest) = apiKapilTutorService.contactTrainer(contactTrainerRequest)

    suspend fun fetchAllWebinars() = apiKapilTutorService.fetchAllWebinars()

    suspend fun fetchAllDemos() = apiKapilTutorService.fetchAllDemos()

    suspend fun getAllJobOpenings() = apiKapilTutorService.getAllJobOpenings()

    suspend fun getDashBoardPopularAndTrendingCourses() = apiKapilTutorService.getDashBoardPopularAndTrendingCourses()

    suspend fun getAllPopularAndTrendingCourses() = apiKapilTutorService.getAllPopularAndTrendingCourses()

    suspend  fun createLeadApi(createLeadRequest: CreateLeadRequest) = apiKapilTutorService.createLeadApi(createLeadRequest)

    suspend fun uploadTrainerGalleryImages(uploadImageCourse: UploadImageGallery) =
        apiKapilTutorService.uploadTrainerGalleryImages(uploadImageCourse)

    suspend fun getTrainerGalleryImages(packageId: String) =
        apiKapilTutorService.getTrainerGalleryImages(packageId)

    suspend fun addtestimonials(addTrainerTestimonial: PostTestimonialsModel) = apiKapilTutorService.addtestimonials(addTrainerTestimonial)

    suspend fun getTestimonials(tenantId: Int) = apiKapilTutorService.getTestimonials(tenantId)



    suspend fun getTaxes() = apiKapilTutorService.getTaxes()

    suspend fun getListOfStudyMaterials(studyMaterialListRequest: StudyMatrialListRequest) = apiKapilTutorService.getListOfStudyMaterials(studyMaterialListRequest)

    suspend fun getStudentProfileData(userId: String) = apiKapilTutorService.getStudentProfileData(userId)

    suspend fun getStudyMaterialOverView(studyMatrialOverViewRequest: StudyMatrialOverViewRequest) = apiKapilTutorService.getStudyMaterialOverView(studyMatrialOverViewRequest)


    suspend fun getFolderContent(folderContentRequest: FolderContentRequest) = apiKapilTutorService.getFolderContent(folderContentRequest)

//    suspend fun getStudyMaterialList(studyMaterialListRequest: StudyMaterialListRequest) = apiKapilTutorService.getStudyMaterialList(studyMaterialListRequest)

    suspend fun getCouponsList(trainerId: Int) = apiKapilTutorService.getCouponsList(trainerId)


    suspend fun updateStudentProfileData(userId: String, studentProfileData: StudentProfileData) = apiKapilTutorService.updateStudentProfile(userId, studentProfileData)

    suspend fun addCoupon(addCouponsRequest: AddCouponsRequest) = apiKapilTutorService.addCoupon(addCouponsRequest)


    open suspend fun getCategoryCourse(couponCourseCategoryRequest: CouponCourseCategoryRequest) = apiKapilTutorService.getCategoryCourse(couponCourseCategoryRequest)

}
