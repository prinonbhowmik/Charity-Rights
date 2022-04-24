package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.dashboard.dashboardBaseResponse
import com.charityright.charityauthority.model.admin.zoneList.ZoneBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception
import java.util.*

class adminHomeActivityViewModel(application: Application) : AndroidViewModel(application) {

    val dashBoardResponse: LiveData<dashboardBaseResponse?> = MutableLiveData()
    val zoneResponse: LiveData<ZoneBaseResponse?> = MutableLiveData()
    val addZoneResponse: LiveData<ZoneBaseResponse?> = MutableLiveData()
    val addressResponse: LiveData<String?> = MutableLiveData()

    var lat: Double = 0.0
    var lon: Double = 0.0

    suspend fun launchGetAddress(lat:Double, lon: Double,context: Context){
        addressResponse as MutableLiveData

        try {
            addressResponse.value = getAddress(lat,lon,context)
        }catch (e: Exception){
            Log.wtf("AdminHomeActivityViewModel addressResponse",e.message)
        }
    }

    private suspend fun getAddress(lat:Double, lon: Double,context: Context): String? {
        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            lat,
            lon,
            1
        )

        return withContext(Dispatchers.IO){
            addresses[0].getAddressLine(0)
        }
    }

    suspend fun launchDashboardApiCall(){
        dashBoardResponse as MutableLiveData
        zoneResponse as MutableLiveData
        addZoneResponse as MutableLiveData

        try {
            CustomDialog.show()
            dashBoardResponse.value = getDashboardResponse()
            zoneResponse.value = getZoneResponse()
            addZoneResponse.value = getAddZoneResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("AdminHomeActivityViewModel",e.message)
        }
    }

    private suspend fun getAddZoneResponse(): ZoneBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).addZoneListApi("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

    private suspend fun getZoneResponse(): ZoneBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).zoneListApi("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

    private suspend fun getDashboardResponse(): dashboardBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).dashBoardAPi("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}