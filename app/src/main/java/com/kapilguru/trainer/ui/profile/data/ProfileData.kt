package com.kapilguru.trainer.ui.profile.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileData(
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("contact_number") var contactNumber: String? = null,
    @SerializedName("country_code") var countryCode: String? = null,
    @SerializedName("alternate_number") var alternateNumber: String? = null,
    @SerializedName("email_id") var email_id: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("address_line_1") var addressLine1: String? = null,
    @SerializedName("address_line_2") var addressLine2: String? = null,
    @SerializedName("city") var cityId: String? = null,
    @SerializedName("state") var stateId: String? = null,
    @SerializedName("country") var countryId: String? = null,
    @SerializedName("postal_code") var postalCode: String? = null,
    @SerializedName("describe_about_you") var description: String? = null,
    @SerializedName("is_organization") var isOrganization: Int? = null,
    @SerializedName("company_name") var companyName: String? = null,
    @SerializedName("org_contact_number") var orgContactNumber: String? = null,
    @SerializedName("official_email") var officialEmail: String? = null,
    @SerializedName("company_website_url") var companyWebsiteUrl: String? = null,
    @SerializedName("org_address_line_1") var orgAddressLine1: String? = null,
    @SerializedName("org_address_line_2") var orgAddressLine2: String? = null,
    @SerializedName("org_city") var orgCityId: String? = null,
    @SerializedName("org_state") var orgStateId: String? = null,
    @SerializedName("org_country") var orgCountryId: String? = null,
    @SerializedName("org_postal_code") var orgPostalCode: String? = null,
    @SerializedName("is_gst") var isGst: Int? = null,
    @SerializedName("GST_number") var GSTNumber: String? = "",
    @SerializedName("image") var image: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("bank_details_id") var bankDetailsId: Int? = null,
    @SerializedName("pan") var pan: String? = null,
    @SerializedName("aadhar") var aadhar: String? = null,
    @SerializedName("org_pan") var orgPan: String? = null,
    @SerializedName("estabilshment_no ") var estabilshmentNo : String? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("created_date") var createdDate: String? = null, //2021-11-25T21:33:04.000Z
    @SerializedName("modified_by") var modifiedBy: Int? = null,
    @SerializedName("modified_date") var modifiedDate: String? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("facebook_url") var facebookUrl: String? = null,
    @SerializedName("twitter_url") var twitterUrl: String? = null,
    @SerializedName("instagram_url") var instagramUrl: String? = null,
    @SerializedName("linkedin_url") var linkedinUrl: String? = null,
    @SerializedName("establishment_date") var establishmentDate: String? = null,
    @SerializedName("years_of_exp") var yearsOfExp: Int? = null,
    @SerializedName("is_pan_uploaded") var isPanUploaded: Int? = null,
    @SerializedName("is_aadhar_uploaded") var isAadharUploaded: Int? = null,
    @SerializedName("is_establisment_proof_uploaded") var isEstablismentProofUploaded: Int? = null,
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userId)
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(contactNumber)
        parcel.writeString(countryCode)
        parcel.writeString(alternateNumber)
        parcel.writeString(email_id)
        parcel.writeString(gender)
        parcel.writeString(currency)
        parcel.writeString(addressLine1)
        parcel.writeString(addressLine2)
        parcel.writeString(cityId)
        parcel.writeString(stateId)
        parcel.writeString(countryId)
        parcel.writeString(postalCode)
        parcel.writeString(description)
        parcel.writeValue(isOrganization)
        parcel.writeString(companyName)
        parcel.writeString(orgContactNumber)
        parcel.writeString(officialEmail)
        parcel.writeString(companyWebsiteUrl)
        parcel.writeString(orgAddressLine1)
        parcel.writeString(orgAddressLine2)
        parcel.writeString(orgCityId)
        parcel.writeString(orgStateId)
        parcel.writeString(orgCountryId)
        parcel.writeString(orgPostalCode)
        parcel.writeValue(isGst)
        parcel.writeString(GSTNumber)
        parcel.writeString(image)
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeValue(bankDetailsId)
        parcel.writeString(pan)
        parcel.writeString(aadhar)
        parcel.writeString(orgPan)
        parcel.writeString(estabilshmentNo)
        parcel.writeValue(createdBy)
        parcel.writeString(createdDate)
        parcel.writeValue(modifiedBy)
        parcel.writeString(modifiedDate)
        parcel.writeValue(isActive)
        parcel.writeString(facebookUrl)
        parcel.writeString(twitterUrl)
        parcel.writeString(instagramUrl)
        parcel.writeString(linkedinUrl)
        parcel.writeString(establishmentDate)
        parcel.writeValue(yearsOfExp)
        parcel.writeValue(isPanUploaded)
        parcel.writeValue(isAadharUploaded)
        parcel.writeValue(isEstablismentProofUploaded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileData> {
        override fun createFromParcel(parcel: Parcel): ProfileData {
            return ProfileData(parcel)
        }

        override fun newArray(size: Int): Array<ProfileData?> {
            return arrayOfNulls(size)
        }
    }
}