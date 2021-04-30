package com.nambasoft.stepapp.Models

import androidx.annotation.DrawableRes

data class NotificationModel(
    @DrawableRes val notImage : Int,
    val notFollowerName : String,
    val notFollowerActive : String,
    val notDate : String
)
