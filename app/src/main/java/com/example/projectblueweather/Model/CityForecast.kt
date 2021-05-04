package com.example.projectblueweather.Model

import com.google.gson.annotations.SerializedName
import java.util.*

data class CityForecast(

        val title:String,

        @SerializedName("consolidated_weather")
        val forecast: List<Weather>,

        val timezone:String,

        val time:Date,
        val sun_rise:Date,
        val sun_set:Date
)