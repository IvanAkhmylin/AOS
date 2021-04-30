package com.vanganistan.aos.start.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vanganistan.aos.Repository
import com.vanganistan.aos.Utils.Resource

class SignUpViewModel : ViewModel() {

    val signInLiveData = MutableLiveData<Resource<String>>()


    fun signUpUser(userData: HashMap<String, Any>) {
        signInLiveData.value = Resource.loading()
        Repository().signUpUser(userData) {
            signInLiveData.value = it
        }
    }
}