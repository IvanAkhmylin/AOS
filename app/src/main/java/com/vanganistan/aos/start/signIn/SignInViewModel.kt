package com.vanganistan.aos.start.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vanganistan.aos.Repository
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource

class SignInViewModel: ViewModel(){
    val signInLiveData = MutableLiveData<Resource<String>>()
    val userUtilsResult = MutableLiveData<String>()

    fun deleteCurrentUser() {
        signInLiveData.value = Resource.loading()
        Repository().deleteUser{
            userUtilsResult.postValue(it)
        }
    }

    fun exitFromApp() {
        signInLiveData.value = Resource.loading()
        Repository().exitFromApp{
            userUtilsResult.postValue(it)
        }
    }

    fun changeUserPassWord(email: String) {
        signInLiveData.value = Resource.loading()
        Repository().changeUserPass(email){
            userUtilsResult.postValue(it)
        }
    }

    fun signInUser(email: String, pass: String) {
        signInLiveData.value = Resource.loading()
        Repository().signInUser(email, pass){
            signInLiveData.value = it
        }
    }

}