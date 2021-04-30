package com.nambasoft.stepapp.Models

import com.nambasoft.stepapp.app_models.CreateStep

data class MainParentModel(
    val title : String = "",
    val children : ArrayList<CreateStep>
)