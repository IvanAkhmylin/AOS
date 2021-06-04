package com.vanganistan.aos.main.fragments.labs

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lab (
    var labId: Int = -1,
    var eduId: Int = -1,
    var fileDescription: String = "",
    var labUri: String = "",
    var eduUri: String = ""
) : Parcelable