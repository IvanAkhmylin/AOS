package com.vanganistan.aos.main.fragments.lecture

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanganistan.aos.Repository
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.models.Lecture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LectureViewModel: ViewModel(){
    val lectureUploadLiveData = MutableLiveData<Resource<String>>()
    val lectureDetailLiveData = MutableLiveData<Resource<String>>()
    val lecturesLiveData = MutableLiveData<Resource<ArrayList<Lecture>>>()

    init {
        getLectures()
    }

    fun uploadLecture(lecture: Lecture) {
        lectureUploadLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().uploadLecture(lecture) {
                lectureUploadLiveData.postValue(Resource.success(it))
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


    fun getLectureDetail(lecture: Lecture) {
        lectureDetailLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO){
            Repository().getLecturesDetail(lecture) {
                lectureDetailLiveData.postValue(Resource.success(it))
            }
        }
    }
}
