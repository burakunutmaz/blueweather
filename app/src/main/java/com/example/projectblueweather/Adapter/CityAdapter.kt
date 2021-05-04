package com.example.projectblueweather.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectblueweather.Model.City
import com.example.projectblueweather.R

class CityAdapter(
    private val cityList:MutableList<City>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.city_list_item, parent, false)
        return CityViewHolder(v)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentCity = cityList[position]

        val distance = currentCity.distance / 1999
        val distance_text = "$distance km"

        holder.cityName.text = currentCity.title
        holder.distance.text = distance_text
        Glide.with(context)
            .load(cityList[position].thumbnail)
            .centerCrop()
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    inner class CityViewHolder(v:View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val cityName:TextView = v.findViewById(R.id.city_name)
        val distance:TextView = v.findViewById(R.id.city_distance_value)
        val thumbnail:ImageView = v.findViewById(R.id.thumbnail)

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun changeDataset(cityList: List<City>){
        this.cityList.clear()
        this.cityList.addAll(cityList)
        notifyDataSetChanged()
    }

}