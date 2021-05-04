package com.example.projectblueweather.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectblueweather.Adapter.CityAdapter
import com.example.projectblueweather.Model.City
import com.example.projectblueweather.R
import com.example.projectblueweather.Service.APIServiceInterface
import com.example.projectblueweather.Service.RetrofitClientInstance
import com.example.projectblueweather.Utils.Constants
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.*


class MainActivity : AppCompatActivity(), CityAdapter.OnItemClickListener {

    val REQUEST_CODE = 32

    // Google Konum Servisleri için API
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    // Views
    lateinit var tvLocation:TextView
    lateinit var rvCityList:RecyclerView
    lateinit var pbLoading:ProgressBar

    //Data
    var cityList : MutableList<City> = mutableListOf()

    //Adapter
    lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = CityAdapter(cityList, this)

        tvLocation = findViewById(R.id.location_value)
        pbLoading = findViewById(R.id.progressBar)
        rvCityList = findViewById(R.id.list_view)
        rvCityList.adapter = adapter
        rvCityList.layoutManager = LinearLayoutManager(this)
        rvCityList.setHasFixedSize(true)

        locationRequest = LocationRequest.create().apply {
            interval = 1000 * 30
            fastestInterval = 1000 * 5
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        locationCallback = object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
            }
        }

        updateGPS()

    } // onCreate end

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGPS()
            } else {
                Toast.makeText(
                    this,
                    "You need to enable GPS to continue with this application.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun updateGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                val lattlong = it.latitude.toString() + ", " + it.longitude.toString()
                getCityList(lattlong)
                val gcd = Geocoder(
                    baseContext,
                    Locale.getDefault()
                )
                val address = gcd.getFromLocation(it.latitude, it.longitude, 1)
                val address_string = address[0].adminArea + " / " + address[0].countryName
                tvLocation.text = address_string
            }
        }
        else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }
    }

    private fun getCityList(lattlong: String){
        val api = RetrofitClientInstance.retrofitInstance
                ?.create(APIServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = api?.getCityList(lattlong)?.awaitResponse()!!
            if (response.isSuccessful){
                val data = response.body()!!
                runOnUiThread {
                    cityList = data
                    adapter.changeDataset(cityList)
                    getCityWeatherThumbnails()
                    pbLoading.visibility = View.GONE
                }
            }
        }
    }

    private fun getCityWeatherThumbnails(){
        val api = RetrofitClientInstance.retrofitInstance
            ?.create(APIServiceInterface::class.java)

        for(city in cityList){
            GlobalScope.launch(Dispatchers.IO) {
                val response = api?.getCityDetails(city.id)?.awaitResponse()!!
                if (response.isSuccessful){
                    val data = response.body()!!
                    runOnUiThread {
                        city.thumbnail = Constants.Companion.IMG_BASE_URL + data.forecast[0].abbr + ".png"
                        adapter.changeDataset(cityList)
                    }
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
        val id = cityList[position].id
        val intent:Intent = Intent(this, CityDetailsActivity::class.java).apply {
            putExtra("CITY_ID", id)
        }
        startActivity(intent)
    }

}