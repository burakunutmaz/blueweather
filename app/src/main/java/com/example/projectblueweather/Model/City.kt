package com.example.projectblueweather.Model

import com.google.gson.annotations.SerializedName

data class City(
    val distance:Long,
    val title:String,

    var thumbnail:String,

    @SerializedName("woeid")
    val id:Long
)