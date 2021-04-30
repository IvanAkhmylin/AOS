package com.vanganistan.aos.Utils

object Constants{
    const val SUCCESSFUL = "successful"
    const val FAILURE = "failure"
    const val EXIT_FROM_APP = "exit ok"
    const val DELETE_USER_FAIL = "delete user fail"
    const val DELETE_USER_OK = "delete user ok"

    //sign Up const
    const val SIGN_UP_SUCCESS = "success sign up"
    const val SIGN_UP_EMAIL_EXIST = "user already exist"
    const val SIGN_UP_CANNOT_SIGN_UP_NOW = "cannot sign up now"
    const val SIGN_UP_FAIL = "failure sign up"
    const val SIGN_UP_ERROR_SENDING_EMAIL = "something wrong signUp"

    // sign In const
    const val SIGN_IN_INVALID_EMAIL = "The user may have been deleted."
    const val SIGN_IN_INVALID_PASS = "The password is invalid or the user does not have a password"
    const val SIGN_IN_SOMETHING_WRONG = "something wrong"
    const val SIGN_IN_EMAIL_VERIFIED_SANDE = "signIn verify sanded"
    const val SIGN_IN_COMPLETE = "signIn Complete"
    const val SIGN_IN_LOGIN_ATTEMPTS= "Too many unsuccessful login attempts"

}