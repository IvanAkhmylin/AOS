package com.vanganistan.aos.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User(

    var email: String? = "",
    var name: String? = "",
    var group: String? = "",
    var number: String? = "",
    var userImage: String? = null,
    @SerializedName("actions")
    @Expose
    val actions: HashMap<String, UserTestAction>? = null
)
