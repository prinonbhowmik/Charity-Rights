package com.charityright.charityauthority.fragments

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentLocationPickerBinding
import com.charityright.charityauthority.databinding.FragmentLoginBinding
import com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel.AddSchoolFragmentViewModel
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.ArrayList
import android.app.Activity
import java.lang.Exception


class LocationPickerFragment : DialogFragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    OnMapReadyCallback, LocationListener {

    private var _binding: FragmentLocationPickerBinding? = null
    private val binding get() = _binding!!

    lateinit var mapView: MapView

    val TAG = "LocationPickerFragment"

    lateinit var googleMap: GoogleMap

    val REQUEST_CHECK_SETTINGS_GPS = 0x1
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2
    var lat = 0.0
    var lon = 0.0
    var googleApiClient: GoogleApiClient? = null
    var center: LatLng? = null
    var mylocation: Location? = null

    private lateinit var activityViewModel: adminHomeActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.color.transparent_black)
        _binding = FragmentLocationPickerBinding.inflate(inflater, container, false)

        activityViewModel = ViewModelProvider(requireActivity()).get(adminHomeActivityViewModel::class.java)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        val permissionLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (listPermissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    listPermissionsNeeded.toTypedArray(),
                    REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            }
        } else {
            loadData()
        }

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this)

        binding.myLocation.setOnClickListener {
            getMyLocation()
        }

        binding.apply.setOnClickListener {
            lifecycleScope.launch {
                activityViewModel.lat = lat
                activityViewModel.lon = lon
                activityViewModel.launchGetAddress(lat,lon,requireContext())
            }
            dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.70).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    override fun onConnected(p0: Bundle?) {
        checkPermissions()
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        try {
            mapView.onDestroy()
            googleApiClient!!.stopAutoManage(requireActivity())
            googleApiClient!!.disconnect()
        }catch (e: Exception){
            Log.wtf("LocationPickerFragment",e.message)
        }
    }

    private fun checkPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (listPermissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    listPermissionsNeeded.toTypedArray(),
                    REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            }
        } else {
            getMyLocation()
        }
    }

    /**
     * Function for get the lat, lon from gps
     */
    private fun loadData() {
        if (lat == 0.0 && lon == 0.0) {
            if (googleApiClient == null) {
                setUpGClient()
            } else if (mylocation == null) {
                getMyLocation()
            } else {
                lat = mylocation!!.latitude
                lon = mylocation!!.longitude
                Log.v("lat&lon", "lat = $lat&lon=$lon")
            }
        } else {
        }
    }

    @Synchronized
    private fun setUpGClient() {
        googleApiClient = GoogleApiClient.Builder(requireContext())
            .enableAutoManage(requireActivity(), 0, this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient!!.connect()
    }

    override fun onMapReady(map: GoogleMap) {
        Log.v(TAG, "map=$map")
        googleMap = map
        if (map != null) {
            map.uiSettings.isMyLocationButtonEnabled = false
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else {
                map.isMyLocationEnabled = true
            }

            if (lat == 0.0 && lon == 0.0) {
                when {
                    googleApiClient == null -> {
                        setUpGClient()
                    }
                    mylocation == null -> {
                        getMyLocation()
                    }
                    else -> {
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                mylocation!!.latitude,
                                mylocation!!.longitude
                            ), 15f
                        )
                        map.animateCamera(cameraUpdate)
                    }
                }
            } else {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 15f)
                map.animateCamera(cameraUpdate)
            }


            map.setOnCameraChangeListener(GoogleMap.OnCameraChangeListener { // TODO Auto-generated method stub
                center = map.getCameraPosition().target
                Log.v(
                    TAG,
                    "center-latitude=" + center!!.latitude + " &center-longitude=" + center!!.longitude
                )
                lat = center!!.latitude
                lon = center!!.longitude
                map.clear()
            })
        }
    }

    private fun getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient!!.isConnected) {
                val permissionLocation = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    val locationRequest = LocationRequest()
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val builder = LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                    builder.setAlwaysShow(true)
                    LocationServices.FusedLocationApi.requestLocationUpdates(
                        googleApiClient,
                        locationRequest,
                        this
                    )
                    val result: PendingResult<*> = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

                    result.setResultCallback {
                        ResultCallback<LocationSettingsResult> { result ->
                            val status = result.status
                            when (status.statusCode) {
                                LocationSettingsStatusCodes.SUCCESS -> {
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    val permissionLocation = ContextCompat
                                        .checkSelfPermission(
                                            requireContext(),
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                            .getLastLocation(googleApiClient)
                                        if (mylocation != null) {
                                            lat = mylocation!!.latitude
                                            lon = mylocation!!.longitude
                                        }
                                        Log.v("mylocation", "mylocation=$mylocation")
                                    }
                                }
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                                     // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(
                                            requireActivity(),
                                            REQUEST_CHECK_SETTINGS_GPS
                                        )
                                    } catch (e: IntentSender.SendIntentException) {
                                        // Ignore the error.
                                    }
                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        mylocation = location
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    mylocation!!.latitude,
                    mylocation!!.longitude
                ), 15f
            )
        )
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
    }
}