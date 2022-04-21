package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.CampaignListModel.CampaignListBaseModel
import com.charityright.bd.Models.DonationModel.DonationListBaseResponse
import com.charityright.bd.Models.SchoolListBaseModel.SchoolListBaseResponse
import com.charityright.bd.Models.UserInfo.ProfileInfoBaseResponse
import com.charityright.bd.Models.ZoneList.ZoneListBaseResponse
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    val userInfoResponse: LiveData<ProfileInfoBaseResponse?> = MutableLiveData()
    val campaignListResponse: LiveData<CampaignListBaseModel?> = MutableLiveData()
    val schoolListResponse: LiveData<SchoolListBaseResponse?> = MutableLiveData()
    val donationListResponse: LiveData<DonationListBaseResponse?> = MutableLiveData()
    val zoneListResponse: LiveData<ZoneListBaseResponse?> = MutableLiveData()

    suspend fun launchApiCall() {
        userInfoResponse as MutableLiveData
        campaignListResponse as MutableLiveData
        donationListResponse as MutableLiveData
        zoneListResponse as MutableLiveData

        try {
            CustomDialog.show()
            campaignListResponse.value = getCampaignListResponse()
            donationListResponse.value = getDonationListResponse()
            zoneListResponse.value = getZoneList()
            CustomDialog.dismiss()
        } catch (e: Exception) {
            CustomDialog.dismiss()
            Log.wtf("ActivityViewModel", e.stackTraceToString())
        }
    }

    suspend fun launchSchoolListApiCall(id: String) {
        schoolListResponse as MutableLiveData

        schoolListResponse.value = getSchoolListResponse(id)
    }

    private suspend fun getZoneList(): ZoneListBaseResponse {
        val response =
            ApiRequestInterface(getApplication<Application>().applicationContext).getZoneList()

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

    suspend fun launchProfileDetailsApiCall() {
        userInfoResponse as MutableLiveData

        try {
            userInfoResponse.value = getProfileInfoResponse()
        } catch (e: Exception) {
            CustomDialog.dismiss()
            Log.wtf("ActivityViewModel ProfileInfo", e.message)
        }
    }

    private suspend fun getDonationListResponse(): DonationListBaseResponse {
        val response =
            ApiRequestInterface(getApplication<Application>().applicationContext).getDonationList()

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

    private suspend fun getSchoolListResponse(id: String): SchoolListBaseResponse {
        val response =
            ApiRequestInterface(getApplication<Application>().applicationContext).getSchoolList(id)

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

    private suspend fun getCampaignListResponse(): CampaignListBaseModel {
        val response =
            ApiRequestInterface(getApplication<Application>().applicationContext).getCampaignList()

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

    private suspend fun getProfileInfoResponse(): ProfileInfoBaseResponse {
        val response =
            ApiRequestInterface(getApplication<Application>().applicationContext).getUserInfo(
                "Bearer " + CustomSharedPref.read(
                    "Token",
                    ""
                )
            )

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

}