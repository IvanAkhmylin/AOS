package com.vanganistan.aos.start.signUp

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vanganistan.aos.Repository
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.models.UserTestAction

class SignUpViewModel : ViewModel() {

    val signInLiveData = MutableLiveData<Resource<String>>()
    val updateImageLiveData = MutableLiveData<Boolean>()


    fun signUpUser(userData: HashMap<String, Any>) {
        signInLiveData.value = Resource.loading()
        userData["actions"] = ArrayList<UserTestAction>()
        Repository().signUpUser(userData) {
            signInLiveData.value = it
        }
    }

    fun setupUserProfileImage(uri: Uri?) {
        updateImageLiveData.postValue(true)
        Repository().setupUserImage(uri){
            updateImageLiveData.postValue(false)
        }
    }
}