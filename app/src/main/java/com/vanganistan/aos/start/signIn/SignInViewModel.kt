package com.vanganistan.aos.start.signIn

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vanganistan.aos.Repository
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.models.User

class SignInViewModel: ViewModel(){
    val signInLiveData = MutableLiveData<Resource<String>>()
    val userLiveData = MutableLiveData<Resource<Boolean>>()
    val userUtilsResult = MutableLiveData<String>()
    val usersLiveData = MutableLiveData<Resource<ArrayList<User>>>()

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

    fun getUserData(uID: String) : LiveData<User> =
        Repository().getUserData(id = uID)

    fun setupUserProfileImage(uri: Uri?) {
        userLiveData.value = Resource.loading()
        Repository().setupUserImage(uri){
            userLiveData.postValue(Resource.success(true))
        }
    }

    fun getUsers() {
        usersLiveData.value = Resource.loading()
        Repository().getUsers(){
            usersLiveData.postValue(it)
        }
    }


}