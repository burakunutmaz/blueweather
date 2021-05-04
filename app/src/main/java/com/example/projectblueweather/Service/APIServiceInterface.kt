package com.example.projectblueweather.Service

import com.example.projectblueweather.Model.City
import com.example.projectblueweather.Model.CityForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIServiceInterface {
    @GET("/api/location/search/")
    fun getCityList(@Query("lattlong") lattlong:String) : Call<MutableList<City>>

    @GET("/api/location/{id}")
    fun getCityDetails(@Path("id") id:Long) : Call<CityForecast>
}