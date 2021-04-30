package com.nambasoft.stepapp.app_models

data class User(
    var name: String? = "",
    var email: String? = "",
    var password: String? = "",
    var instagram: String? = "",
    var interests: ArrayList<String>? = null,
    var userImage: String? = null
)

data class UserInterests(var interests: ArrayList<String>? = null)
