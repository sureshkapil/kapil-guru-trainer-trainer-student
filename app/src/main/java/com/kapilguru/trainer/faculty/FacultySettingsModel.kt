package com.kapilguru.trainer.faculty

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FacultySettingsModel(
    @SerializedName("add_exams") var addExams: Int? = 0,
    @SerializedName("add_live_course") var addLiveCourse: Int? = 0,
    @SerializedName("create_coupons") var createCoupons: Int? = 0,
    @SerializedName("add_recording_course") var addRecordingCourse: Int? = 0,
    @SerializedName("subscriptions") var subscriptions: Int? = 0,
    @SerializedName("today_follow_ups") var todayFollowUps: Int? = 0,
    @SerializedName("fee_management") var feeManagement: Int? = 0,
    @SerializedName("my_students") var myStudents: Int? = 0,
    @SerializedName("add_study_materials") var addStudyMaterials: Int? = 0,
    @SerializedName("my_enquiries") var myEnquiries: Int? = 0,
    @SerializedName("social_media_links") var socialMediaLinks: Int? = 0,
    @SerializedName("promotional_images") var promotionalImages: Int? = 0,
    @SerializedName("testimonials") var testimonials: Int? = 0,
    @SerializedName("attendance_management") var attendanceManagement: Int? = 0,
    @SerializedName("earnings") var earnings: Int? = 0,
    @SerializedName("certificates") var certificates: Int? = 0,
    @SerializedName("add_free_classes") var addFreeClasses: Int? = 0,
    @SerializedName("messages") var messages: Int? = 0,
    @SerializedName("gallery") var gallery: Int? = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(addExams)
        parcel.writeValue(addLiveCourse)
        parcel.writeValue(createCoupons)
        parcel.writeValue(addRecordingCourse)
        parcel.writeValue(subscriptions)
        parcel.writeValue(todayFollowUps)
        parcel.writeValue(feeManagement)
        parcel.writeValue(myStudents)
        parcel.writeValue(addStudyMaterials)
        parcel.writeValue(myEnquiries)
        parcel.writeValue(socialMediaLinks)
        parcel.writeValue(promotionalImages)
        parcel.writeValue(testimonials)
        parcel.writeValue(attendanceManagement)
        parcel.writeValue(earnings)
        parcel.writeValue(certificates)
        parcel.writeValue(addFreeClasses)
        parcel.writeValue(messages)
        parcel.writeValue(gallery)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FacultySettingsModel> {
        override fun createFromParcel(parcel: Parcel): FacultySettingsModel {
            return FacultySettingsModel(parcel)
        }

        override fun newArray(size: Int): Array<FacultySettingsModel?> {
            return arrayOfNulls(size)
        }
    }

    fun getFacultySettingsModel(facultyListResponseApi: FacultyListResponseApi): FacultySettingsModel {
        return FacultySettingsModel(
            addExams = facultyListResponseApi.addExams,
            addLiveCourse = facultyListResponseApi.addLiveCourse,
            createCoupons = facultyListResponseApi.createCoupons,
            addRecordingCourse = facultyListResponseApi.addRecordingCourse,
            subscriptions = facultyListResponseApi.subscriptions,
            todayFollowUps = facultyListResponseApi.todayFollowUps,
            feeManagement = facultyListResponseApi.feeManagement,
            myStudents = facultyListResponseApi.myStudents,
            addStudyMaterials = facultyListResponseApi.addStudyMaterials,
            myEnquiries = facultyListResponseApi.myEnquiries,
            socialMediaLinks = facultyListResponseApi.socialMediaLinks,
            promotionalImages = facultyListResponseApi.promotionalImages,
            testimonials = facultyListResponseApi.testimonials,
            attendanceManagement = facultyListResponseApi.attendanceManagement,
            earnings = facultyListResponseApi.earnings,
            certificates = facultyListResponseApi.certificates,
            addFreeClasses = facultyListResponseApi.addFreeClasses,
            messages = facultyListResponseApi.messages,
            gallery = facultyListResponseApi.gallery,
        )
    }
}