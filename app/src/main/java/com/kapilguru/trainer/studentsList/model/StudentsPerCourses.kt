package com.kapilguru.trainer.studentsList.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class StudentsPerCourses (

    var courseInfo : StudentsCourseInfo?=null,

    var studentDetails : ArrayList<StudentDetails>?=null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(StudentsCourseInfo::class.java.classLoader) as? StudentsCourseInfo,
        parcel.createTypedArrayList(StudentDetails.CREATOR)
    ) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(courseInfo)
            parcel.writeValue(studentDetails)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentsPerCourses> {
        override fun createFromParcel(parcel: Parcel): StudentsPerCourses {
            return StudentsPerCourses(parcel)
        }

        override fun newArray(size: Int): Array<StudentsPerCourses?> {
            return arrayOfNulls(size)
        }
    }

}