package com.agomezlucena.youcandoit.contact_managament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact (val name: String?, val phoneNumber: String?):Parcelable{
    fun isValid() = (name != null && name.isNotBlank()) || (phoneNumber != null && phoneNumber.isNotBlank())
}