package com.nambasoft.stepapp.app_models

import java.io.Serializable

data class CreateStep(
    var interests: ArrayList<String>? = ArrayList(),
    var startStepImage: String? = null, var startStepText: String? = "", var greetingsVideo: String? = null,
    var greetingsText: String? = "", var stepsList : ArrayList<Steps>? = ArrayList(), var farewellVideo: String? = null,
    var farewellString: String? = "", var stepSum: Int? = 0, var creatorName: String? = "",
    var creatorImage: String? = "", var creatorUID: String? = "", var creatorEMAIL: String? = ""): Serializable

data class Steps(var stepVideo: String? = null, var stepString : String? = ""): Serializable

