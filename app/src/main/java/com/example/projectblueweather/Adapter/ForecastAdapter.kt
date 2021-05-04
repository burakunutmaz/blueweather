package com.example.projectblueweather.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectblueweather.Model.Weather
import com.example.projectblueweather.R
import com.example.projectblueweather.Utils.Constants
import org.w3c.dom.Text
import java.text.DateFormat
import kotlin.math.roundToInt

class ForecastAdapter(
        private val forecastList: List<Weather>
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.forecast_list_item, parent, false)
        return ForecastViewHolder(v)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {

        val weather = forecastList[position]
        val weatherImageUrl = Constants.IMG_BASE_URL + weather.abbr + ".png"

        val day = DateFormat.getDateInstance(DateFormat.FULL).format(weather.date)

        holder.tvDay.text = day
        holder.tvWeatherState.text = weather.state
        Glide.with(context)
                .load(weatherImageUrl)
                .centerCrop()
                .into(holder.imgWeather)

        val humidity = weather.humidity.toString() + "%"
        val mintemp = weather.min_temp.roundToInt().toString() + "°C"
        val maxTemp = weather.max_temp.roundToInt().toString() + "°C"
        val wind = weather.wind_speed.roundToInt().toString() + " mph"

        holder.tvHumidity.text = humidity
        holder.tvMinTemp.text = mintemp
        holder.tvMaxTemp.text = maxTemp
        holder.tvWind.text = wind
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }


    inner class ForecastViewHolder(v:View) : RecyclerView.ViewHolder(v){
        val imgWeather : ImageView = v.findViewById(R.id.weather_img)
        val tvDay:TextView = v.findViewById(R.id.day)
        val tvWeatherState:TextView = v.findViewById(R.id.weather_title)
        val tvMinTemp:TextView = v.findViewById(R.id.min_temp_value)
        val tvMaxTemp:TextView = v.findViewById(R.id.max_temp_value)
        val tvHumidity:TextView = v.findViewById(R.id.humidity_value)
        val tvWind:TextView = v.findViewById(R.id.wind_value)
    }
}