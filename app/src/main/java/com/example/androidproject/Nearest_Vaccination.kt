package com.example.androidproject

import android.Manifest
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.location.LocationListener
import android.widget.TextView
import android.location.LocationManager
import android.os.Bundle
import com.example.androidproject.R
import android.content.Intent
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.location.Geocoder
import android.location.Location
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class Nearest_Vaccination : AppCompatActivity(), LocationListener {
    var tv_address: TextView? = null
    var locationManager: LocationManager? = null

    lateinit var searchbtn : Button
    lateinit var centerRV: RecyclerView
    lateinit var centerList: List<CenterRVModel>
    lateinit var centerRVAdapter: CenterRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearest_vaccine)
        grantPermission()
        tv_address = findViewById(R.id.tv_address)
        searchbtn = findViewById(R.id.btn_search)
        centerRV = findViewById(R.id.RV_centers)
        centerList = ArrayList<CenterRVModel>()

        checkLocation()
        getlocation()
    }

    private fun getlocation() {
        try {
            locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this@Nearest_Vaccination)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun checkLocation() {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!gpsEnabled && !networkEnabled) {
            AlertDialog.Builder(this@Nearest_Vaccination)
                    .setTitle("Enable GPS service")
                    .setCancelable(false)
                    .setPositiveButton("Enable") { dialog, which -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }.setNegativeButton("Cancel", null)
                    .show()
        }
    }

    private fun grantPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }
    }

    override fun onLocationChanged(location: Location) {
        try {
            val geocoder = Geocoder(this@Nearest_Vaccination, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            tv_address!!.text = addresses[0].getAddressLine(0)
            val pinCode = addresses[0].postalCode

            searchbtn.setOnClickListener{
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val dateStr : String = """$dayOfMonth-${month+1}-$year"""
                    getdata(pinCode,dateStr)
                },
                year, month, day)
                dpd.show()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getdata(pinCode:String, date: String){
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+pinCode+"&date="+date
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            try {
                val centerArray = response.getJSONArray("centers")
                if(centerArray.length().equals(0))
                {
                    Toast.makeText(this,"No Vaccination Centers Near You",Toast.LENGTH_SHORT).show()
                }
                for(i in 0 until centerArray.length()){
                    val centerObj = centerArray.getJSONObject(i)
                    val centername:String = centerObj.getString("name")
                    val centeraddress:String = centerObj.getString("address")
                    val centerfromtime:String = centerObj.getString("from")
                    val centertotime:String = centerObj.getString("to")
                    val feetype:String = centerObj.getString("fee_type")

                    val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                    val availablecapacity:Int = sessionObj.getInt("available_capacity")
                    val agelimit:Int = sessionObj.getInt("min_age_limit")
                    val vaccinename:String = sessionObj.getString("vaccine")

                    val center = CenterRVModel(
                            centername,
                            centeraddress,
                            centerfromtime,
                            centertotime,
                            feetype,
                            agelimit,
                            vaccinename,
                            availablecapacity
                    )
                    centerList = centerList+center
                }

                centerRVAdapter = CenterRVAdapter(centerList)
                centerRV.layoutManager = LinearLayoutManager(this)
                centerRV.adapter = centerRVAdapter

            }catch (e : JSONException){
                e.printStackTrace()
            }
        }, {
            error ->
            Toast.makeText(this,"Failed to get data",Toast.LENGTH_SHORT).show()
        })

        queue.add(request)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
}