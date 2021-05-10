package com.vanganistan.aos.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestModel (
    var answers: ArrayList<String> = arrayListOf("", "", "", ""),
    var question: String = "",
    var trueAnswerIndex: Int = 0
) : Parcelable {
    fun validate() : Boolean {
        val filter = answers.filter { it.trim().isEmpty() }
        return if (filter.isNotEmpty()){
            false
        }else if (question.trim().isEmpty()){
            false
        }else{
            true
        }
    }
}

@Parcelize
data class Test (
    var id: Int = -1,
    var tests: ArrayList<TestModel> = arrayListOf(),
    var lectureId: Int = -1,
    var module: Int = -1,
    var lectureName: String = ""
) : Parcelable