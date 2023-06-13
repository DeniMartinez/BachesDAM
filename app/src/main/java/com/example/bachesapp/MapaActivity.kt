package com.example.bachesapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bachesapp.databinding.ActivityMapaBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    internal  var dbHelper = DB_Helper(this)
    lateinit var binding:ActivityMapaBinding

    var googleMaP: GoogleMap ?= null

    private val puntosList: ArrayList<LatLng> = ArrayList()

    var PERMISSION_ID = 42
    var fusedLocationClient: FusedLocationProviderClient?= null
    private val locationCalback = object : LocationCallback(){
        override fun onLocationResult(locationResul: LocationResult) {
            var  lastLocation = locationResul.lastLocation

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        binding.btnGuardar.setOnClickListener{

            dbHelper.deleteAll()
            for (punto in puntosList){
                dbHelper.add(punto.longitude,punto.latitude)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMaP = map

        val res =  dbHelper.allData
        if (res.count != 0){
            while (res.moveToNext()){
               val latLng = LatLng(res.getDouble(0),res.getDouble(1))
                val markerOptions = MarkerOptions().position(latLng)
                googleMaP?.addMarker(markerOptions)
                puntosList.add(latLng)
            }
        }

        googleMaP?.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions().position(latLng)
            googleMaP?.addMarker(markerOptions)
            puntosList.add(latLng)
        }

        googleMaP?.setOnMarkerClickListener { marker ->
            marker.remove()
            val postion = marker.position
            puntosList.remove(postion)
            true
        }
    }

    private fun getLastLocation(){
        if (checkPermission()){
            if(isLocationEnabled()){
                fusedLocationClient?.lastLocation?.addOnCompleteListener{ task ->
                    var location = task.result
                    if (location == null){
                        requestNewLocationData()
                    }else {
                        googleMaP?.moveCamera(CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder().target(
                                LatLng(location.latitude,location.longitude)
                            ).zoom(15f).build()
                        ))
                    }
                }
            }
            else{
                Toast.makeText(this, "Habilita la localizacion", Toast.LENGTH_LONG).show()
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }
        }
        else{
            requestPermission()
        }
    }
    private fun  requestNewLocationData(){
        val locationRequest =  LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCalback,  Looper.myLooper())
    }

    private fun isLocationEnabled(): Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission():Boolean{
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
        PERMISSION_ID
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if (grantResults.isNotEmpty() && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }
    }

}