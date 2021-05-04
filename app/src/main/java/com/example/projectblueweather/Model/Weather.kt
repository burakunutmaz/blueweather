package com.example.projectblueweather.Model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Weather(
        @SerializedName("weather_state_name")
        val state:String,

        @SerializedName("applicable_date")
        val date: Date,

        val min_temp:Double,
        val max_temp:Double,

        val humidity:Int,
        val wind_speed:Double,

        @SerializedName("weather_state_abbr")
        val abbr:String
)