package com.vanganistan.aos.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class UserTestAction(
    var date: Long? = 0L,
    var title: String? = "",
    var questionList: ArrayList<AnswerList>? = null
): Parcelable

@Parcelize
data class AnswerList(
    val question: String = "",
    val testLectureName: String? = "",
    val lectureId: Int? = 0,
    val userAnswerTrue: Boolean = false
): Parcelable

