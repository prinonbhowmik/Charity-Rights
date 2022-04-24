package com.charityright.charityauthority

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.charityright.charityauthority.auditor.viewModel.AuditorActivityViewModel
import com.charityright.charityauthority.databinding.ActivityAuditorBinding
import com.charityright.charityauthority.util.CustomDialog
import com.google.android.gms.location.*
import kotlinx.coroutines.launch


class AuditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuditorBinding

    private lateinit var navController: NavController

    private lateinit var auditorActivityViewModel: AuditorActivityViewModel

    // for current location
    var PERMISSION_ID = 44
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitude: String = ""
    var longitude: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auditorActivityViewModel = ViewModelProvider(this).get(AuditorActivityViewModel::class.java)
        CustomDialog.init(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()


        lifecycleScope.launch {
            auditorActivityViewModel.launchApiCall()
        }

        navController = findNavController(R.id.auditorHomeFragment)
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnSuccessListener {

                    if (it == null){
                        requestNewLocationData()
                    }else{
                        latitude = it.latitude.toString()
                        longitude = it.longitude.toString()
                        Log.wtf("lat", latitude)
                        Log.wtf("lon", longitude)

                        //set lat lon
                        auditorActivityViewModel.lat = latitude
                        auditorActivityViewModel.lon = longitude
                    }

                }

            } else {

                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)

            }

        } else {

            requestPermissions()

        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            val mLastLocation = locationResult.lastLocation
            latitude = mLastLocation.latitude.toString()
            longitude = mLastLocation.longitude.toString()

            auditorActivityViewModel.lat = latitude
            auditorActivityViewModel.lon = longitude

        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

}