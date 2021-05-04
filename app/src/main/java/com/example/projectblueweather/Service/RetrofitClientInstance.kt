package com.example.projectblueweather.Service

import com.example.projectblueweather.Utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    private var retrofit : Retrofit ?= null

    val retrofitInstance : Retrofit?
        get() {
            if (retrofit == null){
                retrofit = Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }

            return retrofit
        }


}