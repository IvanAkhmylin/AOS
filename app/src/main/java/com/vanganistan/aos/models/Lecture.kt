package com.vanganistan.aos.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lecture (
    var id: Int = -1,
    var fileDescription: String = "",
    var fileUri: String = "",
    var module: Int = 1 // 1,2 - module
) : Parcelable