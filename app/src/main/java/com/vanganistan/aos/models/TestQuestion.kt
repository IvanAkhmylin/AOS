package com.vanganistan.aos.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestQuestion (
    var answers: ArrayList<String> = arrayListOf("", "", "", ""),
    var question: String = "",
    var trueAnswerIndex: Int = 0,
    var userAnswerIndex: Int = -1,
    var lectureId: Int = -1,
    var module: Int = -1,
    var lectureName: String = ""

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

    fun answerValid() : Boolean{
        return userAnswerIndex != -1
    }

    fun isUserAnswerTrue() = trueAnswerIndex == userAnswerIndex
}

@Parcelize
data class Test (
    var id: Int = -1,
    var tests: ArrayList<TestQuestion> = arrayListOf()
) : Parcelable