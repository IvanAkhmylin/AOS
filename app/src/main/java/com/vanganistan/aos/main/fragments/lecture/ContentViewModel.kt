package com.vanganistan.aos.main.fragments.lecture

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanganistan.aos.Repository
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.main.fragments.labs.Lab
import com.vanganistan.aos.models.Lecture
import com.vanganistan.aos.models.Test
import com.vanganistan.aos.models.UserTestAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentViewModel: ViewModel(){
    val lectureUploadLiveData = MutableLiveData<Resource<String>>()
    val labUploadLiveData = MutableLiveData<Resource<String>>()
    val lectureDetailLiveData = MutableLiveData<Resource<String>>()
    val labDetailLiveData = MutableLiveData<Resource<String>>()
    val testUpload = MutableLiveData<Resource<String>>()
    val answerUpload = MutableLiveData<Resource<String>>()
    val lecturesLiveData = MutableLiveData<Resource<ArrayList<Lecture>>>()
    val labsLiveData = MutableLiveData<Resource<ArrayList<Lab>>>()
    val testsLiveData = MutableLiveData<Resource<ArrayList<Test>>>()

    init {
        getLectures()
        getTests()
        getLabs()
    }

    private fun getLabs() {
        labsLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().getLabs() {
                labsLiveData.postValue(it)
            }
        }
    }

    fun uploadLecture(lecture: Lecture) {
        lectureUploadLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().uploadLecture(lecture) {
                lectureUploadLiveData.postValue(Resource.success(it))
            }
        }
    }
    fun uploadLab(lab: Lab) {
        labUploadLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().uploadLab(lab) {
                labUploadLiveData.postValue(Resource.success(it))
            }
        }
    }
    fun getLectures() {
        lecturesLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().getLectures() {
                lecturesLiveData.postValue(it)
            }
        }
    }
    fun getTests() {
        testsLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().getTests() {
                testsLiveData.postValue(it)
            }
        }
    }


    fun getLectureDetail(lecture: Lecture) {
        lectureDetailLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().getLecturesDetail(lecture) {
                lectureDetailLiveData.postValue(Resource.success(it))
            }
        }
    }
    fun getLab(lab: Lab,isUdu: Boolean) {
        labDetailLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            if (isUdu){
                Repository().getEduDetail(lab) {
                    labDetailLiveData.postValue(Resource.success(it))
                }
            }else{
                Repository().getLabDetail(lab) {
                    labDetailLiveData.postValue(Resource.success(it))
                }
            }
        }
    }

    fun uploadTest(test: Test) {
        testUpload.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().uploadTest(test) {
                testUpload.postValue(Resource.success(it))
            }
        }
    }

    fun uploadAction(info: UserTestAction) {
        answerUpload.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().uploadAnswer(info) {
                answerUpload.postValue(Resource.success(it))
            }
        }
    }


}
