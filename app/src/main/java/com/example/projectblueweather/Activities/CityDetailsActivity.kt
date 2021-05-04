package com.example.projectblueweather.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.projectblueweather.Adapter.ForecastAdapter
import com.example.projectblueweather.Model.CityForecast
import com.example.projectblueweather.R
import com.example.projectblueweather.Service.APIServiceInterface
import com.example.projectblueweather.Service.RetrofitClientInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.text.DateFormat
import java.text.SimpleDateFormat

class CityDetailsActivity : AppCompatActivity() {

    lateinit var cityForecast: CityForecast
    lateinit var rvForecast:RecyclerView
    lateinit var tvLocationName:TextView
    lateinit var tvLocationTime:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_details)

        val id = intent.getLongExtra("CITY_ID", 0)

        tvLocationName = findViewById(R.id.location_name)
        tvLocationTime = findViewById(R.id.location_time_value)

        rvForecast = findViewById(R.id.forecast_list)
        rvForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvForecast.setHasFixedSize(true)

        val snapHelper:SnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(rvForecast)
        }

        getCityDetails(id)
    }

    private fun getCityDetails(id: Long){
        val api = RetrofitClientInstance.retrofitInstance?.create(APIServiceInterface::class.java)!!

        GlobalScope.launch {
            val response = api.getCityDetails(id).awaitResponse()
            if (response.isSuccessful){
                val data = response.body()!!
                runOnUiThread {
                    cityForecast = data
                    rvForecast.adapter = ForecastAdapter(cityForecast.forecast)
                    tvLocationTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(cityForecast.time)
                    tvLocationName.text = cityForecast.title
                }
            }
        }
    }

    fun go_back(v:View){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}